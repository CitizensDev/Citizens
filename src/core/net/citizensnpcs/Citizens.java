package net.citizensnpcs;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.citizensnpcs.commands.Commands;
import net.citizensnpcs.listeners.EntityListen;
import net.citizensnpcs.listeners.Listener;
import net.citizensnpcs.listeners.PlayerListen;
import net.citizensnpcs.listeners.ServerListen;
import net.citizensnpcs.listeners.WorldListen;
import net.citizensnpcs.npcs.NPCDataManager;
import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCLoader;
import net.citizensnpcs.npctypes.CitizensNPCManager;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.properties.SettingsManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.register.payment.Method;
import net.citizensnpcs.resources.sk89q.CitizensCommandsManager;
import net.citizensnpcs.resources.sk89q.CommandPermissionsException;
import net.citizensnpcs.resources.sk89q.CommandUsageException;
import net.citizensnpcs.resources.sk89q.MissingNestedCommandException;
import net.citizensnpcs.resources.sk89q.RequirementMissingException;
import net.citizensnpcs.resources.sk89q.ServerCommandException;
import net.citizensnpcs.resources.sk89q.UnhandledCommandException;
import net.citizensnpcs.resources.sk89q.WrappedCommandException;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Joiner;

/**
 * Citizens - NPCs for Bukkit
 */
public class Citizens extends JavaPlugin {
	public static Citizens plugin;
	public static Method economy;
	public static final String separatorChar = "/";

	private static final String version = "1.1";

	public static CitizensCommandsManager<Player> commands = new CitizensCommandsManager<Player>();

	public static boolean initialized = false;

	public static List<String> loadedTypes = new ArrayList<String>();

	@Override
	public void onEnable() {
		plugin = this;
		// Load settings.
		SettingsManager.setupVariables();

		// Load NPC types.
		loadNPCTypes();

		// Register our commands.
		Commands.registerCommands();

		// Register our events.
		new EntityListen().registerEvents();
		new WorldListen().registerEvents();
		new ServerListen().registerEvents();
		new PlayerListen().registerEvents();

		// Initialize Permissions.
		Permission.initialize(Bukkit.getServer());

		// schedule Creature tasks
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new CreatureTask(),
				SettingsManager.getInt("CreatureNPCSpawnDelay"),
				SettingsManager.getInt("CreatureNPCSpawnDelay"));
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

		if (SettingsManager.getBoolean("UseSaveTask")) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this,
					new Runnable() {
						@Override
						public void run() {
							Messaging.log("Saving npc files to disk...");
							PropertyManager.saveState();
							Messaging.log("Saved.");
						}
					}, SettingsManager.getInt("SavingDelay"),
					SettingsManager.getInt("SavingDelay"));
		}
		// Call each NPC type's onEnable method
		for (String loaded : loadedTypes) {
			CitizensNPC type = CitizensNPCManager.getType(loaded);
			type.onEnable();
			// Register event listener per-type
			type.addListeners();
			for (Listener listener : CitizensNPCManager.getListeners()) {
				listener.registerEvents();
			}
		}

		Messaging.log("version [" + getVersion() + "] loaded.");
	}

	@Override
	public void onDisable() {
		// Save the local copy of our files to disk.
		PropertyManager.saveState();
		NPCManager.despawnAll();
		CreatureTask.despawnAll();

		Messaging.log("version [" + getVersion() + "] disabled.");
	}

	@Override
	public void onLoad() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;
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
				if (!modifier.isEmpty()) {
					boolean value = handleMistake(sender, split[0], modifier);
					return value;
				}
			}

			HumanNPC npc = null;
			if (player != null && NPCManager.validateSelected(player)) {
				npc = NPCManager.get(NPCDataManager.getSelected(player));
			}
			try {
				commands.execute(split, player, player == null ? sender
						: player, npc);
			} catch (ServerCommandException e) {
				sender.sendMessage(e.getMessage());
			} catch (CommandPermissionsException e) {
				Messaging.sendError(sender, MessageUtils.noPermissionsMessage);
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
			excp.printStackTrace();
			Messaging.sendError(player,
					"Please report this error: [See console]");
			Messaging.sendError(player,
					excp.getClass().getName() + ": " + excp.getMessage());
		}
		return true;
	}

	/**
	 * Get the current version of Citizens
	 * 
	 * @return
	 */
	public static String getVersion() {
		return version;
	}

	private boolean handleMistake(CommandSender sender, String command,
			String modifier) {
		String[] modifiers = commands.getAllCommandModifiers(command);
		Map<Integer, String> values = new TreeMap<Integer, String>();
		int i = 0;
		for (String string : modifiers) {
			values.put(StringUtils.getLevenshteinDistance(modifier, string),
					modifiers[i]);
			++i;
		}
		int best = 0;
		boolean stop = false;
		Set<String> possible = new HashSet<String>();
		for (Entry<Integer, String> entry : values.entrySet()) {
			if (!stop) {
				best = entry.getKey();
				stop = true;
			} else if (entry.getKey() > best) {
				break;
			}
			possible.add(entry.getValue());
		}
		if (possible.size() > 0) {
			sender.sendMessage(ChatColor.GRAY
					+ "Unknown command. Did you mean:");
			for (String string : possible) {
				sender.sendMessage(StringUtils.wrap("    /") + command + " "
						+ StringUtils.wrap(string));
			}
			return true;
		}
		return false;
	}

	private void setupNPCs() {
		PropertyManager.getNPCProfiles().load();
		StringBuilder UIDList = new StringBuilder();
		List<Integer> sorted = PropertyManager.getNPCProfiles().getIntegerKeys(
				null);
		Collections.sort(sorted);
		int max = sorted.size() == 0 ? 0 : sorted.get(sorted.size() - 1), count = 0;
		while (count <= max) {
			if (PropertyManager.getNPCProfiles().pathExists(count)) {
				UIDList.append(count + ",");
			}
			++count;
		}
		String[] values = UIDList.toString().split(",");
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
						NPCDataManager.setText(UID, text);
					}
				}
			}
		}
		Messaging.log("Loaded " + NPCManager.GlobalUIDs.size() + " NPCs.");
		initialized = true;
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
			return true;
		}
		return false;
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
	public static boolean validateTool(String key, int type, boolean sneaking) {
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
		return true;
	}

	/**
	 * Load NPC types in the plugins/Citizens/types directory
	 */
	public void loadNPCTypes() {
		File dir = new File(getDataFolder(), "types");
		dir.mkdir();
		for (String f : dir.list()) {
			if (f.contains(".jar")) {
				CitizensNPC type = CitizensNPCLoader.loadNPCType(new File(dir,
						f), this);
				if (type != null) {
					loadedTypes.add(type.getType());
				}
			}
		}
		Messaging.log("NPC types loaded: " + Joiner.on(", ").join(loadedTypes));
	}
}