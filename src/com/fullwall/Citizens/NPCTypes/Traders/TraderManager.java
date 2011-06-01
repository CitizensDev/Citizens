package com.fullwall.Citizens.NPCTypes.Traders;

import java.util.ArrayList;

public class TraderManager {
	public static ArrayList<Integer> tasks = new ArrayList<Integer>();

	public enum Mode {
		NORMAL,
		STOCK,
		INFINITE
	}
}