package com.fullwall.Citizens.Economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Traders.ItemPrice;
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

	public static boolean canBuy(Operation op, Player player) {
		if (useEconomy) {
			if (useiConomy && iConomyEnabled)
				return IconomyInterface.hasEnough(player, op);
			else
				return ItemInterface.hasEnough(player, op);
		} else
			return true;
	}

	public static boolean canBuy(ItemPrice price, Player player) {
		if (useEconomy) {
			if (price.isIconomy() && useiConomy && iConomyEnabled)
				return IconomyInterface.hasEnough(price, player);
			else
				return ItemInterface.hasEnough(price, player);
		} else
			return true;
	}

	public static boolean canBuy(ItemPrice price, HumanNPC npc) {
		if (useEconomy) {
			if (price.isIconomy() && useiConomy && iConomyEnabled)
				return IconomyInterface.hasEnough(price, npc);
			else
				return ItemInterface.hasEnough(price,
						(Player) npc.getBukkitEntity());
		} else
			return true;
	}

	public static int pay(Operation op, Player player) {
		if (useEconomy) {
			if (iConomyEnabled && useiConomy)
				return IconomyInterface.pay(player, op);
			else
				return ItemInterface.pay(player, op);
		} else
			return 0;
	}

	public static int pay(ItemPrice price, HumanNPC npc) {
		if (useEconomy) {
			if (price.isIconomy() && iConomyEnabled && useiConomy)
				return IconomyInterface.pay(npc, price);
			else
				return ItemInterface.pay((Player) npc.getBukkitEntity(), price);
		} else
			return 0;
	}

	public static int pay(ItemPrice price, Player player) {
		if (useEconomy) {
			if (price.isIconomy() && iConomyEnabled && useiConomy)
				return IconomyInterface.pay(player, price);
			else
				return ItemInterface.pay(player, price);
		} else
			return 0;
	}

	public static String getPaymentType(Operation op) {
		if (useEconomy) {
			if (iConomyEnabled && useiConomy)
				return IconomyInterface.getCurrency();
			else
				return ItemInterface.getCurrency(op);
		} else
			return "None";
	}

	public static String getRemainder(Operation op, Player player) {
		if (useEconomy) {
			if (iConomyEnabled && useiConomy)
				return IconomyInterface.getRemainder(op, player);
			else
				return ItemInterface.getRemainder(op, player);
		} else
			return "0";
	}

	public static String getCurrency(ItemPrice price) {
		if (useEconomy) {
			if (price.isIconomy() && iConomyEnabled && useiConomy)
				return IconomyInterface.getCurrency();
			else
				return Material.getMaterial(price.getItemID()).name();
		} else
			return "0";
	}

	public static boolean useIconomy() {
		return (useiConomy && useEconomy && iConomyEnabled);
	}
}
