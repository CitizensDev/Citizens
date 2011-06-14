package com.fullwall.Citizens.NPCTypes.Questers.Quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fullwall.Citizens.Utils.StringUtils;

public class ChatManager {
	public enum QuestOperation {
		CREATE, EDIT, REMOVE;
	}

	private static final List<String> hasEditMode = new ArrayList<String>();
	// Map<currentQuestion, Entry<optionNumber, optionName>>
	private static final Map<Integer, Entry<Integer, String>> answers = new HashMap<Integer, Entry<Integer, String>>();
	public static int currentQuestion = 0;

	public static boolean hasEditMode(String name) {
		return hasEditMode.contains(name);
	}

	public static void enterEditMode(String name) {
		if (!hasEditMode.contains(name)) {
			hasEditMode.add(name);
		}
	}

	public static boolean isValidAnswer(String name, String answer,
			int maxOptions) {
		if (!hasEditMode.contains(name)) {
			return false;
		}
		int option = 0;
		if (StringUtils.isNumber(answer)) {
			option = Integer.parseInt(answer);
			if (option <= maxOptions) {
				currentQuestion++;
				return true;
			}
		} else if (answers.get(currentQuestion).getValue()
				.equalsIgnoreCase(answer)) {
			return true;
		}
		return false;
	}

	public static int getMaxOptions(int currentQuestion) {
		return answers.get(currentQuestion).getKey();
	}
}