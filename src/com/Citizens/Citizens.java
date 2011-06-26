package com.Citizens;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.Citizens.Constants;
import com.Citizens.CreatureTask;
import com.Citizens.Permission;
import com.Citizens.TickTask;
import com.Citizens.Commands.CommandHandler;
import com.Citizens.Interfaces.NPCFactory;
import com.Citizens.Interfaces.NPCType;
import com.Citizens.Listeners.ServerListen;
import com.Citizens.NPCTypes.Questers.QuesterNPC;
import com.Citizens.NPCTypes.Traders.TraderNPC;
import com.Citizens.NPCTypes.Wizards.WizardNPC;
import com.Citizens.Properties.PropertyManager;
import com.Citizens.Properties.Properties.BlacksmithProperties;
import com.Citizens.Properties.Properties.GuardProperties;
import com.Citizens.Properties.Properties.HealerProperties;
import com.Citizens.Properties.Properties.QuesterProperties;
import com.Citizens.Properties.Properties.TraderProperties;
import com.Citizens.Properties.Properties.UtilityProperties;
import com.Citizens.Properties.Properties.WizardProperties;
import com.Citizens.Utils.MessageUtils;
import com.Citizens.resources.redecouverte.NPClib.HumanNPC;
import com.Citizens.resources.sk89q.commands.CitizensCommandsManager;
import com.Citizens.resources.sk89q.commands.CommandPermissionsException;
import com.Citizens.resources.sk89q.commands.CommandUsageException;
import com.Citizens.resources.sk89q.commands.MissingNestedCommandException;
import com.Citizens.resources.sk89q.commands.RequirementMissingException;
import com.Citizens.resources.sk89q.commands.UnhandledCommandException;
import com.Citizens.resources.sk89q.commands.WrappedCommandException;
import com.Citizens.Listeners.EntityListen;
import com.Citizens.Listeners.PlayerListen;
import com.Citizens.Listeners.WorldListen;
import com.Citizens.NPCTypes.Blacksmiths.BlacksmithNPC;
import com.Citizens.NPCTypes.Guards.GuardNPC;
import com.Citizens.NPCTypes.Guards.GuardTask;
import com.Citizens.NPCTypes.Healers.HealerNPC;
import com.Citizens.NPCTypes.Healers.HealerTask;
import com.Citizens.NPCTypes.Questers.Quests.QuestManager;
import com.Citizens.NPCTypes.Wizards.WizardTask;
import com.Citizens.NPCs.NPCManager;
import com.Citizens.Utils.Messaging;
import com.Citizens.resources.nijikokun.register.payment.Method;

/**
 * Citizens for Bukkit
 */
public class Citizens extends JavaPlugin {
	public static Citizens plugin;

	public static Method economy;

	public static final String separatorChar = "/";

	private static final String codename = "Riot";
	private static final String letter = "";
	private static final String version = "1.0.9" + letter;

	public static CitizensCommandsManager<Player> commands = new CitizensCommandsManager<Player>();

	public static boolean initialized = false;

	@Override
	public void onDisable() {
		// Save the local copy of our files to disk.
		PropertyManager.stateSave();

		NPCManager.despawnAll();
		CreatureTask.despawnAll();

		Messaging.log("version [" + getVersion() + "] (" + codename
				+ ") disabled");
	}

	@Override
	public void onEnable() {
		plugin = this;

		// Register NPC types.
		registerTypes();

		// Register our commands.
		CommandHandler.registerCommands();

		// Register our events.
		new EntityListen().registerEvents();
		new WorldListen().registerEvents();
		new ServerListen().registerEvents();
		new PlayerListen().registerEvents();

		// Register files.
		PropertyManager.registerProperties();

		// Initialize Permissions.
		Permission.initialize(Bukkit.getServer());

		// Load settings.
		Constants.setupVariables();

		// schedule Creature tasks
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new CreatureTask(), Constants.spawnTaskDelay,
				Constants.spawnTaskDelay);
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new CreatureTask.CreatureTick(), 0, 1);

		// Reinitialize existing NPCs. Scheduled tasks run once all plugins are
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
				new TickTask(Constants.npcRange), Constants.tickDelay,
				Constants.tickDelay);
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new GuardTask(), Constants.tickDelay, Constants.tickDelay);
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

		QuestManager.initialize();
		Messaging.log("version [" + getVersion() + "] (" + codename
				+ ") loaded");
	}

	private void registerTypes() {
		NPCManager
				.registerType(new NPCType("blacksmith",
						new BlacksmithProperties(), new NPCFactory(
								BlacksmithNPC.class)));
		NPCManager.registerType(new NPCType("guard", new GuardProperties(),
				new NPCFactory(GuardNPC.class)));
		NPCManager.registerType(new NPCType("healer", new HealerProperties(),
				new NPCFactory(HealerNPC.class)));
		NPCManager.registerType(new NPCType("quester", new QuesterProperties(),
				new NPCFactory(QuesterNPC.class)));
		NPCManager.registerType(new NPCType("trader", new TraderProperties(),
				new NPCFactory(TraderNPC.class)));
		NPCManager.registerType(new NPCType("wizard", new WizardProperties(),
				new NPCFactory(WizardNPC.class)));
	}

	@Override
	public void onLoad() {
	}

	private void setupNPCs() {
		int count = 0;
		String UIDList = "";
		while (PropertyManager.getNPCProfiles().pathExists(count)) {
			UIDList += count + ",";
			++count;
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

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(MessageUtils.mustBeIngameMessage);
			return true;
		}
		Player player = (Player) sender;
		try {
			// must put command into split.
			String[] split = new String[args.length + 1];
			System.arraycopy(args, 0, split, 1, args.length);
			split[0] = command.getName().toLowerCase();

			String modifier = "";
			if (args.length > 0)
				modifier = args[0];

			// No command found!
			if (!commands.hasCommand(split[0], modifier)) {
				return false;
			}

			HumanNPC npc = null;
			if (NPCManager.validateSelected(player)) {
				npc = NPCManager.get(NPCManager.selectedNPCs.get(player
						.getName()));
			}
			try {
				commands.execute(split, player, player, npc);
			} catch (CommandPermissionsException e) {
				Messaging.send(player, MessageUtils.noPermissionsMessage);
			} catch (MissingNestedCommandException e) {
				Messaging.sendError(player, e.getUsage());
			} catch (CommandUsageException e) {
				Messaging.sendError(player, e.getMessage());
				Messaging.sendError(player, e.getUsage());
			} catch (RequirementMissingException e) {
				Messaging.sendError(player, e.getMessage());
			} catch (WrappedCommandException e) {
				throw e.getCause();
			} catch (UnhandledCommandException e) {
				return false;
			}
		} catch (NumberFormatException e) {
			Messaging.sendError(player, "That is not a valid number.");
		} catch (Throwable excp) {
			Messaging.sendError(player,
					"Please report this error: [See console]");
			Messaging.sendError(player,
					excp.getClass().getName() + ": " + excp.getMessage());
			excp.printStackTrace();
		}
		return true;
	}
}