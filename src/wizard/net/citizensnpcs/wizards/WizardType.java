package net.citizensnpcs.wizards;

import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CitizensNPCType;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.api.Properties;
import net.citizensnpcs.wizards.listeners.WizardCitizensListen;

import org.bukkit.event.Event.Type;

public class WizardType extends CitizensNPCType {

	@Override
	public Properties getProperties() {
		return WizardProperties.INSTANCE;
	}

	@Override
	public CommandHandler getCommands() {
		return WizardCommands.INSTANCE;
	}

	@Override
	public void registerEvents() {
		CitizensManager.registerEvent(Type.CUSTOM_EVENT,
				new WizardCitizensListen());
	}

	@Override
	public String getName() {
		return "wizard";
	}

	@Override
	public CitizensNPC getInstance() {
		return new Wizard();
	}
}
