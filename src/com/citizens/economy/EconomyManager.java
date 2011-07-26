package com.citizens.economy;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.SettingsManager.Constant;
import com.citizens.properties.properties.UtilityProperties;

public class EconomyManager {
	private static boolean useEconomy = Constant.UseEconomy.toBoolean();
	private static boolean serverEconomyEnabled = false;
	private static boolean useServerEconomy = Constant.UseEconPlugin
			.toBoolean();

	public static final String[] materialAddendums = { ".misc", ".wood",
			".gold", ".stone", ".iron", ".diamond", ".leather", ".chainmail" };

	/**
	 * Used for economy-plugin support.
	 * 
	 * @param value
	 */
	public static void setServerEconomyEnabled(boolean value) {
		serverEconomyEnabled = value;
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
	 * A helper method that checks a few variables for whether economy-plugins
	 * should be enabled. (is using economy-plugins enabled? is economy enabled?
	 * is an economy-plugin loaded?)
	 * 
	 * @return
	 */
	public static boolean useEconPlugin() {
		return (useServerEconomy && useEconomy && serverEconomyEnabled);
	}

	/**
	 * Pay a price for a blacksmith operation
	 * 
	 * @param player
	 * @param op
	 * @return
	 */
	public static double payBlacksmith(Player player, EconomyOperation op) {
		if (useEconomy) {
			if (useEconPlugin()) {
				return ServerEconomyInterface.payBlacksmith(player, op);
			} else {
				return ItemInterface.payBlacksmith(player, op);
			}
		}
		return 0;
	}

	/**
	 * Gets what item ID or economy-plugin currency is being used for an
	 * operation.
	 * 
	 * @param player
	 * @param op
	 * @param amount
	 * @return
	 */
	public static String getPaymentType(Player player, EconomyOperation op,
			String amount) {
		if (useEconomy) {
			if (useEconPlugin()) {
				return ServerEconomyInterface.format(amount);
			} else {
				if (op.getNPCType().equals("blacksmith")) {
					return ItemInterface.getBlacksmithCurrency(player, op);
				}
				return ItemInterface.getCurrency(op);
			}
		}
		return "None";
	}

	/**
	 * Gets what is necessary to complete an operation.
	 * 
	 * @param op
	 * @param player
	 * @return
	 */
	public static String getRemainder(EconomyOperation op, Player player) {
		if (useEconomy) {
			if (useEconPlugin()) {
				return ServerEconomyInterface.getRemainder(op, player);
			} else {
				return ItemInterface.getRemainder(op, player);
			}
		}
		return "0";
	}

	/**
	 * Get the index of the material addendums array based on an item ID
	 * 
	 * @param item
	 * @return
	 */
	public static int getBlacksmithIndex(ItemStack item) {
		int id = item.getTypeId();
		if (id == 259 || id == 346 || id == 359) {
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

	public static EconomyOperation getOperation(String path) {
		return new EconomyOperation(path, "basic",
				UtilityProperties.getCurrencyID(path),
				UtilityProperties.getItemPrice(path),
				UtilityProperties.getEconPluginPrice(path));
	}
}