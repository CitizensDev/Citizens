package net.citizensnpcs.wizards;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.wizards.listeners.WizardCitizensListen;
import net.citizensnpcs.wizards.listeners.WizardNPCListen;

public class WizardType extends CitizensNPCType {

    @Override
    public CommandHandler getCommands() {
        return WizardCommands.INSTANCE;
    }

    @Override
    public CitizensNPC getInstance() {
        return new Wizard();
    }

    @Override
    public String getName() {
        return "wizard";
    }

    @Override
    public Properties getProperties() {
        return WizardProperties.INSTANCE;
    }

    @Override
    public void registerEvents() {
        NPCTypeManager.registerEvents(new WizardCitizensListen());
        NPCTypeManager.registerEvents(new WizardNPCListen());
    }
}