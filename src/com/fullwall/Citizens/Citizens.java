package com.fullwall.Citizens;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.fullwall.Citizens.Commands.CommandHandler;
import com.fullwall.Citizens.Listeners.CustomListen;
import com.fullwall.Citizens.Listeners.EntityListen;
import com.fullwall.Citizens.Listeners.PlayerListen;
import com.fullwall.Citizens.Listeners.PluginListen;
import com.fullwall.Citizens.Listeners.WorldListen;
import com.fullwall.Citizens.NPCTypes.Guards.GuardTask;
import com.fullwall.Citizens.NPCTypes.Healers.HealerTask;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager;
import com.fullwall.Citizens.NPCTypes.Wizards.WizardTask;
import com.fullwall.Citizens.NPCs.BasicNPCHandler;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.Citizens.Utils.Messaging;
import com.nijikokun.register.payment.Method;

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
	private final PlayerListen playerListener = new PlayerListen(this);
	public final BasicNPCHandler basicNPCHandler = new BasicNPCHandler(this);

	public static Citizens plugin;

	public static Method economy;

	public static final String separatorChar = "/";

	private static final String codename = "Realist";
	private static final String letter = "h";
	private static final String version = "1.0.8" + letter;
	public static boolean initialized = false;

	@Override
	public void onDisable() {
		// Save the local copy of our files to disk.
		PropertyManager.stateSave();

		basicNPCHandler.despawnAll();
		CreatureTask.despawnAll();

		Messaging.log("version [" + getVersion() + "] (" + codename
				+ ") disabled");
	}

	@Override
	public void onEnable() {
		// Register our commands.
		new CommandHandler(this).registerCommands();

		// Register our events.
		entityListener.registerEvents();
		customListener.registerEvents();
		worldListener.registerEvents();
		serverListener.registerEvents();
		playerListener.registerEvents();

		// Register files.
		PropertyManager.registerProperties();

		// Initialise Permissions.
		Permission.initialize(Bukkit.getServer());

		// Load settings.
		Constants.setupVariables();

		// schedule Creature tasks
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new CreatureTask(), Constants.spawnTaskDelay,
				Constants.spawnTaskDelay);
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new CreatureTask.CreatureTick(), 0, 1);
		plugin = this;

		// Reinitialise existing NPCs. Scheduled tasks run once all plugins are
		// loaded -> gives multiworld support.
		if (getServer().getScheduler().scheduleSyncDelayedTask(this,
				new Runnable() {
					@Override
					public void run() {
						setupNPCs();
					}
				}) == -1) {
			Messaging
					.log("Issue with scheduled loading of pre-existing NPCs. There may be a multiworld error.");
			setupNPCs();
		}

		// Schedule tasks TODO - Genericify
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
							Messaging.log("Saving npc files to disk...");
							PropertyManager.stateSave();
							Messaging.log("Saved.");
						}
					}, Constants.saveDelay, Constants.saveDelay);
		}

		QuestManager.initialise();
		Messaging.log("version [" + getVersion() + "] (" + codename
				+ ") loaded");
	}

	@Override
	public void onLoad() {
	}

	private void setupNPCs() {
		int count = 0;
		String UIDList = "";
		while (PropertyManager.getProfiles().pathExists(count)) {
			UIDList += count + ",";
			count++;
		}
		String[] values = UIDList.split(",");
		if (values.length > 0 && !values[0].isEmpty()) {
			for (String value : values) {
				int UID = Integer.parseInt(value);
				Location loc = PropertyManager.getBasic().getLocation(UID);
				if (loc != null) {
					NPCManager.register(UID, PropertyManager.getBasic()
							.getOwner(UID));
					ArrayDeque<String> text = PropertyManager.getBasic()
							.getText(UID);
					if (text != null) {
						NPCManager.setText(UID, text);
					}
				}
			}
		}
		Messaging.log("Loaded " + NPCManager.GlobalUIDs.size() + " NPCs.");
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new HealerTask(), HealerTask.getHealthRegenRate(),
				HealerTask.getHealthRegenRate());
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new WizardTask(), Constants.wizardManaRegenRate,
				Constants.wizardManaRegenRate);
		initialized = true;
	}

	/**
	 * Get the current version of Citizens
	 * 
	 * @return
	 */
	public static String getVersion() {
		return version;
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
	 * Returns whether the given item ID is usable as a tool.
	 * 
	 * @param key
	 * @param type
	 * @param sneaking
	 * 
	 * @return Whether the ID is used for a tool.
	 */
	public boolean validateTool(String key, int type, boolean sneaking) {
		if (Constants.useItemList) {
			String[] items = UtilityProperties.getSettings().getString(key)
					.split(",");
			List<String> item = Arrays.asList(items);
			if (item.contains("*")) {
				return true;
			}
			boolean isShift;
			for (String s : item) {
				isShift = false;
				if (s.contains("SHIFT-")) {
					s = s.replace("SHIFT-", "");
					isShift = true;
				}
				if (Integer.parseInt(s) == type && isShift == sneaking) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

}