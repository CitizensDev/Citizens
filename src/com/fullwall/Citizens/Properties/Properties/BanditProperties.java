package com.fullwall.Citizens.Properties.Properties;

import java.util.ArrayList;
import java.util.List;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BanditProperties extends Saveable {
	private final PropertyHandler bandits = new PropertyHandler(
			"plugins/Citizens/Bandits/bandits.citizens");
	private final PropertyHandler stealables = new PropertyHandler(
			"plugins/Citizens/Bandits/stealables.citizens");

	@Override
	public void saveFiles() {
		bandits.save();
		stealables.save();
	}

	private void saveStealables(int UID, List<Integer> steal) {
		String save = "";
		for (int x = 0; x < steal.size(); x++) {
			save += steal.get(x) + ",";
		}
		stealables.setString(UID, save);
	}

	private List<Integer> getStealables(int UID) {
		String save = stealables.getString(UID);
		List<Integer> items = new ArrayList<Integer>();
		for (String s : save.split(",")) {
			items.add(Integer.parseInt(s));
		}
		return items;
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isBandit());
			saveStealables(npc.getUID(), npc.getBandit().getStealables());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setBandit(getEnabled(npc));
		npc.getBandit().setStealables(getStealables(npc.getUID()));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		bandits.removeKey(npc.getUID());
		stealables.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		bandits.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return bandits.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return bandits.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.BANDIT;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (bandits.keyExists(UID)) {
			bandits.setString(nextUID, bandits.getString(UID));
		}
		if (stealables.keyExists(UID)) {
			stealables.setString(nextUID, stealables.getString(UID));
		}
	}
}