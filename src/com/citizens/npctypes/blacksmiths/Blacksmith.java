package com.citizens.npctypes.blacksmiths;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.commands.CommandHandler;
import com.citizens.commands.commands.BlacksmithCommands;
import com.citizens.economy.EconomyManager;
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
			String op = "";
			if (BlacksmithManager.validateTool(player.getItemInHand())) {
				op = "blacksmith-toolrepair";
			} else if (BlacksmithManager.validateArmor(player.getItemInHand())) {
				op = "blacksmith-armorrepair";
			}
			if (op != null) {
				BlacksmithManager.buyRepair(player, npc,
						EconomyManager.getOperation(op));
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
		nodes.put("prices.blacksmith.armorrepair.item-currency-id", 37);
		nodes.put("prices.blacksmith.armorrepair.econplugin.leather", 0.25);
		nodes.put("prices.blacksmith.armorrepair.econplugin.gold", 0.50);
		nodes.put("prices.blacksmith.armorrepair.econplugin.chainmail", 0.75);
		nodes.put("prices.blacksmith.armorrepair.econplugin.iron", 1);
		nodes.put("prices.blacksmith.armorrepair.econplugin.diamond", 1.25);
		nodes.put("prices.blacksmith.armorrepair.item.leather", 1);
		nodes.put("prices.blacksmith.armorrepair.item.gold", 2);
		nodes.put("prices.blacksmith.armorrepair.item.chainmail", 3);
		nodes.put("prices.blacksmith.armorrepair.item.iron", 4);
		nodes.put("prices.blacksmith.armorrepair.item.diamond", 5);
		nodes.put("prices.blacksmith.toolrepair.item-currency-id", 37);
		nodes.put("prices.blacksmith.toolrepair.econplugin.wood", 0.25);
		nodes.put("prices.blacksmith.toolrepair.econplugin.gold", 0.50);
		nodes.put("prices.blacksmith.toolrepair.econplugin.stone", 0.75);
		nodes.put("prices.blacksmith.toolrepair.econplugin.iron", 1);
		nodes.put("prices.blacksmith.toolrepair.econplugin.diamond", 1.25);
		nodes.put("prices.blacksmith.toolrepair.econplugin.misc", 0.50);
		nodes.put("prices.blacksmith.toolrepair.item.wood", 1);
		nodes.put("prices.blacksmith.toolrepair.item.gold", 2);
		nodes.put("prices.blacksmith.toolrepair.item.stone", 3);
		nodes.put("prices.blacksmith.toolrepair.item.iron", 4);
		nodes.put("prices.blacksmith.toolrepair.item.diamond", 5);
		nodes.put("prices.blacksmith.toolrepair.item.misc", 2);
		return nodes;
	}
}