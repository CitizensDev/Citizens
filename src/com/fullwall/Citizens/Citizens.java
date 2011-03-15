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
import com.fullwall.Citizens.Listeners.CustomListen;
import com.fullwall.Citizens.Listeners.EntityListen;
import com.fullwall.Citizens.Listeners.PluginListen;
import com.fullwall.Citizens.Listeners.WorldListen;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.Citizens.Utils.StringUtils;

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

	public static boolean followingEnabled = true;
	public static boolean useNPCColours = true;
	public static String NPCColour = "§f";
	public static String chatFormat = "[%name%]: ";
	public static String buildNumber = "1";

	public static Logger log = Logger.getLogger("Minecraft");
	public static boolean talkWhenClose = false;
	public static iConomy economy = null;

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
		setupNPCs();
		setupVariables();
		setupHelp();

		BasicNPCCommandExecutor executor = new BasicNPCCommandExecutor(this);
		this.getCommand("npc").setExecutor(executor);
		this.getCommand("citizens").setExecutor(executor);

		int delay = PropertyPool.settings.getInt("tick-delay");
		double range = PropertyPool.settings.getDouble("look-range");

		if (followingEnabled) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this,
					new TickTask(this, range), 5, delay);
		}
		log.info("[" + pdfFile.getName() + "]: version ["
				+ pdfFile.getVersion() + "_" + buildNumber + "] (" + codename
				+ ") loaded");
	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		handler.despawnAllNPCs();
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
		}
	}

	private void setupVariables() {
		useNPCColours = PropertyPool.settings.getBoolean("use-npc-colours");
		NPCColour = PropertyPool.settings.getString("npc-colour");
		followingEnabled = PropertyPool.settings.getBoolean("enable-following");
		talkWhenClose = PropertyPool.settings.getBoolean("talk-when-close");
		chatFormat = PropertyPool.settings.getString("chat-format");
	}

	private void setupNPCs() {
		String[] list = PropertyPool.locations.getString("list").split(",");
		for (String name : list) {
			Location loc = StringUtils.getLocationFromName(name);
			if (loc != null) {
				handler.spawnNPC(name, loc);
				ArrayList<String> text = PropertyPool.getText(name);
				if (text != null)
					handler.setNPCText(name, text);
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

	public boolean validateName(String name) {
		if (NPCManager.GlobalUIDs.containsKey(name)) {
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