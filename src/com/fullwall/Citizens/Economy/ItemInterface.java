package com.fullwall.Citizens.Economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Utils.PropertyPool;

public class ItemInterface {

	public static boolean hasEnough(Player player, Operation op) {
		int price = PropertyPool.getPrice(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-item");
		int currencyID = PropertyPool.getCurrencyID(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-item-currency-id");
		return player.getInventory().contains(currencyID, price);
	}

	public static int pay(Player player, Operation op) {
		int price = PropertyPool.getPrice(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-item");
		int currencyID = PropertyPool.getCurrencyID(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-item-currency-id");
		PlayerInventory inv = player.getInventory();
		int current = price;
		for (ItemStack i : inv.getContents()) {
			if (i.getTypeId() == currencyID) {
				int amount = i.getAmount();
				int toChange = 0;
				current -= amount;
				if (current < 0) {
					// we've gone over what we need to take.
					// take away the negative to get the positive value
					// (remainder).
					toChange -= current;
				}
				// air blocks are null
				if (toChange == 0)
					i = null;
				else {
					i.setAmount(toChange);
					break;
				}
			}
		}
		return price;
	}

	public static String getCurrency(Operation op) {
		int ID = PropertyPool.getCurrencyID(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-item-currency-id");
		return Material.getMaterial(ID) != null ? Material.getMaterial(ID)
				.name() : "";
	}
}
