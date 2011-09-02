package net.citizensnpcs.blacksmiths;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.Properties;

public class BlacksmithType extends CitizensNPCType {

	@Override
	public String getName() {
		return "blacksmith";
	}

	@Override
	public Properties getProperties() {
		return BlacksmithProperties.INSTANCE;
	}

	@Override
	public CommandHandler getCommands() {
		return BlacksmithCommands.INSTANCE;
	}

	@Override
	public CitizensNPC getInstance() {
		return new Blacksmith();
	}
}