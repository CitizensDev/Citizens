package com.citizens.NPCTypes.Questers;

import java.util.ArrayDeque;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.citizens.Interfaces.Clickable;
import com.citizens.Interfaces.Toggleable;
import com.citizens.NPCTypes.Questers.CompletedQuest;
import com.citizens.NPCTypes.Questers.PlayerProfile;
import com.citizens.NPCTypes.Questers.Quest;
import com.citizens.NPCTypes.Questers.Reward;
import com.citizens.NPCTypes.Questers.Quests.QuestManager;
import com.citizens.NPCTypes.Questers.Rewards.QuestReward;
import com.citizens.Utils.PageUtils;
import com.citizens.Utils.StringUtils;
import com.citizens.Utils.PageUtils.PageInstance;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;
import com.iConomy.util.Messaging;

public class QuesterNPC extends Toggleable implements Clickable {
	private PageInstance display;
	private Player previous;
	private final ArrayDeque<String> quests = new ArrayDeque<String>();

	/**
	 * Quester NPC object
	 * 
	 * @param npc
	 */
	public QuesterNPC(HumanNPC npc) {
		super(npc);
	}

	/**
	 * Add a quest
	 * 
	 * @param quest
	 */
	public void addQuest(String quest) {
		quests.push(quest);
	}

	public void removeQuest(String quest) {
		quests.removeFirstOccurrence(quest);
	}

	public ArrayDeque<String> getQuests() {
		return quests;
	}

	@Override
	public String getType() {
		return "quester";
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		previous = player;
		cycle(player);
	}

	private void cycle(Player player) {
		quests.addLast(quests.pop());
		Quest quest = getQuest(quests.peek());
		display = PageUtils.newInstance(player);
		display.setSmoothTransition(true);
		display.header(ChatColor.GREEN + "======= Quest %x/%y - "
				+ StringUtils.wrap(quest.getName()) + " =======");
		for (String push : quest.getDescription().split("<br>")) {
			display.push(push);
			if (display.elements() % 8 == 0 && display.maxPages() == 1) {
				display.push(ChatColor.GOLD
						+ "Right click to continue description.");
			} else if (display.elements() % 9 == 0) {
				display.push(ChatColor.GOLD
						+ "Right click to continue description.");
			}
		}
		if (display.maxPages() == 1)
			player.sendMessage(ChatColor.GOLD + "Right click to accept.");
		display.process(1);
	}

	private Quest getQuest(String name) {
		return QuestManager.getQuest(name);
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (QuestManager.getProfile(player.getName()).hasQuest()) {
			PlayerProfile profile = QuestManager.getProfile(player.getName());
			if (profile.getProgress().fullyCompleted()
					&& profile.getProgress().getQuesterUID() == this.npc
							.getUID()) {
				Quest quest = QuestManager.getQuest(profile.getProgress()
						.getQuestName());
				Messaging.send(quest.getCompletedText());
				for (Reward reward : quest.getRewards()) {
					if (reward instanceof QuestReward)
						((QuestReward) reward).grantQuest(player, npc);
					else
						reward.grant(player);
				}
				long elapsed = System.currentTimeMillis()
						- profile.getProgress().getStartTime();
				profile.setProgress(null);
				profile.addCompletedQuest(new CompletedQuest(quest, npc
						.getStrippedName(), elapsed));
				QuestManager.setProfile(player.getName(), profile);

			}
		} else {
			if (previous == null
					|| !previous.getName().equals(player.getName())) {
				previous = player;
				cycle(player);
			}
			if (display.currentPage() != display.maxPages()) {
				display.displayNext();
				if (display.currentPage() == display.maxPages()) {
					player.sendMessage(ChatColor.GOLD
							+ "Right click to accept.");
				}
			} else {
				QuestManager.assignQuest(this.npc, player, quests.peek());
			}
		}
	}

	public boolean hasQuests() {
		return this.quests == null ? false : this.quests.size() > 0;
	}
}