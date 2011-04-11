package com.fullwall.Citizens.Utils;

import com.fullwall.Citizens.PropertyHandler;

public class TraderPropertyPool {
	public static final PropertyHandler traders = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.traders");
	public static final PropertyHandler inventories = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.inventories");
	public static final PropertyHandler buying = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.buying");
	public static final PropertyHandler selling = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.selling");
	public static final PropertyHandler balances = new PropertyHandler(
			"plugins/Citizens/Traders/Citizens.balances");

	public static void saveAll() {
		traders.save();
		inventories.save();
		buying.save();
		selling.save();
		balances.save();
	}

	public static void saveTrader(int UID, boolean state) {
		traders.setBoolean(UID, state);
	}

	public static boolean isTrader(int UID) {
		return traders.keyExists(UID);
	}

	public static boolean getTraderState(int UID) {
		return traders.getBoolean(UID);
	}

	public static void removeTrader(int UID) {
		traders.removeKey(UID);
	}

}
