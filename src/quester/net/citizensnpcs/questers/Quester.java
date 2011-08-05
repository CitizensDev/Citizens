package net.citizensnpcs.questers;

import java.util.ArrayDeque;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCManager;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.questers.quests.QuestManager;
import net.citizensnpcs.questers.rewards.QuestReward;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.PageUtils;
import net.citizensnpcs.utils.PageUtils.PageInstance;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Quester extends CitizensNPC {
	private PageInstance display;
	private Player previous;
	private final ArrayDeque<String> quests = new ArrayDeque<String>();

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

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (QuestManager.getProfile(player.getName()).hasQuest()) {
			PlayerProfile profile = QuestManager.getProfile(player.getName());
			if (profile.getProgress().fullyCompleted()
					&& profile.getProgress().getQuesterUID() == npc.getUID()) {
				Quest quest = QuestManager.getQuest(profile.getProgress()
						.getQuestName());
				Messaging.send(player, quest.getCompletedText());
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
				QuestManager.assignQuest(npc, player, quests.peek());
				Messaging.send(player, getQuest(quests.peek())
						.getAcceptanceText());
			}
		}
	}

	private void cycle(Player player) {
		if (quests == null) {
			player.sendMessage(ChatColor.GRAY + "No quests available.");
			return;
		}
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

	public boolean hasQuests() {
		return this.quests == null ? false : this.quests.size() > 0;
	}

	@Override
	public Properties getProperties() {
		return new QuesterProperties();
	}

	@Override
	public CommandHandler getCommands() {
		return new QuesterCommands();
	}

	@Override
	public void onEnable() {
		QuestProperties.initialize();
		QuestManager.initialize();
	}

	@Override
	public void addListeners() {
		CitizensNPCManager.addListener(new QuesterBlockListen());
		CitizensNPCManager.addListener(new QuesterPlayerListen());
	}
}