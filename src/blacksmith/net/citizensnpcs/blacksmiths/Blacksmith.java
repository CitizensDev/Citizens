package net.citizensnpcs.blacksmiths;

import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.api.Properties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;

import org.bukkit.entity.Player;

public class Blacksmith extends CitizensNPC {
	
	@Override
	public String getVersion() {
		return "1.1";
	}
	
	@Override
	public String getType() {
		return "blacksmith";
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (PermissionManager.generic(player, "citizens.blacksmith.use.repair")) {
			String repairType = "";
			if (InventoryUtils.isTool(player.getItemInHand())) {
				repairType = "toolrepair";
			} else if (InventoryUtils.isArmor(player.getItemInHand())) {
				repairType = "armorrepair";
			}
			if (!repairType.isEmpty()) {
				BlacksmithManager.repairItem(player, npc, repairType);
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}

	@Override
	public Properties getProperties() {
		return BlacksmithProperties.INSTANCE;
	}

	@Override
	public CommandHandler getCommands() {
		return BlacksmithCommands.INSTANCE;
	}
}