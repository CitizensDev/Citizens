package com.citizens.npctypes.blacksmiths;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.citizens.economy.EconomyManager;
import com.citizens.economy.EconomyOperation;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.MessageUtils;
import com.citizens.utils.StringUtils;

public class BlacksmithManager {

	/**
	 * Validate that an item has a durability and can be repaired
	 * 
	 * @param item
	 * @return
	 */
	public static boolean validateTool(ItemStack item) {
		int id = item.getTypeId();
		return (id >= 256 && id <= 259) || (id >= 267 && id <= 279)
				|| (id >= 283 && id <= 286) || (id >= 290 && id <= 294)
				|| id == 346 || id == 359;
	}

	/**
	 * Validate that the item to repair is armor
	 * 
	 * @param armor
	 * @return
	 */
	public static boolean validateArmor(ItemStack armor) {
		int id = armor.getTypeId();
		return id >= 298 && id <= 317;
	}

	/**
	 * Purchase an item repair
	 * 
	 * @param player
	 * @param npc
	 * @param op
	 */
	public static void buyRepair(Player player, HumanNPC npc,
			EconomyOperation op) {
		if (!EconomyManager.useEconomy() || op.canBuy(player)) {
			ItemStack item = player.getItemInHand();
			if (item.getDurability() > 0) {
				double paid = op.pay(player);
				if (paid > 0 || op.isFree()) {
					player.sendMessage(StringUtils.wrap(npc.getStrippedName())
							+ " has repaired your item for "
							+ StringUtils.wrap(EconomyManager.getPaymentType(
									player, op, "" + paid)) + ".");
					item.setDurability((short) 0);
					player.setItemInHand(item);
				}
			} else {
				player.sendMessage(ChatColor.RED + "Your "
						+ MessageUtils.getMaterialName(item.getTypeId())
						+ ChatColor.RED + " is already fully repaired.");
			}
		} else if (EconomyManager.useEconomy()) {
			player.sendMessage(ChatColor.RED
					+ "You do not have enough to repair that.");
		}
	}
}