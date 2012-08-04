package net.citizensnpcs.blacksmiths;

import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;

import org.bukkit.entity.Player;

public class Blacksmith extends CitizensNPC {

	@Override
	public CitizensNPCType getType() {
		return new BlacksmithType();
	}

	@Override
	public void load(Storage profiles, int UID) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (PermissionManager.hasPermission(player,
				"citizens.blacksmith.use.repair")) {
			String repairType = "";
			if (InventoryUtils.isTool(player.getItemInHand().getTypeId())) {
				repairType = "toolrepair";
			} else if (InventoryUtils.isArmor(player.getItemInHand()
					.getTypeId())) {
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
	public void save(Storage profiles, int UID) {
	}
}