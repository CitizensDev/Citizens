package com.fullwall.Citizens.Economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class EconomyHandler {
	private static boolean useEconomy = true;
	private static boolean iConomyEnabled = false;
	private static boolean useiConomy = false;

	// Parsed and used as settings for Citizens.economy.
	public enum Operation {
		/**
		 * Create a basic NPC
		 */
		BASIC_NPC_CREATE,
		/**
		 * Create a trader NPC
		 */
		TRADER_NPC_CREATE,
		/**
		 * Create a healer NPC
		 */
		HEALER_NPC_CREATE,
		/**
		 * Level-up a healer NPC
		 */
		HEALER_LEVEL_UP,
		/**
		 * Create a wizard NPC
		 */
		WIZARD_NPC_CREATE,
		/**
		 * Teleport between wizard NPCs
		 */
		WIZARD_TELEPORT,
		/**
		 * Create a quester NPC
		 */
		QUESTER_NPC_CREATE,
		/**
		 * Create a blacksmith NPC
		 */
		BLACKSMITH_NPC_CREATE,
		/**
		 * Repair tools using a blacksmith NPC
		 */
		BLACKSMITH_TOOL_REPAIR,
		/**
		 * Repair armor using a blacksmith NPC
		 */
		BLACKSMITH_ARMOR_REPAIR;

		/**
		 * Changes an operation enum to a string value for use in settings
		 * retrieval.
		 * 
		 * @param op
		 * @param addendum
		 * @return
		 */
		public static String getString(Operation op, String addendum) {
			return op.toString().toLowerCase().replace("_", "-") + addendum;
		}
	}

	/**
	 * Used for iConomy support.
	 * 
	 * @param value
	 */
	public static void setiConomyEnable(boolean value) {
		iConomyEnabled = value;
	}

	/**
	 * Checks if i/economy is being used.
	 */
	public static void setUpVariables() {
		useEconomy = PropertyPool.checkEconomyEnabled();
		useiConomy = PropertyPool.checkiConomyEnabled();
	}

	/**
	 * A helper method to check if economy is currently enabled.
	 * 
	 * @return
	 */
	public static boolean useEconomy() {
		return useEconomy;
	}

	/**
	 * A helper method that checks a few variables for whether iConomy should be
	 * enabled. (is using iConomy enabled? is economy enabled? is iConomy
	 * loaded?)
	 * 
	 * @return
	 */
	public static boolean useIconomy() {
		return (useiConomy && useEconomy && iConomyEnabled);
	}

	/**
	 * Interface for whether an operation can be carried out successfully.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static boolean canBuy(Operation op, Player player) {
		if (useEconomy) {
			if (useIconomy())
				return IconomyInterface.hasEnough(player, op);
			else
				return ItemInterface.hasEnough(player, op);
		} else
			return true;
	}

	/**
	 * Interface for whether a payment can be carried out successfully.
	 * 
	 * @param payment
	 * @param player
	 * @return
	 */
	public static boolean canBuy(Payment payment, Player player) {
		if (useEconomy) {
			if (payment.isiConomy() && useIconomy())
				return IconomyInterface.hasEnough(payment, player);
			else
				return ItemInterface.hasEnough(payment, player);
		} else
			return true;
	}

	/**
	 * Uses the npc iConomy balance (separate) to determine the payment's
	 * viability.
	 * 
	 * @param payment
	 * @param npc
	 * @return
	 */
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

	/**
	 * Pays using an operation.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static double pay(Operation op, Player player) {
		if (useEconomy) {
			if (useIconomy())
				return IconomyInterface.pay(player, op);
			else
				return ItemInterface.pay(player, op);
		} else
			return 0;
	}

	/**
	 * Pays using an operation.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static double pay(Operation op, Player player, int multiple) {
		if (useEconomy) {
			if (useIconomy())
				return IconomyInterface.pay(player, op, multiple);
			else
				return ItemInterface.pay(player, op, multiple);
		} else
			return 0;
	}

	/**
	 * Pays using a payment (uses the npc's iConomy balance).
	 * 
	 * @param payment
	 * @param npc
	 * @return
	 */
	public static double pay(Payment payment, HumanNPC npc, int slot) {
		if (useEconomy) {
			if (payment.isiConomy() && useIconomy())
				return IconomyInterface.pay(npc, payment);
			else
				return ItemInterface.pay((Player) npc.getPlayer(), payment,
						slot);
		} else
			return 0;
	}

	/**
	 * Pays using a payment.
	 * 
	 * @param payment
	 * @param player
	 * @return
	 */
	public static double pay(Payment payment, Player player, int slot) {
		if (useEconomy) {
			if (payment.isiConomy() && useIconomy())
				return IconomyInterface.pay(player, payment);
			else
				return ItemInterface.pay(player, payment, slot);
		} else
			return 0;
	}

	/**
	 * Gets what item ID or iConomy currency is being used for an operation.
	 * 
	 * @param op
	 * @param amount
	 * @return
	 */
	public static String getPaymentType(Operation op, String amount,
			ChatColor colour) {
		if (useEconomy) {
			if (useIconomy())
				return IconomyInterface.format(amount);
			else
				return ItemInterface.getCurrency(op, colour);
		} else
			return "None";
	}

	/**
	 * Gets what is necessary to complete an operation.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static String getRemainder(Operation op, Player player) {
		if (useEconomy) {
			if (useIconomy())
				return IconomyInterface.getRemainder(op, player);
			else
				return ItemInterface.getRemainder(op, player);
		} else
			return "0";
	}

	/**
	 * Gets the currency of a payment (iConomy currency or item name).
	 * 
	 * @param payment
	 * @return
	 */
	public static String getCurrency(Payment payment, ChatColor colour) {
		if (useEconomy) {
			if (payment.isiConomy() && useIconomy()) {
				return IconomyInterface.format(payment.getPrice());
			} else {
				return ItemInterface.getCurrency(payment, colour);
			}
		} else
			return "0";
	}
}
