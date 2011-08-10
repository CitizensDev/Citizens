package net.citizensnpcs.blacksmiths;

import net.citizensnpcs.economy.EconomyManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlacksmithManager {

	/**
	 * Purchase an item repair
	 * 
	 * @param player
	 * @param npc
	 * @param op
	 */
	public static void buyRepair(Player player, HumanNPC npc, String repairType) {
		if (EconomyManager.hasEnough(player,
				getBlacksmithPrice(player, repairType))
				|| !EconomyManager.useEconPlugin()) {
			ItemStack item = player.getItemInHand();
			if (item.getDurability() > 0) {
				double paid = EconomyManager.pay(player,
						getBlacksmithPrice(player, repairType));
				if (paid >= 0) {
					player.sendMessage(StringUtils.wrap(npc.getStrippedName())
							+ " has repaired your item for "
							+ StringUtils.wrap(EconomyManager.format(paid))
							+ ".");
					item.setDurability((short) 0);
					player.setItemInHand(item);
				}
			} else {
				player.sendMessage(ChatColor.RED + "Your "
						+ MessageUtils.getMaterialName(item.getTypeId())
						+ ChatColor.RED + " is already fully repaired.");
			}
		} else {
			player.sendMessage(ChatColor.RED
					+ "You do not have enough to repair that.");
		}
	}

	/**
	 * Get the price for a blacksmith operation
	 * 
	 * @param player
	 * @param repairType
	 * @return
	 */
	public static double getBlacksmithPrice(Player player, String repairType) {
		ItemStack item = player.getItemInHand();
		short maxDurability = Material.getMaterial(item.getTypeId())
				.getMaxDurability();
		double price = (maxDurability - (maxDurability - item.getDurability()))
				* UtilityProperties.getPrice("blacksmith."
						+ repairType
						+ "."
						+ EconomyManager.materialAddendums[EconomyManager
								.getBlacksmithIndex(item)]);
		return price;
	}
}