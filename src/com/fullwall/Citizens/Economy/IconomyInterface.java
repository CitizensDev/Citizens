package com.fullwall.Citizens.Economy;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;

public class IconomyInterface {

	public static boolean playerHasEnough(String name, double amount) {
		if (iConomy.getBank().hasAccount(name))
			return iConomy.getBank().getAccount(name).hasEnough(amount);
		else
			return false;
	}

	public static double getBalance(String name) {
		if (iConomy.getBank().hasAccount(name))
			return iConomy.getBank().getAccount(name).getBalance();
		else
			return -1;
	}

	public static String getCurrency() {
		return iConomy.getBank().getCurrency();
	}

	public static String getRemainder(Operation op, Player player) {
		int price = PropertyPool.getPrice(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-iconomy");
		Account acc = iConomy.getBank().getAccount(player.getName());
		return "" + (price - acc.getBalance());
	}

	public static boolean hasEnough(Player player, Operation op) {
		int price = PropertyPool.getPrice(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-iconomy");
		return playerHasEnough(player.getName(), price);
	}

	public static boolean hasEnough(Payment payment, Player player) {
		return playerHasEnough(player.getName(), payment.getPrice());
	}

	public static boolean hasEnough(Payment payment, HumanNPC npc) {
		return npc.getTraderNPC().getBalance() > payment.getPrice();
	}

	public static int pay(Player player, Operation op) {
		int price = PropertyPool.getPrice(op.toString().toLowerCase()
				.replace("_", "-")
				+ "-iconomy");
		iConomy.getBank().getAccount(player.getName()).subtract(price);
		return price;
	}

	public static int pay(HumanNPC npc, Payment payment) {
		npc.getTraderNPC().setBalance(
				npc.getTraderNPC().getBalance() - payment.getPrice());
		return payment.getPrice();
	}

	public static int pay(Player player, Payment payment) {
		iConomy.getBank().getAccount(player.getName())
				.subtract(payment.getPrice());
		return payment.getPrice();
	}
}
