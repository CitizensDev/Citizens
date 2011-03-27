package com.fullwall.Citizens;

import java.util.ArrayList;
import java.util.logging.Logger;

import me.taylorkelly.help.Help;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.coelho.iConomy.iConomy;

import com.fullwall.Citizens.CommandExecutors.BasicNPCCommandExecutor;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Listeners.CustomListen;
import com.fullwall.Citizens.Listeners.EntityListen;
import com.fullwall.Citizens.Listeners.PluginListen;
import com.fullwall.Citizens.Listeners.WorldListen;
import com.fullwall.Citizens.Utils.PropertyPool;

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
	private static final String codename = "Helpers";

	public static boolean defaultFollowingEnabled = true;
	public static boolean useNPCColours = true;
	public static String NPCColour = "§f";
	public static String chatFormat = "[%name%]: ";
	public static String buildNumber = "2";
	public static boolean convertSlashes = false;
	public static String convertToSpaceChar = "/";

	public static Logger log = Logger.getLogger("Minecraft");
	public static boolean defaultTalkWhenClose = false;
	public static iConomy economy = null;

	@Override
	public void onLoad() {
	}

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGED, l, Event.Priority.Normal,
				this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, l, Event.Priority.Normal,
				this);
		pm.registerEvent(Event.Type.CUSTOM_EVENT, cl, Event.Priority.Normal,
				this);
		pm.registerEvent(Event.Type.CHUNK_LOADED, wl, Event.Priority.Normal,
				this);
		pm.registerEvent(Event.Type.CHUNK_UNLOADED, wl, Event.Priority.Normal,
				this);
		getServer().getPluginManager().registerEvent(Event.Type.PLUGIN_ENABLE,
				pl, Event.Priority.Monitor, this);
		PluginDescriptionFile pdfFile = this.getDescription();
		plugin = this;

		Permission.initialize(getServer());
		setupVariables();
		setupNPCs();
		setupHelp();

		BasicNPCCommandExecutor executor = new BasicNPCCommandExecutor(this);
		this.getCommand("npc").setExecutor(executor);
		this.getCommand("citizens").setExecutor(executor);

		int delay = PropertyPool.settings.getInt("tick-delay");
		double range = PropertyPool.settings.getDouble("look-range");

		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new TickTask(this, range), 5, delay);

		log.info("[" + pdfFile.getName() + "]: Loaded "
				+ NPCManager.GlobalUIDs.size() + " NPC's");
		log.info("[" + pdfFile.getName() + "]: version ["
				+ pdfFile.getVersion() + "_" + buildNumber + "] (" + codename
				+ ") loaded ");
	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		handler.despawnAllNPCs();
		PropertyPool.locations.save();
		PropertyPool.texts.save();
		PropertyPool.items.save();
		PropertyPool.colours.save();
		PropertyPool.settings.save();
		log.info("[" + pdfFile.getName() + "]: version ["
				+ pdfFile.getVersion() + "_" + buildNumber + "] (" + codename
				+ ") disabled");
	}

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
			helpPlugin.registerCommand("npc talkwhenclose [true|false]",
					"Enable or disable if a NPC talks when aproached.", this,
					"citizens.general.talkwhenclose");
			helpPlugin.registerCommand("npc lookatplayers [true|false]",
					"Enable or disable if a NPC looks at near players.", this,
					"citizens.general.lookatplayers");
		}
	}

	private void setupVariables() {
		EconomyHandler.setUpVariables();
		if (!PropertyPool.settings.keyExists("slashes-to-spaces"))
			PropertyPool.settings.setBoolean("slashes-to-spaces", true);
		if (!PropertyPool.settings.keyExists("default-enable-following"))
			PropertyPool.settings.setBoolean("default-enable-following", true);
		if (!PropertyPool.settings.keyExists("default-talk-when-close"))
			PropertyPool.settings.setBoolean("default-talk-when-close", false);
		if (!PropertyPool.settings.keyExists("enable-following"))
			PropertyPool.settings.removeKey("enable-following");
		if (!PropertyPool.settings.keyExists("talk-when-close"))
			PropertyPool.settings.removeKey("talk-when-close");

		useNPCColours = PropertyPool.settings.getBoolean("use-npc-colours");
		NPCColour = PropertyPool.settings.getString("npc-colour");
		defaultFollowingEnabled = PropertyPool.settings
				.getBoolean("default-enable-following");
		defaultTalkWhenClose = PropertyPool.settings
				.getBoolean("default-talk-when-close");
		chatFormat = PropertyPool.settings.getString("chat-format");
		convertSlashes = PropertyPool.settings.getBoolean("slashes-to-spaces");
	}

	private void setupNPCs() {
		String[] list = PropertyPool.locations.getString("list").split(",");
		if (list.length > 0 && list[0] != "") {
			for (String name : list) {
				// Conversion from old to new save format:
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
					PropertyPool.talkWhenClose.setBoolean(UID, false);
					PropertyPool.locations.setString(
							"list",
							PropertyPool.locations.getString("list").replace(
									oldName, name));
					list = PropertyPool.locations.getString("list").split(",");
				}
				//
				Location loc = PropertyPool.getLocationFromName(Integer
						.valueOf(name.split("_")[0]));
				if (loc != null) {
					handler.spawnExistingNPC(name.split("_", 2)[1],
							Integer.valueOf(name.split("_")[0]));
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
	}

	public boolean shouldShowText(Integer type) {
		if (PropertyPool.settings.getBoolean("item-list-on") == true) {
			String[] items = PropertyPool.settings.getString("items")
					.split(",");
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

	public boolean validateUID(int UID) {
		if (NPCManager.GlobalUIDs.containsKey(UID)) {
			return true;
		} else
			return false;
	}

	public static boolean setiConomy(iConomy plugin) {
		if (economy == null) {
			economy = plugin;
		} else {
			return false;
		}
		return true;
	}
}