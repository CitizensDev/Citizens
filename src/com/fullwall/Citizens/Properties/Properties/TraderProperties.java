package com.fullwall.Citizens.Properties.Properties;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.Interfaces.Saveable;
import com.fullwall.Citizens.NPCTypes.Traders.Check;
import com.fullwall.Citizens.NPCTypes.Traders.ItemPrice;
import com.fullwall.Citizens.NPCTypes.Traders.Stockable;
import com.fullwall.Citizens.Properties.PropertyHandler;
import com.fullwall.Citizens.Properties.PropertyManager.PropertyType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderProperties extends Saveable {
	private final PropertyHandler traders = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.traders");
	private final PropertyHandler stocking = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.stocking");
	private final PropertyHandler unlimiteds = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.unlimited");

	private void saveUnlimited(int UID, boolean unlimited) {
		unlimiteds.setBoolean(UID, unlimited);
	}

	private boolean getUnlimited(int UID) {
		return unlimiteds.getBoolean(UID);
	}

	private void setStockables(int UID, String set) {
		stocking.setString(UID, set);
	}

	@SuppressWarnings("unused")
	private void removeStockable(int UID, Stockable s) {
		String write = "";
		write += s.toString() + ";";
		String read = stocking.getString(UID).replace(write, "");
		stocking.setString(UID, read);
	}

	private void saveStockables(int UID,
			ConcurrentHashMap<Check, Stockable> stockables) {
		String string = "";
		setStockables(UID, string);
		int count = 0;
		for (Stockable entry : stockables.values()) {
			if (!string.contains(entry.toString()))
				string += entry.toString();
			count += 1;
		}
		setStockables(UID, string);
	}

	private ConcurrentHashMap<Check, Stockable> getStockables(int UID) {
		ConcurrentHashMap<Check, Stockable> stockables = new ConcurrentHashMap<Check, Stockable>();
		int i = 0;
		for (String s : stocking.getString(UID).split(";")) {
			if (s.isEmpty()) {
				continue;
			}
			i = 0;
			ItemStack stack = new ItemStack(37);
			ItemPrice price = new ItemPrice(0);
			boolean selling = false;
			for (String main : s.split(",")) {
				switch (i) {
				case 0:
					String[] split = main.split("/");
					stack = new ItemStack(Integer.parseInt(split[0]),
							Integer.parseInt(split[1]));
					MaterialData data = new MaterialData(stack.getType(),
							Byte.parseByte(split[2]));
					if (data != null) {
						stack.setData(data);
					}
					break;
				case 1:
					String[] parts = main.split("/");
					if (parts.length == 2) {
						price = new ItemPrice(Double.parseDouble(parts[0]));
						price.setiConomy(Boolean.parseBoolean(parts[1]));
					} else {
						price = new ItemPrice(createItemStack(
								Integer.parseInt(parts[0]),
								Integer.parseInt(parts[1]),
								Integer.parseInt(parts[2])));
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

	private ItemStack createItemStack(int amount, int itemID, int data) {
		ItemStack item = new ItemStack(itemID, amount);
		item.setData(new MaterialData(itemID, (byte) data));
		return item;
	}

	@Override
	public void saveFiles() {
		traders.save();
		stocking.save();
		unlimiteds.save();
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			setEnabled(npc, npc.isTrader());
			saveUnlimited(npc.getUID(), npc.getTrader().isUnlimited());
			saveStockables(npc.getUID(), npc.getTrader().getStocking());
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		npc.setTrader(getEnabled(npc));
		npc.getTrader().setUnlimited(getUnlimited(npc.getUID()));
		npc.getTrader().setStocking(getStockables(npc.getUID()));
		saveState(npc);
	}

	@Override
	public void removeFromFiles(HumanNPC npc) {
		traders.removeKey(npc.getUID());
		stocking.removeKey(npc.getUID());
		unlimiteds.removeKey(npc.getUID());
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		traders.setBoolean(npc.getUID(), value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return traders.getBoolean(npc.getUID());
	}

	@Override
	public boolean exists(HumanNPC npc) {
		return traders.keyExists(npc.getUID());
	}

	@Override
	public PropertyType type() {
		return PropertyType.TRADER;
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (traders.keyExists(UID)) {
			traders.setString(nextUID, traders.getString(UID));
		}
		if (stocking.keyExists(UID)) {
			stocking.setString(nextUID, stocking.getString(UID));
		}
		if (unlimiteds.keyExists(UID)) {
			unlimiteds.setString(nextUID, unlimiteds.getString(UID));
		}
	}
}