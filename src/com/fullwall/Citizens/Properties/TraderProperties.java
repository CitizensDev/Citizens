package com.fullwall.Citizens.Properties;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.InventoryPlayer;

import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.Citizens.Traders.Check;
import com.fullwall.Citizens.Traders.ItemPrice;
import com.fullwall.Citizens.Traders.Stockable;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderProperties extends Saveable {
	public PropertyHandler traders;
	public PropertyHandler inventories;
	public PropertyHandler stocking;
	public PropertyHandler unlimiteds;

	public TraderProperties() {
		traders = new PropertyHandler(
				"plugins/Citizens/Traders/Citizens.traders");
		inventories = new PropertyHandler(
				"plugins/Citizens/Traders/Citizens.inventories");
		stocking = new PropertyHandler(
				"plugins/Citizens/Traders/Citizens.stocking");
		unlimiteds = new PropertyHandler(
				"plugins/Citizens/Traders/Citizens.unlimited");
	}

	public void saveTrader(int UID, boolean state) {
		traders.setBoolean(UID, state);
	}

	public boolean isTrader(int UID) {
		return traders.keyExists(UID);
	}

	public boolean getTraderState(int UID) {
		return traders.getBoolean(UID);
	}

	public void removeTrader(int UID) {
		traders.removeKey(UID);
	}

	public void saveInventory(int UID, PlayerInventory inv) {
		String save = "";
		for (ItemStack i : inv.getContents()) {
			if (i == null)
				save += 0 + "/" + 1 + "/" + 0 + ",";
			else
				save += i.getTypeId() + "/" + i.getAmount() + "/"
						+ ((i.getData() == null) ? 0 : i.getData().getData())
						+ ",";
		}
		inventories.setString(UID, save);
	}

	public PlayerInventory getInventory(int UID) {
		String save = inventories.getString(UID);
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		for (String s : save.split(",")) {
			String[] split = s.split("/");
			if (!split[0].equals("0"))
				array.add(new ItemStack(parse(split[0]), parse(split[1]),
						(short) 0, (byte) parse(split[2])));
			else
				array.add(null);
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

	public void saveUnlimited(int UID, boolean unlimited) {
		unlimiteds.setBoolean(UID, unlimited);
	}

	public boolean getUnlimited(int UID) {
		return unlimiteds.getBoolean(UID);
	}

	public String addToStockableString(int UID, String s, Stockable st) {
		return s += st.toString() + ";";
	}

	public void setStockables(int UID, String set) {
		stocking.setString(UID, set);
	}

	public void removeStockable(int UID, Stockable s) {
		String write = "";
		write += s.toString() + ";";
		String read = stocking.getString(UID);
		read.replace(write, "");
		stocking.setString(UID, read);
	}

	public void saveStockables(int UID,
			ConcurrentHashMap<Check, Stockable> stockables) {
		String string = "";
		for (Entry<Check, Stockable> entry : stockables.entrySet()) {
			string += addToStockableString(UID, string, entry.getValue());
			setStockables(UID, string);
		}
	}

	public ConcurrentHashMap<Check, Stockable> getStockables(int UID) {
		ConcurrentHashMap<Check, Stockable> stockables = new ConcurrentHashMap<Check, Stockable>();
		int i = 0;
		for (String s : stocking.getString(UID).split(";")) {
			if (s.isEmpty())
				continue;
			i = 0;
			ItemStack stack = new ItemStack(37);
			ItemPrice price = new ItemPrice(0);
			boolean selling = false;
			for (String main : s.split(",")) {
				switch (i) {
				case 0:
					String[] split = main.split("/");
					MaterialData data = new MaterialData(
							Integer.parseInt(split[2]));
					stack = new ItemStack(Integer.parseInt(split[0]),
							Integer.parseInt(split[1]));
					if (data != null)
						stack.setData(data);
					break;
				case 1:
					String[] parts = main.split("/");
					if (parts.length == 2) {
						price = new ItemPrice(Double.parseDouble(parts[0]));
						price.setiConomy(Boolean.parseBoolean(parts[1]));
					} else {
						price = new ItemPrice(Integer.parseInt(parts[0]),
								Integer.parseInt(parts[1]),
								Integer.parseInt(parts[2]));
						price.setiConomy(Boolean.parseBoolean(parts[3]));
					}
					break;
				case 2:
					selling = Boolean.parseBoolean(main);
					break;
				}
				i += 1;
			}
			Stockable stock = new Stockable(stack, price, selling);
			stockables.put(stock.createCheck(), stock);
		}
		return stockables;
	}

	/**
	 * Copies all data from one ID to another.
	 * 
	 * @param UID
	 * @param nextUID
	 */
	@Override
	public void copy(int UID, int nextUID) {
		if (traders.keyExists(UID))
			traders.setString(nextUID, traders.getString(UID));
		if (inventories.keyExists(UID))
			inventories.setString(nextUID, inventories.getString(UID));
		if (stocking.keyExists(UID))
			stocking.setString(nextUID, stocking.getString(UID));
		if (unlimiteds.keyExists(UID))
			unlimiteds.setString(nextUID, unlimiteds.getString(UID));
		saveFiles();
	}

	@Override
	public void saveFiles() {
		traders.save();
		inventories.save();
		stocking.save();
		unlimiteds.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc))
			set(npc, npc.isTrader());
		saveInventory(npc.getUID(), npc.getPlayer().getInventory());
		PropertyPool.saveBalance(npc.getUID(), npc.getBalance());
		saveUnlimited(npc.getUID(), npc.getTrader().isUnlimited());
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		traders.removeKey(npc.getUID());
		stocking.removeKey(npc.getUID());
		inventories.removeKey(npc.getUID());
		unlimiteds.removeKey(npc.getUID());
	}

	@Override
	public void set(HumanNPC npc) {
		set(npc, true);
	}

	@Override
	public void set(HumanNPC npc, boolean value) {
		saveTrader(npc.getUID(), value);
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return isTrader(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.TRADER;
	}
}
