package net.citizensnpcs.npctypes.traders;

import java.util.ArrayList;

public class TraderManager {
	public static final ArrayList<Integer> tasks = new ArrayList<Integer>();

	public enum Mode {
		NORMAL, STOCK, INFINITE;
	}
}