package com.fullwall.Citizens;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.fullwall.Citizens.Commands.CommandHandler;
import com.fullwall.Citizens.Listeners.CustomListen;
import com.fullwall.Citizens.Listeners.EntityListen;
import com.fullwall.Citizens.Listeners.PluginListen;
import com.fullwall.Citizens.Listeners.WorldListen;
import com.fullwall.Citizens.NPCTypes.Bandits.BanditTask;
import com.fullwall.Citizens.NPCTypes.Guards.GuardTask;
import com.fullwall.Citizens.NPCTypes.Healers.HealerTask;
import com.fullwall.Citizens.NPCs.BasicNPCHandler;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.ConfigurationHandler;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Properties.Properties.UtilityProperties;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
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
	public final BasicNPCHandler basicNPCHandler = new BasicNPCHandler(this);

	public static Citizens plugin;

	public static Method economy;

	public static Logger log = Logger.getLogger("Minecraft");
	public static String separatorChar = "/";

	private static final String codename = "Realist";
	private static final String letter = "e";
	private static String version = "1.0.8" + letter;

	/**
	 * Returns the current version of Citizens
	 * 
	 * @return
	 */
	private static String getVersion() {
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
	 * Get the health regeneration rate for a healer based on its level
	 * 
	 * @return
	 */
	private int getHealthRegenRate() {
		int delay = 0;
		if (!NPCManager.getList().isEmpty()) {
			for (Entry<Integer, HumanNPC> entry : NPCManager.getList()
					.entrySet()) {
				delay = Constants.healerHealthRegenIncrement
						* (11 - (entry.getValue().getHealer().getLevel()));
			}
		} else {
			delay = 12000;
		}
		return delay;
	}

	@Override
	public void onDisable() {
		PropertyManager.stateSave();

		basicNPCHandler.despawnAll();

		// Save the local copy of our files to disk.
		log.info("[Citizens]: version [" + getVersion() + "] (" + codename
				+ ") disabled");
	}

	@Override
	public void onEnable() {
		// TODO - remove on update.
		transferSettings();

		// Register files.
		PropertyManager.registerProperties();

		// TODO - remove on update.
		transferInventories();

		// Register our commands.
		CommandHandler commandHandler = new CommandHandler(this);
		commandHandler.registerCommands();

		// Register our events.
		entityListener.registerEvents();
		customListener.registerEvents();
		worldListener.registerEvents();
		serverListener.registerEvents();

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

		// Schedule tasks TODO - Genericify
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new TickTask(this, Constants.npcRange), Constants.tickDelay,
				Constants.tickDelay);
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new GuardTask(this), Constants.tickDelay, Constants.tickDelay);
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new BanditTask(this), Constants.tickDelay, Constants.tickDelay);
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

		log.info("[Citizens]: version [" + getVersion() + "] (" + codename
				+ ") loaded");
	}

	@Override
	public void onLoad() {
	}

	private void setupNPCs() {
		// Start reloading old NPCs from the config files.
		String[] list = PropertyManager.getBasic().locations.getString("list")
				.split(",");
		if (list.length > 0 && !list[0].isEmpty()) {
			for (String name : list) {
				Location loc = PropertyManager.getBasic().getLocation(
						Integer.valueOf(name.split("_")[0]));
				if (loc != null) {
					NPCManager.register(
							name.split("_", 2)[1],
							Integer.valueOf(name.split("_")[0]),
							PropertyManager.getBasic().getOwner(
									Integer.valueOf(name.split("_")[0])));
					LinkedList<String> text = PropertyManager.getBasic()
							.getText(Integer.valueOf(name.split("_")[0]));
					if (text != null)
						NPCManager.setText(Integer.valueOf(name.split("_")[0]),
								text);
				} else {
					PropertyManager.getBasic().deleteNameFromList(name);
				}
			}
		}
		log.info("[" + getDescription().getName() + "]: Loaded "
				+ NPCManager.GlobalUIDs.size() + " NPCs.");
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new HealerTask(), getHealthRegenRate(), getHealthRegenRate());
	}

	private void transferInventories() {
		File file = new File("plugins/Citizens/Traders/Citizens.inventories");
		if (file.exists()) {
			try {
				PropertyHandler temp = PropertyManager.getBasic().inventories;
				PropertyHandler handler = new PropertyHandler(
						"plugins/Citizens/Traders/Citizens.inventories");
				for (Entry<String, String> entry : handler.returnMap()
						.entrySet()) {
					temp.setString(entry.getKey(), entry.getValue());
				}
				handler.clear();
			} catch (Exception e) {
			}
			file.deleteOnExit();
		}
	}

	private void transferSettings() {
		File file = new File("plugins/Citizens/Citizens.settings");
		if (file.exists()) {
			ConfigurationHandler temp = new ConfigurationHandler(
					"plugins/Citizens/citizens.yml", true);
			PropertyHandler handler = new PropertyHandler(
					"plugins/Citizens/Citizens.settings");
			temp.setInt("general.limits.npcs-per-player",
					handler.getInt("max-NPCs-per-player"));
			temp.setBoolean("general.defaults.enable-following",
					handler.getBoolean("default-enable-following"));
			temp.setBoolean("general.defaults.talk-when-close",
					handler.getBoolean("default-talk-when-close"));
			temp.setBoolean("general.colors.use-npc-colours",
					handler.getBoolean("use-npc-colours"));
			temp.setString("general.colors.npc-colour",
					handler.getString("npc-colour"));
			temp.setString("general.chat.format",
					handler.getString("chat-format"));
			temp.setBoolean("general.chat.slashes-to-spaces",
					handler.getBoolean("slashes-to-spaces"));
			temp.setString("general.chat.default-text",
					handler.getString("default-text"));
			temp.setInt("general.wizards.wizard-max-locations",
					handler.getInt("wizard-max-locations"));
			// Item Settings
			temp.setBoolean("items.item-list-on",
					handler.getBoolean("item-list-on"));
			temp.setString("items.basic.talk-items", handler.getString("items"));
			temp.setString("items.basic.select-items",
					handler.getString("select-item"));
			temp.setInt("items.healers.take-health-item",
					handler.getInt("healer-take-health-item"));
			temp.setInt("items.healers.give-health-item",
					handler.getInt("healer-give-health-item"));
			temp.setInt("items.wizards.interact-item",
					handler.getInt("wizard-interact-item"));
			// Tick Settings
			temp.setInt("ticks.general.delay", handler.getInt("tick-delay"));
			temp.setBoolean("ticks.saving.use-task",
					handler.getBoolean("use-save-task"));
			temp.setBoolean("ticks.saving.save-often",
					handler.getBoolean("save-often"));
			temp.setInt("ticks.saving.delay", handler.getInt("save-tick-delay"));
			temp.setInt("ticks.healers.health-regen-increment",
					handler.getInt("healer-health-regen-increment"));
			// Range Settings
			temp.setInt("range.basic.look", handler.getInt("look-range"));
			handler.clear();
			file.deleteOnExit();
		}
		file = new File("plugins/Citizens/Citizens.economy");
		if (file.exists()) {
			ConfigurationHandler temp = new ConfigurationHandler(
					"plugins/Citizens/economy.yml", true);
			PropertyHandler handler = new PropertyHandler(
					"plugins/Citizens/Citizens.economy");
			temp.setBoolean("economy.use-economy",
					handler.getBoolean("use-economy"));
			temp.setBoolean("economy.use-econplugin",
					handler.getBoolean("use-econplugin"));

			temp.setInt("prices.basic.creation.item",
					handler.getInt("basic-npc-create-item"));
			temp.setInt("prices.basic.creation.item-currency-id",
					handler.getInt("basic-npc-create-item-currency-id"));
			temp.setInt("prices.basic.creation.econplugin",
					handler.getInt("basic-npc-create-econplugin"));

			temp.setInt("prices.blacksmith.creation.item",
					handler.getInt("blacksmith-npc-create-item"));
			temp.setInt("prices.blacksmith.creation.item-currency-id",
					handler.getInt("blacksmith-npc-create-item-currency-id"));
			temp.setInt("prices.blacksmith.creation.econplugin",
					handler.getInt("blacksmith-npc-create-econplugin"));

			temp.setInt("prices.blacksmith.armorrepair.item",
					handler.getInt("blacksmith-armor-repair-item"));
			temp.setInt("prices.blacksmith.armorrepair.item-currency-id",
					handler.getInt("blacksmith-armor-repair-item-currency-id"));
			temp.setInt("prices.blacksmith.armorrepair.econplugin",
					handler.getInt("blacksmith-armor-repair-econplugin"));

			temp.setInt("prices.blacksmith.toolrepair.item",
					handler.getInt("blacksmith-tool-repair-item"));
			temp.setInt("prices.blacksmith.toolrepair.item-currency-id",
					handler.getInt("blacksmith-tool-repair-item-currency-id"));
			temp.setInt("prices.blacksmith.toolrepair.econplugin",
					handler.getInt("blacksmith-tool-repair-econplugin"));

			temp.setInt("prices.healer.creation.item",
					handler.getInt("healer-npc-create-item"));
			temp.setInt("prices.healer.creation.item-currency-id",
					handler.getInt("healer-npc-create-item-currency-id"));
			temp.setInt("prices.healer.creation.econplugin",
					handler.getInt("healer-npc-create-econplugin"));

			temp.setInt("prices.healer.levelup.item",
					handler.getInt("healer-level-up-item"));
			temp.setInt("prices.healer.levelup.item-currency-id",
					handler.getInt("healer-level-up-item-currency-id"));
			temp.setInt("prices.healer.levelup.econplugin",
					handler.getInt("healer-level-up-econplugin"));

			temp.setInt("prices.trader.creation.item",
					handler.getInt("trader-npc-create-item"));
			temp.setInt("prices.trader.creation.item-currency-id",
					handler.getInt("trader-npc-create-item-currency-id"));
			temp.setInt("prices.trader.creation.econplugin",
					handler.getInt("trader-npc-create-econplugin"));

			temp.setInt("prices.wizard.creation.item",
					handler.getInt("wizard-npc-create-item"));
			temp.setInt("prices.wizard.creation.item-currency-id",
					handler.getInt("wizard-npc-create-item-currency-id"));
			temp.setInt("prices.wizard.creation.econplugin",
					handler.getInt("wizard-npc-create-econplugin"));

			temp.setInt("prices.wizard.teleport.item",
					handler.getInt("wizard-teleport-item"));
			temp.setInt("prices.wizard.teleport.item-currency-id",
					handler.getInt("wizard-teleport-item-currency-id"));
			temp.setInt("prices.wizard.teleport.econplugin",
					handler.getInt("wizard-teleport-econplugin"));
			handler.clear();
			file.deleteOnExit();
		}
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
			boolean isShift = false;
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
		} else {
			return true;
		}
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
		} else {
			return false;
		}
	}
}