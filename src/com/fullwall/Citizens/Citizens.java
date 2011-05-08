package com.fullwall.Citizens;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.logging.Logger;

import me.taylorkelly.help.Help;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.iConomy.iConomy;

import com.fullwall.Citizens.CommandExecutors.BasicExecutor;
import com.fullwall.Citizens.CommandExecutors.HealerExecutor;
import com.fullwall.Citizens.CommandExecutors.QuesterExecutor;
import com.fullwall.Citizens.CommandExecutors.TogglerExecutor;
import com.fullwall.Citizens.CommandExecutors.TraderExecutor;
import com.fullwall.Citizens.CommandExecutors.WizardExecutor;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Healers.HealerTask;
import com.fullwall.Citizens.Listeners.CustomListen;
import com.fullwall.Citizens.Listeners.EntityListen;
import com.fullwall.Citizens.Listeners.PluginListen;
import com.fullwall.Citizens.Listeners.WorldListen;
import com.fullwall.Citizens.NPCs.BasicNPCHandler;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.Citizens.Utils.QuesterPropertyPool;
import com.fullwall.Citizens.Utils.TraderPropertyPool;
import com.fullwall.Citizens.Utils.WizardPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Citizens for Bukkit
 * 
 * @author fullwall
 */
public class Citizens extends JavaPlugin {
	public final EntityListen l = new EntityListen(this);
	public final CustomListen cl = new CustomListen(this);
	public final WorldListen wl = new WorldListen(this);
	public final PluginListen pl = new PluginListen(this);
	public final BasicNPCHandler handler = new BasicNPCHandler(this);

	public static Citizens plugin;

	public static int tickDelay = 1;
	public static int saveDelay = 72000;
	public static int maxNPCsPerPlayer = 10;
	public static int healerGiveHealthItem = 35;
	public static int healerTakeHealthItem = 276;
	public static int wizardMaxLocations = 10;
	public static int wizardInteractItem = 288;
	public static int questerInteractItem = 339;
	public static int questAcceptItem = 341;
	public static int questDenyItem = 338;
	public static int healerHealthRegenIncrement = 12000;

	public static double npcRange = 5;

	public static String chatFormat = "[%name%]: ";
	public static String convertToSpaceChar = "/";
	public static String NPCColour = "§f";

	public static boolean convertSlashes = false;
	public static boolean defaultFollowingEnabled = true;
	public static boolean defaultTalkWhenClose = false;
	public static boolean useNPCColours = true;

	public static iConomy economy = null;

	public static Logger log = Logger.getLogger("Minecraft");
	public static String separatorChar = "/";

	public static final int MAGIC_DATA_VALUE = -1;
	private static final String codename = "Helpers";
	public static String version = "";

	@Override
	public void onLoad() {
	}

	public void onEnable() {
		File file = new File("plugins/Citizens/Traders/Citizens.buyables");
		file.delete();
		file = new File("plugins/Citizens/Traders/Citizens.sellables");
		file.delete();
		BasicExecutor executor = new BasicExecutor(this);
		this.getCommand("npc").setExecutor(executor);
		this.getCommand("citizens").setExecutor(executor);
		this.getCommand("basic").setExecutor(executor);

		TraderExecutor traderExecutor = new TraderExecutor(this);
		this.getCommand("trader").setExecutor(traderExecutor);

		HealerExecutor healerExecutor = new HealerExecutor(this);
		this.getCommand("healer").setExecutor(healerExecutor);

		WizardExecutor wizardExecutor = new WizardExecutor(this);
		this.getCommand("wizard").setExecutor(wizardExecutor);

		QuesterExecutor questerExecutor = new QuesterExecutor(this);
		this.getCommand("quester").setExecutor(questerExecutor);

		TogglerExecutor togglerExecutor = new TogglerExecutor(this);
		this.getCommand("toggle").setExecutor(togglerExecutor);

		// Register our events.
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, l, Event.Priority.Normal,
				this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, l, Event.Priority.Normal,
				this);
		pm.registerEvent(Event.Type.CUSTOM_EVENT, cl, Event.Priority.Normal,
				this);
		pm.registerEvent(Event.Type.CHUNK_LOAD, wl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.CHUNK_UNLOAD, wl, Event.Priority.Normal,
				this);
		getServer().getPluginManager().registerEvent(Event.Type.PLUGIN_ENABLE,
				pl, Event.Priority.Monitor, this);

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
				new TickTask(this, npcRange), tickDelay, tickDelay);
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new Runnable() {
					@Override
					public void run() {
						log.info("[Citizens]: Saving npc files to disk...");
						PropertyPool.saveAll();
						TraderPropertyPool.saveAll();
						HealerPropertyPool.saveAll();
						WizardPropertyPool.saveAll();
						QuesterPropertyPool.saveAll();
						log.info("[Citizens]: Saved.");
					}
				}, saveDelay, saveDelay);

		log.info("[" + pdfFile.getName() + "]: version ["
				+ pdfFile.getVersion() + "] (" + codename + ") loaded");
	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		handler.despawnAllNPCs();
		// Save the local copy of our files to disk.
		PropertyPool.saveAll();
		TraderPropertyPool.saveAll();
		HealerPropertyPool.saveAll();
		WizardPropertyPool.saveAll();
		QuesterPropertyPool.saveAll();
		log.info("[" + pdfFile.getName() + "]: version ["
				+ pdfFile.getVersion() + "i] (" + codename + ") disabled");
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
		if (!PropertyPool.settings.keyExists("enable-following"))
			PropertyPool.settings.removeKey("enable-following");
		if (!PropertyPool.settings.keyExists("talk-when-close"))
			PropertyPool.settings.removeKey("talk-when-close");

		convertSlashes = PropertyPool.settings.getBoolean("slashes-to-spaces");
		defaultFollowingEnabled = PropertyPool.settings
				.getBoolean("default-enable-following");
		defaultTalkWhenClose = PropertyPool.settings
				.getBoolean("default-talk-when-close");
		useNPCColours = PropertyPool.settings.getBoolean("use-npc-colours");
		chatFormat = PropertyPool.settings.getString("chat-format");
		NPCColour = PropertyPool.settings.getString("npc-colour");
		if (PropertyPool.settings.keyExists("look-range"))
			npcRange = PropertyPool.settings.getDouble("look-range");
		if (PropertyPool.settings.keyExists("max-npcs-per-player"))
			maxNPCsPerPlayer = PropertyPool.settings
					.getInt("max-npcs-per-player");
		if (PropertyPool.settings.keyExists("healer-give-health-item"))
			healerGiveHealthItem = PropertyPool.settings
					.getInt("healer-give-health-item");
		if (PropertyPool.settings.keyExists("healer-take-health-item"))
			healerTakeHealthItem = PropertyPool.settings
					.getInt("healer-take-health-item");
		if (PropertyPool.settings.keyExists("healer-health-regen-increment"))
			healerHealthRegenIncrement = PropertyPool.settings
					.getInt("healer-health-regen-increment");
		if (PropertyPool.settings.keyExists("tick-delay"))
			tickDelay = PropertyPool.settings.getInt("tick-delay");
		if (PropertyPool.settings.keyExists("save-tick-delay"))
			saveDelay = PropertyPool.settings.getInt("save-tick-delay");
		if (PropertyPool.settings.keyExists("wizard-max-locations"))
			wizardMaxLocations = PropertyPool.settings
					.getInt("wizard-max-locations");
		if (PropertyPool.settings.keyExists("wizard-interact-item"))
			wizardInteractItem = PropertyPool.settings
					.getInt("wizard-interact-item");
		if (PropertyPool.settings.keyExists("quester-interact-item")) {
			questerInteractItem = PropertyPool.settings
					.getInt("quester-interact-item");
		}
	}

	private void setupNPCs() {
		// Start reloading old NPCs from the config files.
		String[] list = PropertyPool.locations.getString("list").split(",");
		if (list.length > 0 && !list[0].isEmpty()) {
			for (String name : list) {
				// Conversion from old to new save format:
				// Maybe ready to remove now? For next release anyways.
				if (name.split("_", 2).length == 1
						&& !name.split("_", 2)[0].isEmpty()) {
					int UID = PropertyPool.getNewNpcID();
					String oldName = name;
					name = UID + "_" + name;
					PropertyPool.locations.setString(UID,
							PropertyPool.locations.getString(oldName));
					PropertyPool.locations.removeKey(oldName);
					PropertyPool.colours.setString(UID,
							PropertyPool.colours.getString(oldName));
					PropertyPool.colours.removeKey(oldName);
					PropertyPool.items.setString(UID,
							PropertyPool.items.getString(oldName));
					PropertyPool.items.removeKey(oldName);
					PropertyPool.texts.setString(UID,
							PropertyPool.texts.getString(oldName));
					PropertyPool.texts.removeKey(oldName);
					PropertyPool.lookat.setBoolean(UID, true);
					PropertyPool.talkwhenclose.setBoolean(UID, false);
					PropertyPool.locations.setString(
							"list",
							PropertyPool.locations.getString("list").replace(
									oldName, name));
					list = PropertyPool.locations.getString("list").split(",");
				}
				Location loc = PropertyPool.getLocationFromID(Integer
						.valueOf(name.split("_")[0]));
				if (loc != null) {
					handler.spawnExistingNPC(name.split("_", 2)[1], Integer
							.valueOf(name.split("_")[0]), PropertyPool
							.getOwner(Integer.valueOf(name.split("_")[0])));
					ArrayList<String> text = PropertyPool.getText(Integer
							.valueOf(name.split("_")[0]));
					if (text != null)
						handler.setNPCText(Integer.valueOf(name.split("_")[0]),
								text);
				} else {
					PropertyPool.deleteNameFromList(name);
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
		String[] items = PropertyPool.settings.getString("select-item").split(
				",");
		ArrayList<String> item = new ArrayList<String>();
		for (String s : items) {
			item.add(s);
		}
		if (item.contains("*"))
			return true;
		items = PropertyPool.settings.getString("items").split(",");
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
		if (PropertyPool.settings.getBoolean("item-list-on")) {
			String[] items = PropertyPool.settings.getString(key).split(",");
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
	public static boolean setiConomy(iConomy plugin) {
		if (economy == null) {
			economy = plugin;
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
	 */
	private int getHealthRegenRate() {
		int delay = 0;
		if (!NPCManager.getNPCList().isEmpty()) {
			for (Entry<Integer, HumanNPC> entry : NPCManager.getNPCList()
					.entrySet()) {
				int level = HealerPropertyPool.getLevel(entry.getValue()
						.getUID());
				delay = healerHealthRegenIncrement * (11 - level);
				return delay;
			}
		} else {
			return 12000;
		}
		return delay;
	}
}