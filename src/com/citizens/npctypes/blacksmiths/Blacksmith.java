package com.citizens.npctypes.blacksmiths;

import org.bukkit.entity.Player;

import com.citizens.Permission;
import com.citizens.commands.CommandHandler;
import com.citizens.commands.commands.BlacksmithCommands;
import com.citizens.economy.EconomyHandler.Operation;
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
			Operation op = null;
			if (BlacksmithManager.validateTool(player.getItemInHand())) {
				op = Operation.BLACKSMITH_TOOLREPAIR;
			} else if (BlacksmithManager.validateArmor(player.getItemInHand())) {
				op = Operation.BLACKSMITH_ARMORREPAIR;
			}
			if (op != null) {
				BlacksmithManager.buyRepair(player, npc, op);
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
}