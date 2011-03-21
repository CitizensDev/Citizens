package com.fullwall.Citizens.Economy;

import java.util.EnumMap;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Utils.PropertyPool;

public class EconomyHandler {
	private static boolean iConomyEnabled = false;
	private static boolean useiConomy = false;

	public enum Operation {
		BASIC_NPC_CREATE
	}

	public static EnumMap<Operation, String> OperationMapping;

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

	public static void pay(Operation op, Player player) {
		if (iConomyEnabled && useiConomy)
			IconomyInterface.pay(player, op);
		else
			ItemInterface.pay(player, op);
	}
}
