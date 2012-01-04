package net.citizensnpcs.wizards;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.Settings.SettingsType;
import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Setting;
import net.citizensnpcs.wizards.listeners.WizardCitizensListen;
import net.citizensnpcs.wizards.listeners.WizardNPCListen;

import org.bukkit.Bukkit;
import org.bukkit.event.Event.Type;

public class WizardType extends CitizensNPCType {
	@Override
	public CommandHandler getCommands() {
		return WizardCommands.INSTANCE;
	}

	@Override
	public String getName() {
		return "wizard";
	}

	@Override
	public List<Setting> getSettings() {
		List<Setting> settings = new ArrayList<Setting>();
		settings.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.wizard.teleport", 100));
		settings.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.wizard.changetime", 100));
		settings.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.wizard.spawnmob", 100));
		settings.add(new Setting("", SettingsType.GENERAL,
				"economy.prices.wizard.togglestorm", 100));
		settings.add(new Setting("TeleportManaCost", SettingsType.GENERAL,
				"wizards.mana-costs.teleport", 5));
		settings.add(new Setting("ChangeTimeManaCost", SettingsType.GENERAL,
				"wizards.mana-costs.changetime", 5));
		settings.add(new Setting("SpawnMobManaCost", SettingsType.GENERAL,
				"wizards.mana-costs.spawnmob", 5));
		settings.add(new Setting("ToggleStormManaCost", SettingsType.GENERAL,
				"wizards.mana-costs.togglestorm", 5));
		settings.add(new Setting("WizardMaxLocations", SettingsType.GENERAL,
				"wizards.max-locations", 10));
		settings.add(new Setting("WizardMaxMana", SettingsType.GENERAL,
				"wizards.max-mana", 100));
		settings.add(new Setting("WizardInteractItem", SettingsType.GENERAL,
				"wizards.interact-item", 288));
		settings.add(new Setting("WizardManaRegenItem", SettingsType.GENERAL,
				"wizards.mana-regen-item", 348));
		settings.add(new Setting("WizardManaRegenRate", SettingsType.GENERAL,
				"wizards.mana-regen-rate", 6000));
		settings.add(new Setting("RegenWizardMana", SettingsType.GENERAL,
				"wizards.regen-mana", true));
		return settings;
	}

	@Override
	public CitizensNPC newInstance() {
		return new Wizard();
	}

	@Override
	public void onEnable() {
		if (!Settings.getBoolean("RegenWizardMana")) {
			return;
		}
		for (HumanNPC entry : CitizensManager.getNPCs()) {
			if (!entry.isType("wizard"))
				continue;
			WizardTask task = new WizardTask(entry);
			task.addID(Bukkit
					.getServer()
					.getScheduler()
					.scheduleSyncRepeatingTask(Citizens.plugin, task,
							Settings.getInt("WizardManaRegenRate"),
							Settings.getInt("WizardManaRegenRate")));
		}
		NPCTypeManager.registerEvent(Type.CUSTOM_EVENT,
				new WizardCitizensListen());
		NPCTypeManager.registerEvent(Type.CUSTOM_EVENT, new WizardNPCListen());
	}
}