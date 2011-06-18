package com.fullwall.Citizens.NPCTypes.Blacksmiths;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Economy.ItemInterface;
import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithNPC implements Toggleable, Clickable {
	private final HumanNPC npc;

	/**
	 * Blacksmith NPC object
	 * 
	 * @param npc
	 */
	public BlacksmithNPC(HumanNPC npc) {
		this.npc = npc;
	}

	@Override
	public void toggle() {
		npc.setBlacksmith(!npc.isBlacksmith());
	}

	@Override
	public boolean getToggle() {
		return npc.isBlacksmith();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "blacksmith";
	}

	@Override
	public void saveState() {
		PropertyManager.get("blacksmith").saveState(npc);
	}

	@Override
	public void register() {
		PropertyManager.get("blacksmith").register(npc);
	}

	/**
	 * Validate that an item has a durability and can be repaired
	 * 
	 * @param item
	 * @return
	 */
	public boolean validateTool(ItemStack item) {
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
	public boolean validateArmor(ItemStack armor) {
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
	private void buyItemRepair(Player player, HumanNPC npc, ItemStack item,
			Operation op) {
		if (!EconomyHandler.useEconomy()
				|| EconomyHandler.canBuyBlacksmith(player, op)) {
			if (item.getDurability() > 0) {
				double paid = EconomyHandler.payBlacksmith(op, player);
				if (paid > 0) {
					item.setDurability((short) 0);
					player.setItemInHand(item);
					String msg = StringUtils.wrap(npc.getStrippedName())
							+ " has repaired your item for ";
					if (EconomyHandler.useIconomy()) {
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

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (Permission.canUse(player, npc, getType())) {
			Operation op = null;
			if (validateTool(player.getItemInHand())) {
				op = Operation.BLACKSMITH_TOOLREPAIR;
			} else if (validateArmor(player.getItemInHand())) {
				op = Operation.BLACKSMITH_ARMORREPAIR;
			}
			if (op != null) {
				npc.getBlacksmith().buyItemRepair(player, npc,
						player.getItemInHand(), op);
			}
		} else {
			player.sendMessage(MessageUtils.noPermissionsMessage);
		}
	}
}