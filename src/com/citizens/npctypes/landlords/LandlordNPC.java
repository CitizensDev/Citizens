package com.citizens.npctypes.landlords;

import org.bukkit.entity.Player;

import com.citizens.npctypes.interfaces.Clickable;
import com.citizens.npctypes.interfaces.Toggleable;
import com.citizens.resources.npclib.HumanNPC;

public class LandlordNPC extends Toggleable implements Clickable {

	public LandlordNPC(HumanNPC npc) {
		super(npc);
	}

	@Override
	public String getType() {
		return "landlord";
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
	}
}