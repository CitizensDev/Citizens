package com.fullwall.Citizens.Economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class EconomyHandler {
	private static boolean useEconomy = true;
	private static boolean iConomyEnabled = false;
	private static boolean useiConomy = false;

	// Common buying operations
	public enum Operation {
		BASIC_NPC_CREATE, TRADER_NPC_CREATE
	}

	public static void setiConomyEnable(boolean value) {
		iConomyEnabled = value;
	}

	public static void setUpVariables() {
		useEconomy = PropertyPool.checkEconomyEnabled();
		useiConomy = PropertyPool.checkiConomyEnabled();
	}

	public static boolean useEconomy() {
		return useEconomy;
	}

	public static boolean useIconomy() {
		return (useiConomy && useEconomy && iConomyEnabled);
	}

	public static boolean canBuy(Operation op, Player player) {
		if (useEconomy) {
			if (useIconomy())
				return IconomyInterface.hasEnough(player, op);
			else
				return ItemInterface.hasEnough(player, op);
		} else
			return true;
	}

	public static boolean canBuy(Payment payment, Player player) {
		if (useEconomy) {
			if (payment.isiConomy() && useIconomy())
				return IconomyInterface.hasEnough(payment, player);
			else
				return ItemInterface.hasEnough(payment, player);
		} else
			return true;
	}

	public static boolean canBuy(Payment payment, HumanNPC npc) {
		if (useEconomy) {
			if (payment.isiConomy() && useIconomy())
				return IconomyInterface.hasEnough(payment, npc);
			else
				return ItemInterface.hasEnough(payment,
						(Player) npc.getPlayer());
		} else
			return true;
	}

	public static int pay(Operation op, Player player) {
		if (useEconomy) {
			if (useIconomy())
				return IconomyInterface.pay(player, op);
			else
				return ItemInterface.pay(player, op);
		} else
			return 0;
	}

	public static int pay(Payment payment, HumanNPC npc) {
		if (useEconomy) {
			if (payment.isiConomy() && useIconomy())
				return IconomyInterface.pay(npc, payment);
			else
				return ItemInterface.pay((Player) npc.getPlayer(), payment);
		} else
			return 0;
	}

	public static int pay(Payment payment, Player player) {
		if (useEconomy) {
			if (payment.isiConomy() && useIconomy())
				return IconomyInterface.pay(player, payment);
			else
				return ItemInterface.pay(player, payment);
		} else
			return 0;
	}

	public static String getPaymentType(Operation op, String amount) {
		if (useEconomy) {
			if (useIconomy())
				return IconomyInterface.getCurrency(amount);
			else
				return ItemInterface.getCurrency(op);
		} else
			return "None";
	}

	public static String getRemainder(Operation op, Player player) {
		if (useEconomy) {
			if (useIconomy())
				return IconomyInterface.getRemainder(op, player);
			else
				return ItemInterface.getRemainder(op, player);
		} else
			return "0";
	}

	public static String getCurrency(Payment payment) {
		if (useEconomy) {
			if (payment.isiConomy() && useIconomy())
				return IconomyInterface.getCurrency(payment.getPrice());
			else
				return Material.getMaterial(payment.getItem().getTypeId())
						.name();
		} else
			return "0";
	}
}
