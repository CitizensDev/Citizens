package com.fullwall.Citizens.Properties.Properties;

import org.bukkit.entity.CreatureType;

import com.fullwall.Citizens.NPCTypes.Wizards.WizardManager.WizardMode;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class WizardProperties extends Saveable {
	private final PropertyHandler wizards = new PropertyHandler(
			"plugins/Citizens/Wizards/Citizens.wizards");
	private final PropertyHandler locations = new PropertyHandler(
			"plugins/Citizens/Wizards/Citizens.locations");
	private final PropertyHandler mana = new PropertyHandler(
			"plugins/Citizens/Wizards/mana.citizens");
	private final PropertyHandler mode = new PropertyHandler(
			"plugins/Citizens/Wizards/mode.citizens");
	private final PropertyHandler time = new PropertyHandler(
			"plugins/Citizens/Wizards/time.citizens");
	private final PropertyHandler mob = new PropertyHandler(
			"plugins/Citizens/Wizards/mob.citizens");

	private void saveLocations(int UID, String locationString) {
		locations.setString(UID, locationString.replace(")(", "):("));
	}

	private String getLocations(int UID) {
		return locations.getString(UID).replace(")(", "):(");
	}

	private void saveMana(int UID, int manaLevel) {
		mana.setInt(UID, manaLevel);
	}

	private int getMana(int UID) {
		return mana.getInt(UID);
	}

	private void saveMode(int UID, WizardMode wizardMode) {
		mode.setString(UID, wizardMode.toString());
	}

	private WizardMode getMode(int UID) {
		return WizardMode.parse(mode.getString(UID));
	}

	private void saveTime(int UID, String worldTime) {
		time.setString(UID, worldTime);
	}

	private String getTime(int UID) {
		return time.getString(UID);
	}

	private void saveMob(int UID, CreatureType type) {
		mob.setString(UID, type.getName());
	}

	private CreatureType getMob(int UID) {
		if (CreatureType.fromName(mob.getString(UID)) != null) {
			return CreatureType.fromName(mob.getString(UID));
		}
		return CreatureType.CREEPER;
	}

	@Override
	public void saveFiles() {
		wizards.save();
		locations.save();
		mana.save();
		mode.save();
		time.save();
		mob.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isWizard());
			saveLocations(npc.getUID(), npc.getWizard().getLocations());
			saveMana(npc.getUID(), npc.getWizard().getMana());
			saveMode(npc.getUID(), npc.getWizard().getMode());
			saveTime(npc.getUID(), npc.getWizard().getTime());
			saveMob(npc.getUID(), npc.getWizard().getMob());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setWizard(getEnabled(npc));
		npc.getWizard().setLocations(getLocations(npc.getUID()));
		npc.getWizard().setMana(getMana(npc.getUID()));
		npc.getWizard().setMode(getMode(npc.getUID()));
		npc.getWizard().setTime(getTime(npc.getUID()));
		npc.getWizard().setMob(getMob(npc.getUID()));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		wizards.removeKey(npc.getUID());
		locations.removeKey(npc.getUID());
		mana.removeKey(npc.getUID());
		mode.removeKey(npc.getUID());
		time.removeKey(npc.getUID());
		mob.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		wizards.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return wizards.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return wizards.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.WIZARD;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (wizards.keyExists(UID)) {
			wizards.setString(nextUID, wizards.getString(UID));
		}
		if (locations.keyExists(UID)) {
			locations.setString(nextUID, locations.getString(UID));
		}
		if (mana.keyExists(UID)) {
			mana.setString(nextUID, mana.getString(UID));
		}
		if (mode.keyExists(UID)) {
			mode.setString(nextUID, mode.getString(UID));
		}
		if (time.keyExists(UID)) {
			time.setString(nextUID, time.getString(UID));
		}
		if (mob.keyExists(UID)) {
			mob.setString(nextUID, mob.getString(UID));
		}
	}
}