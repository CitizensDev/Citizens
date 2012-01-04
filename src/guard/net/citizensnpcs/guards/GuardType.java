package net.citizensnpcs.guards;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings.SettingsType;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.Setting;

import org.bukkit.Bukkit;

public class GuardType extends CitizensNPCType {
	@Override
	public CommandHandler getCommands() {
		return GuardCommands.INSTANCE;
	}

	@Override
	public String getName() {
		return "guard";
	}

	@Override
	public List<Setting> getSettings() {
		List<Setting> nodes = new ArrayList<Setting>();
		nodes.add(new Setting("MaxStationaryReturnTicks", SettingsType.GENERAL,
				"guards.max.stationary-return-ticks", 25));
		nodes.add(new Setting("GuardRespawnDelay", SettingsType.GENERAL,
				"guards.respawn-delay", 100));
		nodes.add(new Setting("DefaultBouncerProtectionRadius",
				SettingsType.GENERAL,
				"guards.bouncers.default.protection-radius", 10));
		nodes.add(new Setting("SoldierSelectTool", SettingsType.GENERAL,
				"guards.soldiers.items.select", "*"));
		nodes.add(new Setting("SoldierReturnTool", SettingsType.GENERAL,
				"guards.soldiers.items.return", "288,"));
		nodes.add(new Setting("SoldierDeselectAllTool", SettingsType.GENERAL,
				"guards.soldiers.items.deselect-all", "352,"));
		return nodes;
	}

	@Override
	public CitizensNPC newInstance() {
		return new Guard();
	}

	@Override
	public void onEnable() {
		Bukkit.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(Citizens.plugin, new GuardTask(), 0,
						1);
	}
}