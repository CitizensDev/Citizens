package com.citizens.properties.properties;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.inventory.ItemStack;

import com.citizens.interfaces.Saveable;
import com.citizens.npcs.NPCTypeManager;
import com.citizens.npctypes.traders.Check;
import com.citizens.npctypes.traders.ItemPrice;
import com.citizens.npctypes.traders.Stockable;
import com.citizens.npctypes.traders.TraderNPC;
import com.citizens.properties.PropertyManager;
import com.citizens.resources.npclib.HumanNPC;
import com.google.common.base.Joiner;

public class TraderProperties extends PropertyManager implements Saveable {
	private final String isTrader = ".trader.toggle";
	private final String stock = ".trader.stock";
	private final String unlimited = ".trader.unlimited";
	private final String balance = ".trader.balance";

	private void saveBalance(int UID, double traderBalance) {
		profiles.setDouble(UID + balance, traderBalance);
	}

	private double getBalance(int UID) {
		return profiles.getDouble(UID + balance);
	}

	private void saveUnlimited(int UID, boolean value) {
		profiles.setBoolean(UID + unlimited, value);
	}

	private boolean isUnlimited(int UID) {
		return profiles.getBoolean(UID + unlimited);
	}

	private void setStockables(int UID, String set) {
		profiles.setString(UID + stock, set);
	}

	@SuppressWarnings("unused")
	private void removeStockable(int UID, Stockable s) {
		String write = "";
		write += s.toString() + ";";
		String read = profiles.getString(UID + stock).replace(write, "");
		profiles.setString(UID + stock, read);
	}

	private void saveStockables(int UID,
			ConcurrentHashMap<Check, Stockable> stockables) {
		/*
		 * for (Stockable entry : stockables.values()) { if
		 * (!string.contains(entry.toString())) string += (entry.toString() +
		 * ";"); count += 1; }
		 */
		setStockables(UID, Joiner.on(";").join(stockables.values()));
	}

	private ConcurrentHashMap<Check, Stockable> getStockables(int UID) {
		ConcurrentHashMap<Check, Stockable> stockables = new ConcurrentHashMap<Check, Stockable>();
		int i = 0;
		for (String s : profiles.getString(UID + stock).split(";")) {
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
					short data = Short.parseShort(split[2]);
					stack.setDurability(data);
					break;
				case 1:
					String[] parts = main.split("/");
					if (parts.length == 2) {
						price = new ItemPrice(Double.parseDouble(parts[0]));
						price.setEconPlugin(Boolean.parseBoolean(parts[1]));
					} else {
						price = new ItemPrice(createItemStack(
								Integer.parseInt(parts[0]),
								Integer.parseInt(parts[1]),
								Integer.parseInt(parts[2])));
						price.setEconPlugin(Boolean.parseBoolean(parts[3]));
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
		item.setDurability((short) data);
		return item;
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("trader");
			setEnabled(npc, is);
			if (is) {
				TraderNPC trader = npc.getToggleable("trader");
				saveUnlimited(npc.getUID(), trader.isUnlimited());
				saveStockables(npc.getUID(), trader.getStocking());
				saveBalance(npc.getUID(), npc.getBalance());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("trader", NPCTypeManager.getFactory("trader"));
			TraderNPC trader = npc.getToggleable("trader");
			npc.setBalance(getBalance(npc.getUID()));
			trader.setUnlimited(isUnlimited(npc.getUID()));
			trader.setStocking(getStockables(npc.getUID()));
		}
		saveState(npc);
	}

	@Override
	public void register(HumanNPC npc) {
		setEnabled(npc, true);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isTrader, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isTrader);
	}

	@Override
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isTrader)) {
			profiles.setString(nextUID + isTrader,
					profiles.getString(UID + isTrader));
		}
		if (profiles.pathExists(UID + stock)) {
			profiles.setString(nextUID + stock, profiles.getString(UID + stock));
		}
		if (profiles.pathExists(UID + unlimited)) {
			profiles.setString(nextUID + unlimited,
					profiles.getString(UID + unlimited));
		}
		if (profiles.pathExists(UID + balance)) {
			profiles.setString(nextUID + balance, UID + balance);
		}
	}
}
