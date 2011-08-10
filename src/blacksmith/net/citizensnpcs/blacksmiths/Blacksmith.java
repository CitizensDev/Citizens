package net.citizensnpcs.blacksmiths;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;

import org.bukkit.entity.Player;

public class Blacksmith extends CitizensNPC {

	@Override
	public String getType() {
		return "blacksmith";
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (PermissionManager.generic(player, "citizens.blacksmith.use.repair")) {
			String repairType = "";
			if (InventoryUtils.isTool(player.getItemInHand())) {
				repairType = "blacksmith-toolrepair";
			} else if (InventoryUtils.isArmor(player.getItemInHand())) {
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