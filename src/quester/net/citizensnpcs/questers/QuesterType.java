package net.citizensnpcs.questers;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.questers.data.QuestProperties;
import net.citizensnpcs.questers.data.QuesterProperties;
import net.citizensnpcs.questers.listeners.QuesterBlockListen;
import net.citizensnpcs.questers.listeners.QuesterCitizensListen;
import net.citizensnpcs.questers.listeners.QuesterEntityListen;
import net.citizensnpcs.questers.listeners.QuesterPlayerListen;

public class QuesterType extends CitizensNPCType {

    @Override
    public CommandHandler getCommands() {
        return QuesterCommands.INSTANCE;
    }

    @Override
    public CitizensNPC getInstance() {
        return new Quester();
    }

    @Override
    public String getName() {
        return "quester";
    }

    @Override
    public Properties getProperties() {
        return QuesterProperties.INSTANCE;
    }

    @Override
    public void registerEvents() {
        QuestProperties.initialize();
        // custom events
        NPCTypeManager.registerEvents(new QuesterCitizensListen());
        NPCTypeManager.registerEvents(new QuesterEntityListen());
        // block events
        QuesterBlockListen bl = new QuesterBlockListen();
        NPCTypeManager.registerEvents(bl);
        // player events
        QuesterPlayerListen pl = new QuesterPlayerListen();
        NPCTypeManager.registerEvents(pl);
    }
}