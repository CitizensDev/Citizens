package com.fullwall.Citizens.NPCTypes.Blacksmiths;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.ItemInterface;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithNPC implements Toggleable, Clickable {
	private HumanNPC npc;

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
		if (id == 256 || id == 257 || id == 258 || id == 259 || id == 267
				|| id == 268 || id == 269 || id == 270 || id == 271
				|| id == 272 || id == 273 || id == 274 || id == 275
				|| id == 276 || id == 277 || id == 278 || id == 279
				|| id == 283 || id == 284 || id == 285 || id == 286
				|| id == 290 || id == 291 || id == 292 || id == 293
				|| id == 294 || id == 346) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Validate that the item to repair is armor
	 * 
	 * @param armor
	 * @return
	 */
	public boolean validateArmor(ItemStack armor) {
		int id = armor.getTypeId();
		if (id >= 298 && id <= 317) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get type of tool
	 * 
	 * @param id
	 * @return
	 */
	public String getToolType(ItemStack item) {
		String type = "";
		if (validateTool(item)) {
			type = "tool";
		} else if (validateArmor(item)) {
			type = "armor";
		}
		return type;
	}

	/**
	 * Purchase a item repair
	 * 
	 * @param player
	 * @param npc
	 * @param op
	 */
	public void buyItemRepair(Player player, HumanNPC npc, ItemStack item,
			Operation op) {
		if (!EconomyHandler.useEconomy() || EconomyHandler.canBuy(op, player)) {
			if (EconomyHandler.useEconomy()) {
				if (item.getDurability() > 0) {
					double paid = EconomyHandler.payBlacksmith(op, player);
					if (paid > 0) {
						item.setDurability((short) 0);
						player.setItemInHand(item);
						String msg = StringUtils.wrap(npc.getStrippedName())
								+ " has repaired your item for ";
						if (EconomyHandler.useIconomy()) {
							msg += StringUtils.wrap(EconomyHandler
									.getPaymentType(op, "" + paid,
											ChatColor.YELLOW));
						} else {
							msg += StringUtils.wrap(ItemInterface
									.getBlacksmithPrice(player, item, op)
									+ " "
									+ ItemInterface.getCurrencyName(op));
						}
						msg += ChatColor.GREEN + ".";
						player.sendMessage(msg);
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "Your item is already fully repaired.");
				}
			} else {
				player.sendMessage(ChatColor.GRAY
						+ "Your server has not turned economy on for Citizens.");
			}
		} else if (EconomyHandler.useEconomy()) {
			player.sendMessage(MessageUtils.getNoMoneyMessage(op, player));
			return;
		}
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		// TODO Auto-generated method stub
		
	}
}