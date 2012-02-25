package net.citizensnpcs;

import com.google.common.base.Joiner;
import net.citizensnpcs.api.event.CitizensDisableEvent;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.CitizensEnableTypeEvent;
import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.api.event.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.commands.BasicCommands;
import net.citizensnpcs.commands.ToggleCommands;
import net.citizensnpcs.commands.WaypointCommands;
import net.citizensnpcs.listeners.EntityListen;
import net.citizensnpcs.listeners.PlayerListen;
import net.citizensnpcs.listeners.WorldListen;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.npctypes.CitizensNPCLoader;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPCType;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.resources.sk89q.*;
import net.citizensnpcs.utils.*;
import net.citizensnpcs.utils.Metrics.Plotter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Citizens - NPCs for Bukkit
 */
public class Citizens extends JavaPlugin {
    public static Citizens plugin;
    public static CitizensCommandsManager<Player> commands = new CitizensCommandsManager<Player>();

    public static Permission permission = null;
    public static net.milkbowl.vault.economy.Economy economy = null;
    public static Chat chat = null;

    public static boolean initialized = false;

    public static List<String> loadedTypes = new ArrayList<String>();

    @Override
    public void onEnable() {
        plugin = this;

        // load NPC types (must be loaded before settings)
        loadNPCTypes();

        // load settings
        Settings.setupVariables();

        // initialise metrics
        new Thread() {
            @Override
            public void run() {
                try {
                    Metrics metrics = new Metrics();
                    metrics.addCustomData(plugin, new Plotter() {
                        @Override
                        public String getColumnName() {
                            return "Total NPCs";
                        }

                        @Override
                        public int getValue() {
                            return NPCManager.getList().size();
                        }
                    });
                    metrics.beginMeasuringPlugin(plugin);
                } catch (IOException e) {
                    Messaging.log("unable to load metrics");
                }
            }
        }.start();

        // initialize error reporting
        Web.initErrorReporting();

        // register events per type
        for (String loaded : loadedTypes) {
            NPCTypeManager.getType(loaded).registerEvents();
        }

        // register our events
        getServer().getPluginManager().registerEvents(new EntityListen(), this);
        getServer().getPluginManager().registerEvents(new WorldListen(), this);
        getServer().getPluginManager().registerEvents(new PlayerListen(), this);

        // register our commands
        commands.register(BasicCommands.class);
        commands.register(ToggleCommands.class);
        commands.register(WaypointCommands.class);

        // initialize permissions system
        new PermissionManager(getServer().getPluginManager());

        // schedule Creature tasks
        if (CreatureNPCType.hasSpawning()) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new CreatureTask(),
                    Settings.getInt("CreatureNPCSpawnDelay"), Settings.getInt("CreatureNPCSpawnDelay"));
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new CreatureTask.CreatureTick(), 0, 1);
        }
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TickTask(), 0, 1);

        if (Settings.getBoolean("UseSaveTask")) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    Messaging.log("Saving npc files to disk...");
                    PropertyManager.saveState();
                    Messaging.log("Saved.");
                }
            }, Settings.getInt("SavingDelay"), Settings.getInt("SavingDelay"));
        }

        Messaging.log("version [" + localVersion() + "] loaded.");
        // reinitialize existing NPCs
        // Scheduled tasks run once all plugins are loaded (multiworld support)
        if (getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                setupNPCs();
                // call enable event, can be used for initialization of
                // type-specific things
                // TODO remove when types are made into plugins
                Bukkit.getServer().getPluginManager().callEvent(new CitizensEnableEvent());
            }
        }) == -1) {
            Messaging.log("Issue with multiworld scheduling, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // initialize Permissions and Economy
        Economy.init();
        new PermissionManager(Bukkit.getServer().getPluginManager());
    }

    public static String localVersion() {
        return Version.VERSION;
    }

    @Override
    public void onDisable() {
        // save the local copy of our files to disk
        Web.disableErrorReporting();
        PropertyManager.saveState();
        NPCManager.despawnAll(NPCRemoveReason.UNLOAD);
        CreatureTask.despawnAll(NPCRemoveReason.UNLOAD);

        // call disable event
        // TODO remove when types are made into plugins
        Bukkit.getServer().getPluginManager().callEvent(new CitizensDisableEvent());

        Messaging.log("version [" + localVersion() + "] disabled.");
    }

    @Override
    public void onLoad() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        try {
            // must put command into split.
            String[] split = new String[args.length + 1];
            System.arraycopy(args, 0, split, 1, args.length);
            split[0] = command.getName().toLowerCase();

            String modifier = "";
            if (args.length > 0) {
                modifier = args[0];
            }

            // No command found!
            if (!commands.hasCommand(split[0], modifier)) {
                if (!modifier.isEmpty()) {
                    boolean value = handleMistake(sender, split[0], modifier);
                    return value;
                }
            }

            HumanNPC npc = null;
            if (player != null && NPCManager.hasSelected(player)) {
                npc = NPCManager.get(NPCDataManager.getSelected(player));
            }
            try {
                commands.execute(split, player, player == null ? sender : player, npc);
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
            Messaging.sendError(player, "Please report this error: [See console]");
            Messaging.sendError(player, excp.getClass().getName() + ": " + excp.getMessage());
        }
        return true;
    }

    // TODO: clean this up a little bit.
    private boolean handleMistake(CommandSender sender, String command, String modifier) {
        String[] modifiers = commands.getAllCommandModifiers(command);
        Map<Integer, String> values = new TreeMap<Integer, String>();
        int i = 0;
        for (String string : modifiers) {
            values.put(StringUtils.getLevenshteinDistance(modifier, string), modifiers[i]);
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
            sender.sendMessage(ChatColor.GRAY + "Unknown command. Did you mean:");
            for (String string : possible) {
                sender.sendMessage(StringUtils.wrap("    /") + command + " " + StringUtils.wrap(string));
            }
            return true;
        }
        return false;
    }

    private void setupNPCs() {
        PropertyManager.getNPCProfiles().load();
        List<Integer> sorted = PropertyManager.getNPCProfiles().getIntegerKeys(null);
        Collections.sort(sorted);
        int max = sorted.size() == 0 ? 0 : sorted.get(sorted.size() - 1), count = 0;
        while (count <= max) {
            if (!PropertyManager.getNPCProfiles().keyExists("" + count)) {
                ++count;
                continue;
            }
            int UID = count;
            Location loc = PropertyManager.getBasic().getLocation(UID);
            if (loc != null && loc.getWorld() != null) {
                NPCManager.register(UID, PropertyManager.getBasic().getOwner(UID), NPCCreateReason.SPAWN);
                Deque<String> text = PropertyManager.getBasic().getText(UID);
                if (text != null) {
                    NPCDataManager.setText(UID, text);
                }
            }
            ++count;
        }
        Messaging.log("Loaded " + NPCManager.GlobalUIDs.size() + " NPCs.");
        initialized = true;
    }

    // load NPC types in the plugins/Citizens/types directory
    private void loadNPCTypes() {
        File dir = new File(getDataFolder(), "types");
        dir.mkdirs();
        for (String f : dir.list()) {
            if (f.contains(".jar")) {
                CitizensNPCType type = CitizensNPCLoader.loadNPCType(new File(dir, f), this);
                if (type != null) {
                    loadedTypes.add(type.getName());
                    Bukkit.getPluginManager().callEvent(new CitizensEnableTypeEvent(type));
                }
            }
        }
        if (loadedTypes.size() > 0) {
            Messaging.log("NPC types loaded: " + Joiner.on(", ").join(loadedTypes));
        } else {
            Messaging.log("No NPC types loaded.");
        }
    }

    public static Permission setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
            Messaging.log("Found Permissions Plugin: " + permission.getName());
        } else {
            Messaging.log("No Permissions Plugin found!");
        }
        return permission;
    }

    public static Chat setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
            Messaging.log("Found Chat Plugin: " + chat.getName());
        } else {
            Messaging.log("No Chat Plugin found!");
        }
        return chat;
    }

    public static net.milkbowl.vault.economy.Economy setupEconomy()
    {
        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            Messaging.log("Found Economy Plugin: " + economy.getName());
        } else {
            Messaging.log("No Economy plugin found!");
        }
        return economy;
    }
}