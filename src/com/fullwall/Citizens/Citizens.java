package com.fullwall.Citizens;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.logging.Logger;

import me.taylorkelly.help.Help;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijikokun.register.payment.Method;

import com.fullwall.Citizens.CommandExecutors.CommandHandler;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Healers.HealerTask;
import com.fullwall.Citizens.Listeners.CustomListen;
import com.fullwall.Citizens.Listeners.EntityListen;
import com.fullwall.Citizens.Listeners.PluginListen;
import com.fullwall.Citizens.Listeners.WorldListen;
import com.fullwall.Citizens.NPCs.BasicNPCHandler;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Citizens for Bukkit
 * 
 * @author fullwall
 */
public class Citizens extends JavaPlugin {
	private final EntityListen entityListener = new EntityListen(this);
	private final CustomListen customListener = new CustomListen(this);
	private final WorldListen worldListener = new WorldListen(this);
	private final PluginListen serverListener = new PluginListen(this);
	public final BasicNPCHandler basicNPCHandler = new BasicNPCHandler(this);

	public static Citizens plugin;

	public static Method economy;

	public static Logger log = Logger.getLogger("Minecraft");
	public static String separatorChar = "/";

	public static final int MAGIC_DATA_VALUE = -1;
	private static final String codename = "Helpers";
	public static String version = "1.0.8";

	@Override
	public void onLoad() {
	}

	public void onEnable() {
		// Register our commands.
		CommandHandler commandHandler = new CommandHandler(this);
		commandHandler.registerCommands();

		// Register files.
		PropertyManager.registerProperties();

		// Register our events.
		entityListener.registerEvents();
		customListener.registerEvents();
		worldListener.registerEvents();
		serverListener.registerEvents();

		PluginDescriptionFile pdfFile = this.getDescription();
		version = pdfFile.getVersion();

		Permission.initialize(getServer());
		setupVariables();

		// Reinitialise existing NPCs. Scheduled tasks run once all plugins are
		// loaded. This means that multiworld will now work without hacky
		// workarounds.
		if (getServer().getScheduler().scheduleSyncDelayedTask(this,
				new Runnable() {
					@Override
					public void run() {
						setupNPCs();
					}
				}) == -1) {
			log.info("[Citizens]: Issue with scheduled loading of pre-existing NPCs. There may be a multiworld error.");
			setupNPCs();
		}
		// Compatibility with Help plugin.
		setupHelp();

		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new TickTask(this, Constants.npcRange), Constants.tickDelay,
				Constants.tickDelay);
		if (Constants.useSaveTask) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this,
					new Runnable() {
						@Override
						public void run() {
							log.info("[Citizens]: Saving npc files to disk...");
							PropertyManager.saveFiles();
							log.info("[Citizens]: Saved.");
						}
					}, Constants.saveDelay, Constants.saveDelay);
		}
		log.info("[" + pdfFile.getName() + "]: version ["
				+ pdfFile.getVersion() + "d] (" + codename + ") loaded");
	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		basicNPCHandler.despawnAllNPCs();
		// Save the local copy of our files to disk.
		PropertyManager.saveFiles();
		log.info("[" + pdfFile.getName() + "]: version ["
				+ pdfFile.getVersion() + "d] (" + codename + ") disabled");
	}

	/**
	 * Provides a help menu for use with tkelly's help plugin.
	 */
	private void setupHelp() {
		Plugin test = getServer().getPluginManager().getPlugin("Help");
		if (test != null) {
			Help helpPlugin = ((Help) test);
			// create,set,add,name,remove,reset,colour,item,helmet|torso|legs|boots
			helpPlugin.registerCommand("npc create [name] (text)",
					"Creates an NPC.", this, "citizens.basic.create");
			helpPlugin.registerCommand("npc set [name] [text]",
					"Sets an NPC's text.", this, "citizens.basic.settext");
			helpPlugin.registerCommand("npc add [name] [text]",
					"Adds to an NPC's text.", this, "citizens.basic.settext");
			helpPlugin.registerCommand("npc name [name] [newname]",
					"Sets an NPC's name.", this, "citizens.general.create");
			helpPlugin.registerCommand("npc remove [name]",
					"Removes an NPC and deletes its data.", this,
					"citizens.general.remove.singular");
			helpPlugin.registerCommand("npc remove all", "Removes all NPCs.",
					this, "citizens.general.remove.all");
			helpPlugin.registerCommand("npc reset [name]",
					"Clears the messages of an NPC.", this,
					"citizens.basic.settext");
			helpPlugin.registerCommand("npc colo(u)r [name] [&(code)]",
					"Sets an NPC's name colour", this,
					"citizens.general.color", "citizens.general.colour");
			helpPlugin.registerCommand("npc item [name] [itemID|name]",
					"Sets an NPC's held item.", this,
					"citizens.general.setitem");
			helpPlugin.registerCommand(
					"npc helmet|torso|legs|boots [name] [itemID|name]",
					"Sets an NPC's armor item.", this,
					"citizens.general.setitem");
			helpPlugin.registerCommand("npc move [name]",
					"Moves an NPC to your location.", this,
					"citizens.general.move");
			helpPlugin.registerCommand("npc tp [name]",
					"Teleports you to the location of an NPC.", this,
					"citizens.general.move");
			helpPlugin.registerCommand("npc copy", "Copies the selected NPC.",
					this, "citizens.general.copy");
			helpPlugin.registerCommand("npc getid",
					"Gets the ID of the selected NPC.", this,
					"citizens.general.getid");
			helpPlugin.registerCommand("npc select [id]",
					"Selects the NPC with the given id.", this,
					"citizens.general.select");
			helpPlugin.registerCommand("npc getowner",
					"Gets the owner of the selected NPC.", this,
					"citizens.general.getowner");
			helpPlugin.registerCommand("npc setowner [name]",
					"Sets the owner of the selected NPC.", this,
					"citizens.general.setowner");
			helpPlugin.registerCommand("npc addowner [name]",
					"Adds an owner to the selected NPC.", this,
					"citizens.general.addowner");
			helpPlugin.registerCommand("npc talkwhenclose [true|false]",
					"Enable or disable if a NPC talks when aproached.", this,
					"citizens.general.talkwhenclose");
			helpPlugin.registerCommand("npc lookatplayers [true|false]",
					"Enable or disable if a NPC looks at near players.", this,
					"citizens.general.lookatplayers");
		}
	}

	/**
	 * Sets up miscellaneous variables, mostly reading from property files.
	 */
	private void setupVariables() {
		// Used for static access to this object.
		plugin = this;

		EconomyHandler.setUpVariables();

		// Boolean defaults
		Constants.convertSlashes = UtilityProperties.settings
				.getBoolean("slashes-to-spaces");
		Constants.defaultFollowingEnabled = UtilityProperties.settings
				.getBoolean("default-enable-following");
		Constants.defaultTalkWhenClose = UtilityProperties.settings
				.getBoolean("default-talk-when-close");
		Constants.useNPCColours = UtilityProperties.settings
				.getBoolean("use-npc-colours");

		// String defaults
		Constants.chatFormat = UtilityProperties.settings
				.getString("chat-format");
		Constants.NPCColour = UtilityProperties.settings
				.getString("npc-colour");

		// Double defaults
		Constants.npcRange = UtilityProperties.settings.getDouble("look-range");

		// Int defaults
		Constants.maxNPCsPerPlayer = UtilityProperties.settings
				.getInt("max-npcs-per-player");
		Constants.healerGiveHealthItem = UtilityProperties.settings
				.getInt("healer-give-health-item");
		Constants.healerTakeHealthItem = UtilityProperties.settings
				.getInt("healer-take-health-item");
		Constants.healerHealthRegenIncrement = UtilityProperties.settings
				.getInt("healer-health-regen-increment");
		Constants.tickDelay = UtilityProperties.settings.getInt("tick-delay");
		Constants.saveDelay = UtilityProperties.settings
				.getInt("save-tick-delay");
		Constants.wizardMaxLocations = UtilityProperties.settings
				.getInt("wizard-max-locations");
		Constants.wizardInteractItem = UtilityProperties.settings
				.getInt("wizard-interact-item");
	}

	private void setupNPCs() {
		// Start reloading old NPCs from the config files.
		String[] list = PropertyManager.getBasicProperties().locations
				.getString("list").split(",");
		if (list.length > 0 && !list[0].isEmpty()) {
			for (String name : list) {
				Location loc = PropertyManager.getBasicProperties()
						.getLocationFromID(Integer.valueOf(name.split("_")[0]));
				if (loc != null) {
					basicNPCHandler.spawnExistingNPC(
							name.split("_", 2)[1],
							Integer.valueOf(name.split("_")[0]),
							PropertyManager.getBasicProperties().getOwner(
									Integer.valueOf(name.split("_")[0])));
					ArrayList<String> text = PropertyManager
							.getBasicProperties().getText(
									Integer.valueOf(name.split("_")[0]));
					if (text != null)
						basicNPCHandler.setNPCText(
								Integer.valueOf(name.split("_")[0]), text);
				} else {
					PropertyManager.getBasicProperties().deleteNameFromList(
							name);
				}
			}
		}
		log.info("[" + this.getDescription().getName() + "]: Loaded "
				+ NPCManager.GlobalUIDs.size() + " NPCs.");
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new HealerTask(), getHealthRegenRate(), getHealthRegenRate());
	}

	/**
	 * If "*" is used as a tool for anything (little bit of a hack for
	 * displaying selection notifications).
	 * 
	 * @return Whether "*" is used.
	 */
	public boolean canSelectAny() {
		String[] items = UtilityProperties.settings.getString("select-item")
				.split(",");
		ArrayList<String> item = new ArrayList<String>();
		for (String s : items) {
			item.add(s);
		}
		if (item.contains("*"))
			return true;
		items = UtilityProperties.settings.getString("items").split(",");
		item = new ArrayList<String>();
		for (String s : items) {
			item.add(s);
		}
		if (item.contains("*"))
			return true;
		return false;
	}

	/**
	 * Returns whether the given item ID is usable as a tool.
	 * 
	 * @param key
	 *            , type
	 * @return Whether the ID is used for a tool.
	 */
	public boolean validateTool(String key, int type) {
		if (UtilityProperties.settings.getBoolean("item-list-on")) {
			String[] items = UtilityProperties.settings.getString(key).split(
					",");
			ArrayList<String> item = new ArrayList<String>();
			for (String s : items) {
				item.add(s);
			}
			if (item.contains("" + type) || item.contains("*"))
				return true;
			else if (type == 0 && item.contains("0"))
				return true;
			else
				return false;
		} else
			return true;
	}

	/**
	 * Checks if UID is being used by an NPC.
	 * 
	 * @param UID
	 *            to check.
	 * @return Whether it is used.
	 */
	public boolean validateUID(int UID) {
		if (NPCManager.GlobalUIDs.containsKey(UID)) {
			return true;
		} else
			return false;
	}

	/**
	 * A method used for iConomy support.
	 * 
	 * @param iConomy
	 *            plugin
	 * @return
	 */
	public static boolean setMethod(Method method) {
		if (economy == null) {
			economy = method;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Returns the current version of Citizens as specified in the plugin.yml.
	 * 
	 * @return
	 */
	public static String getVersion() {
		return version;
	}

	/**
	 * Schedule a timer to regenerate a healer's health based on their level
	 * 
	 * @return
	 */
	private int getHealthRegenRate() {
		int delay = 0;
		if (!NPCManager.getNPCList().isEmpty()) {
			for (Entry<Integer, HumanNPC> entry : NPCManager.getNPCList()
					.entrySet()) {
				int level = entry.getValue().getHealer().getLevel();
				delay = Constants.healerHealthRegenIncrement * (11 - level);
				return delay;
			}
		} else {
			return 12000;
		}
		return delay;
	}
}