package net.citizensnpcs.alchemists;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.Properties;

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