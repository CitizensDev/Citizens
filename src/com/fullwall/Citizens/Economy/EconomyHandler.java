package com.fullwall.Citizens.Economy;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Utils.PropertyPool;

public class EconomyHandler {
	private static boolean iConomyEnabled = false;
	private static boolean useiConomy = false;

	public enum Operation {
		BASIC_NPC_CREATE
	}

	public static void setiConomyEnable(boolean value) {
		iConomyEnabled = value;
	}

	public static void setUpVariables() {
		useiConomy = PropertyPool.checkiConomyEnabled();
	}

	public static boolean canBuy(Operation op, Player player) {
		if (useiConomy && iConomyEnabled)
			return IconomyInterface.hasEnough(player, op);
		else
			return ItemInterface.hasEnough(player, op);
	}

	public static int pay(Operation op, Player player) {
		if (iConomyEnabled && useiConomy)
			return IconomyInterface.pay(player, op);
		else
			return ItemInterface.pay(player, op);
	}

	public static String getPaymentType(Operation op) {
		if (iConomyEnabled && useiConomy)
			return IconomyInterface.getCurrency();
		else
			return ItemInterface.getCurrency(op);
	}
}
