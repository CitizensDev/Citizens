package com.citizens.npctypes.blacksmiths;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.commands.CommandHandler;
import com.citizens.commands.commands.BlacksmithCommands;
import com.citizens.interfaces.Saveable;
import com.citizens.npctypes.CitizensNPC;
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
	public Map<String, Object> getDefaultSettings() {
		Map<String, Object> nodes = new HashMap<String, Object>();
		nodes.put("prices.blacksmith.armorrepair.leather", 0.25);
		nodes.put("prices.blacksmith.armorrepair.gold", 0.50);
		nodes.put("prices.blacksmith.armorrepair.chainmail", 0.75);
		nodes.put("prices.blacksmith.armorrepair.iron", 1);
		nodes.put("prices.blacksmith.armorrepair.diamond", 1.25);
		nodes.put("prices.blacksmith.toolrepair.wood", 0.25);
		nodes.put("prices.blacksmith.toolrepair.gold", 0.50);
		nodes.put("prices.blacksmith.toolrepair.stone", 0.75);
		nodes.put("prices.blacksmith.toolrepair.iron", 1);
		nodes.put("prices.blacksmith.toolrepair.diamond", 1.25);
		nodes.put("prices.blacksmith.toolrepair.misc", 0.50);
		return nodes;
	}
}