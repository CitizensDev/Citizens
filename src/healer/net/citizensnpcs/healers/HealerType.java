package net.citizensnpcs.healers;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.Settings.SettingsType;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.healers.listeners.HealerCitizensListen;
import net.citizensnpcs.healers.listeners.HealerNPCListen;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Setting;

import org.bukkit.Bukkit;
import org.bukkit.event.Event.Type;

public class HealerType extends CitizensNPCType {
	@Override
	public CommandHandler getCommands() {
		return HealerCommands.INSTANCE;
	}

	@Override
	public String getName() {
		return "healer";
	}

	@Override
	public List<Setting> getSettings() {
		List<Setting> nodes = new ArrayList<Setting>();
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.healer.levelup", 100));
		nodes.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.healer.heal", 100));
		nodes.add(new Setting("HealerGiveHealthItem", SettingsType.GENERAL,
				"healers.give-health-item", 35));
		nodes.add(new Setting("HealerTakeHealthItem", SettingsType.GENERAL,
				"healers.take-health-item", 276));
		nodes.add(new Setting("HealerHealthRegenIncrement",
				SettingsType.GENERAL, "healers.health-regen-increment", 12000));
		nodes.add(new Setting("RegenHealerHealth", SettingsType.GENERAL,
				"healers.regen-health", true));
		return nodes;
	}

	@Override
	public CitizensNPC newInstance() {
		return new Healer();
	}

	@Override
	public void onEnable() {
		if (!Settings.getBoolean("RegenHealerHealth")) {
			return;
		}
		for (HumanNPC entry : CitizensManager.getNPCs()) {
			if (!entry.isType("healer"))
				continue;
			HealerTask task = new HealerTask(entry);
			int delay = ((Healer) entry.getType("healer")).getHealthRegenRate();
			task.addID(Bukkit
					.getServer()
					.getScheduler()
					.scheduleSyncRepeatingTask(Citizens.plugin, task, delay,
							delay));
		}
		NPCTypeManager.registerEvent(Type.CUSTOM_EVENT,
				new HealerCitizensListen());
		NPCTypeManager.registerEvent(Type.CUSTOM_EVENT, new HealerNPCListen());
	}
}