package net.citizensnpcs.traders;

import java.util.List;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.Setting;

public class TraderType extends CitizensNPCType {
	@Override
	public CommandHandler getCommands() {
		return TraderCommands.INSTANCE;
	}

	@Override
	public String getName() {
		return "trader";
	}

	@Override
	public List<Setting> getSettings() {
		return null;
	}

	@Override
	public CitizensNPC newInstance() {
		return new Trader();
	}

	@Override
	public void onEnable() {
		Trader.loadGlobal();
	}
}