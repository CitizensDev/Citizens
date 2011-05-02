package com.fullwall.Citizens.Utils;

import java.util.ArrayList;

import net.minecraft.server.InventoryPlayer;

import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Traders.Buyable;
import com.fullwall.Citizens.Traders.Sellable;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderPropertyPool {
	public static final PropertyHandler traders = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.traders");
	public static final PropertyHandler inventories = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.inventories");
	public static final PropertyHandler buying = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.buying");
	public static final PropertyHandler selling = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.selling");
	public static final PropertyHandler balances = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.balances");
	public static final PropertyHandler unlimiteds = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.unlimited");

	public static void saveAll() {
		traders.save();
		inventories.save();
		buying.save();
		selling.save();
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

	// TODO
	public static void addBuyable(Buyable b) {
		// buying.setBoolean(UID, unlimited);
	}

	public static ArrayList<Buyable> getBuyables(int UID) {
		ArrayList<Buyable> buyables = new ArrayList<Buyable>();
		return buyables;
	}

	public static void addSellable(Sellable s) {
		// buying.setBoolean(UID, unlimited);
	}

	public static ArrayList<Sellable> getSellables(int UID) {
		ArrayList<Sellable> sellables = new ArrayList<Sellable>();
		return sellables;
	}

	public static void saveTraderState(HumanNPC npc) {
		if (isTrader(npc.getUID()))
			saveTrader(npc.getUID(), npc.isTrader());
		saveInventory(npc.getUID(), npc.getBukkitEntity().getInventory());
		saveBalance(npc.getUID(), npc.getTraderNPC().getBalance());
		saveUnlimited(npc.getUID(), npc.getTraderNPC().isUnlimited());
	}
}
