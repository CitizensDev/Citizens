package net.citizensnpcs.traders;

import java.util.ArrayList;

public class TraderManager {
	public static final ArrayList<Integer> tasks = new ArrayList<Integer>();

	public enum TraderMode {
		NORMAL,
		STOCK,
		INFINITE;
	}
}