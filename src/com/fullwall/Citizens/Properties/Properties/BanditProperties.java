package com.fullwall.Citizens.Properties.Properties;

import java.util.ArrayList;

import net.minecraft.server.InventoryPlayer;

import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BanditProperties extends Saveable {
	private final PropertyHandler bandits = new PropertyHandler(
			"plugins/Citizens/Bandits/bandits.citizens");
	private final PropertyHandler inventories = new PropertyHandler(
			"plugins/Citizens/Bandits/inventories.citizens");

	@Override
	public void saveFiles() {
		bandits.save();
		inventories.save();
	}

	private void saveInventory(int UID, PlayerInventory inv) {
		String save = "";
		for (ItemStack i : inv.getContents()) {
			if (i == null) {
				save += 0 + "/" + 1 + "/" + 0 + ",";
			} else {
				save += i.getTypeId() + "/" + i.getAmount() + "/"
						+ ((i.getData() == null) ? 0 : i.getData().getData())
						+ ",";
			}
		}
		inventories.setString(UID, save);
	}

	private PlayerInventory getInventory(int UID) {
		String save = inventories.getString(UID);
		if (save.isEmpty())
			return null;
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		for (String s : save.split(",")) {
			String[] split = s.split("/");
			if (!split[0].equals("0")) {
				array.add(new ItemStack(parse(split[0]), parse(split[1]),
						(short) 0, (byte) parse(split[2])));
			} else {
				array.add(null);
			}
		}
		PlayerInventory inv = new CraftInventoryPlayer(
				new InventoryPlayer(null));
		ItemStack[] stacks = inv.getContents();
		inv.setContents(array.toArray(stacks));
		return inv;
	}

	private int parse(String passed) {
		return Integer.parseInt(passed);
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isBandit());
		}
		saveInventory(npc.getUID(), npc.getPlayer().getInventory());
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setBandit(getEnabled(npc));
		if (getInventory(npc.getUID()) != null) {
			npc.getInventory().setContents(
					getInventory(npc.getUID()).getContents());
		}
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		bandits.removeKey(npc.getUID());
		inventories.removeKey(npc.getUID());
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
		if (inventories.keyExists(UID)) {
			inventories.setString(nextUID, inventories.getString(UID));
		}
	}
}