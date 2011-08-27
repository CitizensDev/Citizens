package net.citizensnpcs.traders;

import java.util.ArrayList;
import java.util.List;

public class TraderManager {
	public static final List<Integer> tasks = new ArrayList<Integer>();

	public enum TraderMode {
		NORMAL, STOCK, INFINITE;
	}
}