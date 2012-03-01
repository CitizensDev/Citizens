package net.citizensnpcs.traders;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Properties;

import org.bukkit.Bukkit;

public class TraderType extends CitizensNPCType {
    @Override
    public Properties getProperties() {
        return TraderProperties.INSTANCE;
    }

    @Override
    public CommandHandler getCommands() {
        return TraderCommands.INSTANCE;
    }

    @Override
    public String getName() {
        return "trader";
    }

    @Override
    public void registerEvents() {
        NPCTypeManager.registerEvents(new CitizensListen());
        if (Bukkit.getPluginManager().getPlugin("Spout") != null) {
            NPCTypeManager.registerEvents(new SpoutListen());
            TraderTask.setUseSpout(true);
        } else {
            Citizens.plugin.getLogger().warning("Spout is not enabled! Traders are susceptible to item duping!");
        }
    }

    @Override
    public CitizensNPC getInstance() {
        return new Trader();
    }
}