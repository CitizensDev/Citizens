package com.temp.NPCTypes.Blacksmiths;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.temp.Economy.EconomyHandler;
import com.temp.Economy.ItemInterface;
import com.temp.Economy.EconomyHandler.Operation;
import com.temp.Utils.StringUtils;
import com.temp.resources.redecouverte.NPClib.HumanNPC;

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
				|| id == 346;
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
	public static void buyItemRepair(Player player, HumanNPC npc, Operation op) {
		if (!EconomyHandler.useEconomy()
				|| EconomyHandler.canBuyBlacksmith(player, op)) {
			ItemStack item = player.getItemInHand();
			if (item.getDurability() > 0) {
				double paid = EconomyHandler.payBlacksmith(op, player);
				if (paid > 0) {
					item.setDurability((short) 0);
					player.setItemInHand(item);
					String msg = StringUtils.wrap(npc.getStrippedName())
							+ " has repaired your item for ";
					if (EconomyHandler.useEcoPlugin()) {
						msg += StringUtils.wrap(EconomyHandler.getPaymentType(
								op, "" + paid));
					} else {
						msg += StringUtils.wrap(ItemInterface
								.getBlacksmithCurrency(player, op));
					}
					msg += ChatColor.GREEN + ".";
					player.sendMessage(msg);
				}
			} else {
				player.sendMessage(ChatColor.RED
						+ "Your item is already fully repaired.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(ChatColor.RED
					+ "You do not have enough to repair that.");
		}
	}
}