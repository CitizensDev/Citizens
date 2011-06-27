package com.citizens.Interfaces;

import org.bukkit.entity.Player;

import com.citizens.resources.redecouverte.NPClib.HumanNPC;

public interface NPCPurchaser {
	public boolean canBuy(Player player, String type);

	public double pay(Player player, String type);

	public String getPaidMessage(Player player, HumanNPC npc, double paid,
			String type);

	public String getNoMoneyMessage(Player player, HumanNPC npc, String type);
}
