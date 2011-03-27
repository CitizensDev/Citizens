package com.fullwall.Citizens.Economy;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;

public class IconomyInterface {

	public static double getBalance(String name) {
		if (iConomy.getBank().hasAccount(name))
			return iConomy.getBank().getAccount(name).getBalance();
		else
			return -1;
	}

	public static boolean hasEnough(Player player, Operation op) {
		int price = PropertyPool.getPrice(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-iconomy");
		return getBalance(player.getName()) > price;
	}

	public static int pay(Player player, Operation op) {
		int price = PropertyPool.getPrice(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-iconomy");
		Account acc = iConomy.getBank().getAccount(player.getName());
		acc.subtract(price);
		return price;
	}

	public static String getCurrency() {
		return iConomy.getBank().getCurrency();
	}

	public static String getRemainder(Operation op, Player player) {
		int price = PropertyPool.getPrice(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-iconomy");
		Account acc = iConomy.getBank().getAccount(player.getName());
		return "" + (acc.getBalance() - price);
	}
}
