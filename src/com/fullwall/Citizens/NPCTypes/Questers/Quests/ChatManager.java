package com.fullwall.Citizens.NPCTypes.Questers.Quests;

import java.util.ArrayList;
import java.util.List;

import com.fullwall.Citizens.Utils.StringUtils;

public class ChatManager {
	private static List<String> hasEditMode = new ArrayList<String>();

	public static boolean hasEditMode(String name) {
		return hasEditMode.contains(name);
	}

	public static void enterEditMode(String name) {
		if (!hasEditMode.contains(name)) {
			hasEditMode.add(name);
		}
	}

	public static boolean isAnswer(String name, String answer) {
		if (!hasEditMode.contains(name)) {
			return false;
		}
		if (StringUtils.isNumber(answer)) {
			return true;
		}
		return false;
	}
}