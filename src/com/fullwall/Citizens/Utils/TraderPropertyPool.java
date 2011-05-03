package com.fullwall.Citizens.Utils;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.InventoryPlayer;

import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Traders.Check;
import com.fullwall.Citizens.Traders.ItemPrice;
import com.fullwall.Citizens.Traders.Stockable;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderPropertyPool {
	public static final PropertyHandler traders = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.traders");
	public static final PropertyHandler inventories = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.inventories");
	public static final PropertyHandler stocking = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.stocking");
	public static final PropertyHandler balances = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.balances");
	public static final PropertyHandler unlimiteds = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.unlimited");

	public static void saveAll() {
		traders.save();
		inventories.save();
		stocking.save();
		balances.save();
		unlimiteds.save();
	}

	public static void saveTrader(int UID, boolean state) {
		traders.setBoolean(UID, state);
	}

	public static boolean isTrader(int UID) {
		return traders.keyExists(UID);
	}

	public static boolean getTraderState(int UID) {
		return traders.getBoolean(UID);
	}

	public static void removeTrader(int UID) {
		traders.removeKey(UID);
	}

	public static void saveInventory(int UID, PlayerInventory inv) {
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

	public static PlayerInventory getInventory(int UID) {
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
		inv.setContents((ItemStack[]) array.toArray());
		return inv;
	}

	private static int parse(String passed) {
		return Integer.parseInt(passed);
	}

	public static void saveBalance(int UID, int balance) {
		balances.setInt(UID, balance);
	}

	public static int getBalance(int UID) {
		return balances.getInt(UID);
	}

	public static void saveUnlimited(int UID, boolean unlimited) {
		unlimiteds.setBoolean(UID, unlimited);
	}

	public static boolean getUnlimited(int UID) {
		return unlimiteds.getBoolean(UID);
	}

	public static void addStockable(int UID, Stockable s) {
		String write = "";
		write += s.toString() + "||";
		stocking.setString(UID, write);
	}

	public static void removeStockable(int UID, Stockable s) {
		String write = "";
		write += s.toString() + "||";
		String read = stocking.getString(UID);
		read.replace(write, "");
		stocking.setString(UID, read);
	}

	public static void saveStockables(int UID,
			ConcurrentHashMap<Check, Stockable> stockables) {
		for (Entry<Check, Stockable> entry : stockables.entrySet()) {
			addStockable(UID, entry.getValue());
		}
	}

	public static ConcurrentHashMap<Check, Stockable> getStockables(int UID) {
		ConcurrentHashMap<Check, Stockable> stockables = new ConcurrentHashMap<Check, Stockable>();
		int i = 0;
		for (String s : stocking.getString(UID).split("||")) {
			i = 0;
			ItemStack stack = new ItemStack(37);
			ItemPrice price = new ItemPrice(0);
			boolean selling = false;
			for (String main : s.split("-")) {
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
						price = new ItemPrice(Integer.parseInt(parts[0]));
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

	public static void saveTraderState(HumanNPC npc) {
		if (isTrader(npc.getUID()))
			saveTrader(npc.getUID(), npc.isTrader());
		saveInventory(npc.getUID(), npc.getPlayer().getInventory());
		saveBalance(npc.getUID(), npc.getTraderNPC().getBalance());
		saveUnlimited(npc.getUID(), npc.getTraderNPC().isUnlimited());
	}
}
