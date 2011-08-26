package net.citizensnpcs.alchemists;

import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CitizensNPCType;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.api.Properties;

public class AlchemistType extends CitizensNPCType {

	@Override
	public String getName() {
		return "alchemist";
	}

	@Override
	public Properties getProperties() {
		return AlchemistProperties.INSTANCE;
	}

	@Override
	public CommandHandler getCommands() {
		return AlchemistCommands.INSTANCE;
	}

	@Override
	public CitizensNPC getInstance() {
		return new Alchemist();
	}
}
