package com.temp.NPCTypes.Blacksmiths;

import org.bukkit.entity.Player;

import com.temp.Permission;
import com.temp.Economy.EconomyHandler.Operation;
import com.temp.Interfaces.Clickable;
import com.temp.Interfaces.Toggleable;
import com.temp.Utils.MessageUtils;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithNPC extends Toggleable implements Clickable {

	/**
	 * Blacksmith NPC object
	 * 
	 * @param npc
	 */
	public BlacksmithNPC(HumanNPC npc) {
		super(npc);
	}

	@Override
	public String getType() {
		return "blacksmith";
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (Permission.canUse(player, npc, getType())) {
			Operation op = null;
			if (BlacksmithManager.validateTool(player.getItemInHand())) {
				op = Operation.BLACKSMITH_TOOLREPAIR;
			} else if (BlacksmithManager.validateArmor(player.getItemInHand())) {
				op = Operation.BLACKSMITH_ARMORREPAIR;
			}
			if (op != null) {
				BlacksmithManager.buyItemRepair(player, npc, op);
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}
}