package com.citizens.npctypes;

import org.bukkit.entity.Player;

import com.citizens.resources.npclib.HumanNPC;

public interface Purchaser {
	public boolean hasPermission(Player player, String type);

	public boolean canBuy(Player player, String type);

	public double pay(Player player, String type);

	public String getPaidMessage(Player player, HumanNPC npc, double paid,
			String type);

	public String getNoMoneyMessage(Player player, HumanNPC npc, String type);

	public String getNoPermissionsMessage(Player player, String type);
}