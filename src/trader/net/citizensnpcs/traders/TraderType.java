package net.citizensnpcs.traders;

import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CitizensNPCType;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.api.Properties;

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
	public CitizensNPC getInstance() {
		return new Trader();
	}
}