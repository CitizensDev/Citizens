package net.citizensnpcs.wizards;

import net.citizensnpcs.properties.DataKey;

import org.bukkit.entity.Player;

public interface WizardOperation {
	String apply(Player player, Wizard wizard);

	WizardOperation cycle();

	String getCycleMessage();

	int getManaCost();

	String getPriceNode();

	void save(DataKey root);
}
