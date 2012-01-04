package net.citizensnpcs.blacksmiths;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.Settings.SettingsType;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.Setting;

public class BlacksmithType extends CitizensNPCType {

	@Override
	public CommandHandler getCommands() {
		return BlacksmithCommands.INSTANCE;
	}

	@Override
	public String getName() {
		return "blacksmith";
	}

	@Override
	public List<Setting> getSettings() {
		List<Setting> nodes = new ArrayList<Setting>();
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.leather", 0.25));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.gold", 0.50));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.chainmail", 0.75));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.iron", 1));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.armorrepair.diamond", 1.25));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.wood", 0.25));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.gold", 0.50));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.stone", 0.75));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.iron", 1));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.diamond", 1.25));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.blacksmith.toolrepair.misc", 0.50));
		return nodes;
	}

	@Override
	public CitizensNPC newInstance() {
		return new Blacksmith();
	}
}