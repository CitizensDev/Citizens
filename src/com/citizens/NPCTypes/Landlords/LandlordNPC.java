package com.citizens.NPCTypes.Landlords;

import org.bukkit.entity.Player;

import com.citizens.Interfaces.Clickable;
import com.citizens.Interfaces.Toggleable;
import com.citizens.Resources.NPClib.HumanNPC;

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