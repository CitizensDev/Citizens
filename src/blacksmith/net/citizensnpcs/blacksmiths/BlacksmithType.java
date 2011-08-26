package net.citizensnpcs.blacksmiths;

import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CitizensNPCType;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.api.Properties;

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