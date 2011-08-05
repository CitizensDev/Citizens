package net.citizensnpcs.npctypes.blacksmiths;

import net.citizensnpcs.Permission;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.commands.commands.BlacksmithCommands;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.properties.BlacksmithProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.MessageUtils;

import org.bukkit.entity.Player;

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
	public Properties getProperties() {
		return new BlacksmithProperties();
	}

	@Override
	public CommandHandler getCommands() {
		return new BlacksmithCommands();
	}
}