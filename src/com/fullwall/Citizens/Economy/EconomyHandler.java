package com.fullwall.Citizens.Economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class EconomyHandler {
	private static boolean useEconomy = true;
	private static boolean serverEconomyEnabled = false;
	private static boolean useServerEconomy = false;

	private static String prefix = "prices.";
	public static String[] materialAddendums = { ".misc", ".wood", ".gold",
			".stone", ".iron", ".diamond", ".leather", ".chainmail" };

	public enum Operation {
		/**
		 * Create a basic NPC
		 */
		BASIC_CREATION,
		/**
		 * Create a trader NPC
		 */
		TRADER_CREATION,
		/**
		 * Create a healer NPC
		 */
		HEALER_CREATION,
		/**
		 * Level-up a healer NPC
		 */
		HEALER_LEVELUP,
		/**
		 * Heal a player using a healer-NPC
		 */
		HEALER_HEAL,
		/**
		 * Create a wizard NPC
		 */
		WIZARD_CREATION,
		/**
		 * Teleport between wizard NPCs
		 */
		WIZARD_TELEPORT,
		/**
		 * Change the time with a wizard NPC
		 */
		WIZARD_CHANGETIME,
		/**
		 * Spawn a mob with a wizard NPC
		 */
		WIZARD_SPAWNMOB,
		/**
		 * Toggle a storm with a wizard NPC
		 */
		WIZARD_TOGGLESTORM,
		/**
		 * Create a quester NPC
		 */
		QUESTER_CREATION,
		/**
		 * Create a blacksmith NPC
		 */
		BLACKSMITH_CREATION,
		/**
		 * Repair tools using a blacksmith NPC
		 */
		BLACKSMITH_TOOLREPAIR,
		/**
		 * Repair armor using a blacksmith NPC
		 */
		BLACKSMITH_ARMORREPAIR,
		/**
		 * Create a bandit NPC
		 */
		BANDIT_CREATION,
		/**
		 * Create a guard NPC
		 */
		GUARD_CREATION;

		/**
		 * Changes an operation enum to a string value for use in settings
		 * retrieval.
		 * 
		 * @param op
		 * @param addendum
		 * @return
		 */
		public static String getString(Operation op, String addendum) {
			return prefix + op.toString().toLowerCase().replace("_", ".")
					+ addendum;
		}
	}

	/**
	 * Used for economy plugin support.
	 * 
	 * @param value
	 */
	public static void setServerEconomyEnabled(boolean value) {
		serverEconomyEnabled = value;
	}

	/**
	 * Checks if types of economy are being used.
	 * 
	 */
	public static void setUpVariables(boolean useEco, boolean useEconplugin) {
		useEconomy = useEco;
		useServerEconomy = useEconplugin;
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
	 * A helper method that checks a few variables for whether economy plugins
	 * should be enabled. (is using economy plugins enabled? is economy enabled?
	 * is an economy plugin loaded?)
	 * 
	 * @return
	 */
	public static boolean useIconomy() {
		return (useServerEconomy && useEconomy && serverEconomyEnabled);
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
			if (useIconomy()) {
				return ServerEconomyInterface.hasEnough(player, op);
			} else {
				return ItemInterface.hasEnough(player, op);
			}
		}
		return true;
	}

	public static boolean canBuyBlacksmith(Player player, Operation op) {
		if (useEconomy) {
			if (useIconomy()) {
				return ServerEconomyInterface.hasEnoughBlacksmith(player, op);
			} else {
				return ItemInterface.hasEnoughBlacksmith(player, op);
			}
		}
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
			if (payment.isiConomy() && useIconomy()) {
				return ServerEconomyInterface.hasEnough(payment, player);
			} else {
				return ItemInterface.hasEnough(payment, player);
			}
		} else {
			return true;
		}
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
			if (payment.isiConomy() && useIconomy()) {
				return ServerEconomyInterface.hasEnough(payment, npc);
			} else {
				return ItemInterface.hasEnough(payment, npc.getPlayer());
			}
		} else {
			return true;
		}
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
			if (useIconomy()) {
				return ServerEconomyInterface.pay(player, op);
			} else {
				return ItemInterface.pay(player, op);
			}
		} else {
			return 0;
		}
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
			if (useIconomy()) {
				return ServerEconomyInterface.pay(player, op, multiple);
			} else {
				return ItemInterface.pay(player, op, multiple);
			}
		} else {
			return 0;
		}
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
			if (payment.isiConomy() && useIconomy()) {
				return ServerEconomyInterface.pay(npc, payment);
			} else {
				return ItemInterface.pay(npc.getPlayer(), payment, slot);
			}
		} else {
			return 0;
		}
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
			if (payment.isiConomy() && useIconomy()) {
				return ServerEconomyInterface.pay(player, payment);
			} else {
				return ItemInterface.pay(player, payment, slot);
			}
		} else {
			return 0;
		}
	}

	/**
	 * Pay a price for a blacksmith operation
	 * 
	 * @param op
	 * @param player
	 * @param multiple
	 * @return
	 */
	public static double payBlacksmith(Operation op, Player player) {
		if (useEconomy) {
			if (useIconomy()) {
				return ServerEconomyInterface.payBlacksmith(player,
						player.getItemInHand(), op);
			} else {
				return ItemInterface.payBlacksmith(player,
						player.getItemInHand(), op);
			}
		}
		return 0;
	}

	/**
	 * Gets what item ID or iConomy currency is being used for an operation.
	 * 
	 * @param op
	 * @param amount
	 * @return
	 */
	public static String getPaymentType(Operation op, String amount) {
		if (useEconomy) {
			if (useIconomy()) {
				return ServerEconomyInterface.format(amount);
			} else {
				return ItemInterface.getCurrency(op);
			}
		} else {
			return "None";
		}
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
			if (useIconomy()) {
				return ServerEconomyInterface.getRemainder(op, player);
			} else {
				return ItemInterface.getRemainder(op, player);
			}
		} else {
			return "0";
		}
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
				return ServerEconomyInterface.format(payment.getPrice());
			} else {
				return ItemInterface.getCurrency(payment, colour);
			}
		} else {
			return "0";
		}
	}

	/**
	 * Get the index of the material addendums array based on an item ID
	 * 
	 * @param item
	 * @return
	 */
	public static int getBlacksmithIndex(ItemStack item) {
		int id = item.getTypeId();
		if (id == 259 || id == 346) {
			return 0;
		} else if ((id >= 268 && id <= 271) || id == 290) {
			return 1;
		} else if ((id >= 283 && id <= 286) || id == 294
				|| (id >= 314 && id <= 317)) {
			return 2;
		} else if ((id >= 272 && id <= 275) || id == 291) {
			return 3;
		} else if ((id >= 256 && id <= 258) || id == 267 || id == 292
				|| (id >= 306 && id <= 309)) {
			return 4;
		} else if ((id >= 276 && id <= 279) || id == 293
				|| (id >= 310 && id <= 313)) {
			return 5;
		} else if ((id >= 298 && id <= 301)) {
			return 6;
		} else if ((id >= 302 && id <= 305)) {
			return 7;
		}
		return 0;
	}
}