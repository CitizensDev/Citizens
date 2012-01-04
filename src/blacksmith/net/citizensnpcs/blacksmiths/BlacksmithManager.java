package net.citizensnpcs.blacksmiths;

import net.citizensnpcs.economy.Account;
import net.citizensnpcs.economy.Economy;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlacksmithManager {
	// Get the price for a blacksmith operation
	public static double getBlacksmithPrice(Player player, String repairType) {
		ItemStack item = player.getItemInHand();
		short maxDurability = Material.getMaterial(item.getTypeId())
				.getMaxDurability();
		return (maxDurability - (maxDurability - item.getDurability()))
				* UtilityProperties.getPrice("blacksmith." + repairType + "."
						+ getNodeExtension(item));
	}

	private static String getNodeExtension(ItemStack item) {
		int id = item.getTypeId();
		if (id == 259 || id == 346 || id == 359) {
			return "misc";
		} else if ((id >= 268 && id <= 271) || id == 290) {
			return "wood";
		} else if ((id >= 283 && id <= 286) || id == 294
				|| (id >= 314 && id <= 317)) {
			return "gold";
		} else if ((id >= 272 && id <= 275) || id == 291) {
			return "stone";
		} else if ((id >= 256 && id <= 258) || id == 267 || id == 292
				|| (id >= 306 && id <= 309)) {
			return "iron";
		} else if ((id >= 276 && id <= 279) || id == 293
				|| (id >= 310 && id <= 313)) {
			return "diamond";
		} else if ((id >= 298 && id <= 301)) {
			return "leather";
		} else if ((id >= 302 && id <= 305)) {
			return "chainmail";
		}
		return "misc";
	}

	// Repair an item
	@SuppressWarnings("deprecation")
	public static void repairItem(Player player, HumanNPC npc, String repairType) {
		ItemStack item = player.getItemInHand();
		String itemName = MessageUtils.getMaterialName(item.getTypeId());
		if (item.getDurability() == 0) {
			player.sendMessage(ChatColor.GRAY + "Your "
					+ StringUtils.wrap(itemName, ChatColor.GRAY)
					+ " is already fully repaired.");
			return;
		}
		double price = getBlacksmithPrice(player, repairType);
		StringBuilder repairMessage = new StringBuilder(StringUtils.wrap(npc
				.getName()));
		repairMessage.append(" has repaired your ");
		repairMessage.append(StringUtils.wrap(itemName) + " for ");
		repairMessage.append(StringUtils.wrap(Economy.format(price)));

		Account account = Economy.getAccount(player);
		if (!account.hasEnough(price)) {
			Messaging.sendError(player,
					"You don't have enough money to repair your " + itemName
							+ ".");
			return;
		}
		account.subtract(price);

		player.sendMessage(repairMessage.append(".").toString());
		item.setDurability((short) 0);
		player.setItemInHand(item);
		player.updateInventory();
	}
}