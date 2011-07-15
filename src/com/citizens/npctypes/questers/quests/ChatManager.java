package com.citizens.npctypes.questers.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatManager {
	public enum QuestOperation {
		CREATE, EDIT, REMOVE;
	}

	private static final List<String> hasEditMode = new ArrayList<String>();
	public static int currentQuestion = 0;

	public static boolean hasEditMode(String name) {
		return hasEditMode.contains(name);
	}

	public static void setEditMode(String name, boolean edit) {
		if (!hasEditMode(name) && edit) {
			hasEditMode.add(name);
		} else if (hasEditMode(name) && !edit) {
			hasEditMode.remove(name);
		}
	}

	public static void startSession(PlayerChatEvent event, Player player,
			QuestOperation questOp) {
		switch (questOp) {
		case CREATE:
			create(event);
		case EDIT:
			edit(event);
		case REMOVE:
			remove(event);
		}
	}

	private static void create(PlayerChatEvent event) {
		// TODO take a player through the creation process here.
	}

	private static void edit(PlayerChatEvent event) {
		// TODO allow a player to edit settings that they ask to
		// change..."What setting would you like to edit?"
	}

	private static void remove(PlayerChatEvent event) {
		// TODO remove a quest
	}
}