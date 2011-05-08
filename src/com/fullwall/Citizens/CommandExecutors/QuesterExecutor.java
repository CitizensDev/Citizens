package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Questers.QuestPropertyPool;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.QuesterPropertyPool;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private Citizens plugin;
	private String questName;
	private String questType;
	private String startNPC;
	private String description;
	private String completionText;

	public QuesterExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(MessageUtils.mustBeIngameMessage);
			return true;
		}
		Player player = (Player) sender;
		HumanNPC npc = null;
		boolean returnval = false;
		if (NPCManager.validateSelected((Player) sender))
			npc = NPCManager
					.getNPC(NPCManager.NPCSelected.get(player.getName()));
		else {
			sender.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			sender.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (!npc.isQuester()) {
			sender.sendMessage(ChatColor.RED + "Your NPC isn't a quester yet.");
			return true;
		} else {
			if (args.length == 2 && args[0].equals("createquest")) {
				if (BasicExecutor.hasPermission("citizens.quester.createquest",
						sender)) {
					QuesterPropertyPool.saveQuestName(npc.getUID(), args[1]);
					PropertyHandler file = getQuestFile(args[1]);
					setupQuestVariables(file, npc.getUID());
					sender.sendMessage(ChatColor.GREEN
							+ StringUtils
									.yellowify("You created a quest file called ")
							+ args[1] + ".quests");
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
		}
		return returnval;
	}

	/**
	 * Gets the file for a quest
	 * 
	 * @param args
	 * @return
	 */
	private PropertyHandler getQuestFile(String args) {
		PropertyHandler questFile = new PropertyHandler(
				"plugins/Citizens/Questers/Quests/" + args + ".quests");
		return questFile;
	}

	/**
	 * Set up all variables in a quest file
	 * 
	 * @param file
	 * @param UID
	 */
	private void setupQuestVariables(PropertyHandler file, int UID) {
		if (!QuestPropertyPool.quest.keyExists("quest-name")) {
			questName = QuestPropertyPool.getQuestName();
		}
		if(!QuestPropertyPool.quest.keyExists("quest-type")) {
			questType = QuestPropertyPool.getQuestType();
		}
		if(!QuestPropertyPool.quest.keyExists("start-npc")) {
			startNPC = QuestPropertyPool.getStartNPC();
		}
		if(!QuestPropertyPool.quest.keyExists("description")) {
			description = QuestPropertyPool.getDescription();
		}
		if(!QuestPropertyPool.quest.keyExists("completion-text")) {
			completionText = QuestPropertyPool.getCompletionText();
		}
	}
}
