package net.citizensnpcs.alchemists;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.Settings.SettingsType;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.Setting;

public class AlchemistType extends CitizensNPCType {

	@Override
	public CommandHandler getCommands() {
		return AlchemistCommands.INSTANCE;
	}

	@Override
	public String getName() {
		return "alchemist";
	}

	@Override
	public List<Setting> getSettings() {
		List<Setting> nodes = new ArrayList<Setting>();
		nodes.add(new Setting("AlchemistFailedCraftChance",
				SettingsType.GENERAL, "alchemists.failed-craft-chance", 10));
		nodes.add(new Setting("AlchemistFailedCraftItem", SettingsType.GENERAL,
				"alchemists.failed-craft-item", 263));
		return nodes;
	}

	@Override
	public CitizensNPC newInstance() {
		return new Alchemist();
	}

}