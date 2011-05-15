package com.fullwall.Citizens.Blacksmiths;

import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Utils.BlacksmithPropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class BlacksmithNPC implements Toggleable {
	private HumanNPC npc;

	/**
	 * NPC Blacksmith object
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
		BlacksmithPropertyPool.saveState(npc);
	}

	@Override
	public void registerState() {
		BlacksmithPropertyPool.saveBlacksmith(npc.getUID(), true);
	}
	
	/**
	 * Validate that an item has a durability and can be repaired
	 * 
	 * @param item
	 * @return
	 */
	public boolean validateItemToRepair(ItemStack item) {
		int id = item.getTypeId();
		if (id == 256 || id == 257 || id == 258 || id == 259 || id == 267
				|| id == 268 || id == 269 || id == 270 || id == 271
				|| id == 272 || id == 273 || id == 274 || id == 275
				|| id == 276 || id == 277 || id == 278 || id == 279
				|| id == 283 || id == 284 || id == 285 || id == 286
				|| id == 290 || id == 291 || id == 292 || id == 293
				|| id == 294 || id == 346 || (id >= 298 && id <= 317)) {
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
	public boolean validateArmorToRepair(ItemStack armor) {
		int id = armor.getTypeId();
		if (id >= 298 && id <= 317) {
			return true;
		} else {
			return false;
		}
	}
}