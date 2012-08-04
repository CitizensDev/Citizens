package net.citizensnpcs.healers;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.healers.listeners.HealerCitizensListen;
import net.citizensnpcs.healers.listeners.HealerNPCListen;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Properties;

public class HealerType extends CitizensNPCType {

    @Override
    public CommandHandler getCommands() {
        return HealerCommands.INSTANCE;
    }

    @Override
    public CitizensNPC getInstance() {
        return new Healer();
    }

    @Override
    public String getName() {
        return "healer";
    }

    @Override
    public Properties getProperties() {
        return HealerProperties.INSTANCE;
    }

    @Override
    public void registerEvents() {
        NPCTypeManager.registerEvents(new HealerCitizensListen());
        NPCTypeManager.registerEvents(new HealerNPCListen());
    }
}