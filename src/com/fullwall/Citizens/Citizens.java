package com.fullwall.Citizens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijikokun.register.payment.Method;

import com.fullwall.Citizens.Commands.CommandHandler;
import com.fullwall.Citizens.Listeners.CustomListen;
import com.fullwall.Citizens.Listeners.EntityListen;
import com.fullwall.Citizens.Listeners.PluginListen;
import com.fullwall.Citizens.Listeners.WorldListen;
import com.fullwall.Citizens.NPCTypes.Guards.GuardTask;
import com.fullwall.Citizens.NPCTypes.Healers.HealerTask;
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

		// Register files.
		PropertyManager.registerProperties();

		// Register our commands.
		CommandHandler commandHandler = new CommandHandler(this);
		commandHandler.registerCommands();

		// Register our events.
		entityListener.registerEvents();
		customListener.registerEvents();
		worldListener.registerEvents();
		serverListener.registerEvents();

		PluginDescriptionFile pdfFile = this.getDescription();
		version = pdfFile.getVersion();

		Permission.initialize(getServer());
		Constants.setupVariables();

		plugin = this;

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

		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new TickTask(this, Constants.npcRange), Constants.tickDelay,
				Constants.tickDelay);
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new GuardTask(this), Constants.tickDelay, Constants.tickDelay);
		if (Constants.useSaveTask) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this,
					new Runnable() {
						@Override
						public void run() {
							log.info("[Citizens]: Saving npc files to disk...");
							PropertyManager.stateSave();
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
		PropertyManager.stateSave();

		basicNPCHandler.despawnAll();

		// Save the local copy of our files to disk.
		log.info("[" + pdfFile.getName() + "]: version ["
				+ pdfFile.getVersion() + "d] (" + codename + ") disabled");
	}

	private void setupNPCs() {
		// Start reloading old NPCs from the config files.
		String[] list = PropertyManager.getBasic().locations
				.getString("list").split(",");
		if (list.length > 0 && !list[0].isEmpty()) {
			for (String name : list) {
				Location loc = PropertyManager.getBasic()
						.getLocation(Integer.valueOf(name.split("_")[0]));
				if (loc != null) {
					NPCManager.register(
							name.split("_", 2)[1],
							Integer.valueOf(name.split("_")[0]),
							PropertyManager.getBasic().getOwner(
									Integer.valueOf(name.split("_")[0])));
					ArrayList<String> text = PropertyManager
							.getBasic().getText(
									Integer.valueOf(name.split("_")[0]));
					if (text != null)
						NPCManager.setText(Integer.valueOf(name.split("_")[0]),
								text);
				} else {
					PropertyManager.getBasic().deleteNameFromList(
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
	public boolean validateTool(String key, int type, boolean sneaking) {
		if (UtilityProperties.settings.getBoolean("item-list-on")) {
			String[] items = UtilityProperties.settings.getString(key).split(
					",");
			List<String> item = Arrays.asList(items);
			if (item.contains("*"))
				return true;
			boolean isShift = false;
			for (String s : item) {
				isShift = false;
				if (s.contains("SHIFT-")) {
					s = s.replace("SHIFT-", "");
					isShift = true;
				}
				if (Integer.parseInt(s) == type && isShift == sneaking)
					return true;
			}
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
		if (!NPCManager.getList().isEmpty()) {
			for (Entry<Integer, HumanNPC> entry : NPCManager.getList()
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