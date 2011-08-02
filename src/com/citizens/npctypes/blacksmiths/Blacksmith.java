package com.citizens.npctypes.blacksmiths;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.commands.CommandHandler;
import com.citizens.commands.commands.BlacksmithCommands;
import com.citizens.interfaces.Saveable;
import com.citizens.npctypes.CitizensNPC;
import com.citizens.properties.Node;
import com.citizens.properties.SettingsManager.SettingsType;
import com.citizens.properties.properties.BlacksmithProperties;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.MessageUtils;

public class Blacksmith extends CitizensNPC {

	@Override
	public String getType() {
		return "blacksmith";
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (Permission.generic(player, "citizens.blacksmith.use.repair")) {
			String repairType = "";
			if (BlacksmithManager.validateTool(player.getItemInHand())) {
				repairType = "blacksmith-toolrepair";
			} else if (BlacksmithManager.validateArmor(player.getItemInHand())) {
				repairType = "blacksmith-armorrepair";
			}
			if (!repairType.isEmpty()) {
				BlacksmithManager.buyRepair(player, npc, repairType);
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}

	@Override
	public Saveable getProperties() {
		return new BlacksmithProperties();
	}

	@Override
	public CommandHandler getCommands() {
		return new BlacksmithCommands();
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.leather", 0.25));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.gold", 0.50));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.chainmail", 0.75));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.iron", 1));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.armorrepair.diamond", 1.25));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.wood", 0.25));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.gold", 0.50));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.stone", 0.75));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.iron", 1));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.diamond", 1.25));
		nodes.add(new Node("", SettingsType.ECONOMY,
				"prices.blacksmith.toolrepair.misc", 0.50));
		return nodes;
	}
}