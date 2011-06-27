package com.citizens.Implementations;

import org.bukkit.entity.Player;

import com.citizens.Economy.EconomyHandler;
import com.citizens.Economy.EconomyHandler.Operation;
import com.citizens.Interfaces.NPCPurchaser;
import com.citizens.Utils.MessageUtils;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;

public class OperationPurchaser implements NPCPurchaser {
	private Operation getOperation(String type) {
		return Operation.valueOf(type.toUpperCase() + "_CREATION");
	}

	@Override
	public boolean canBuy(Player player, String type) {
		return !EconomyHandler.useEconomy()
				|| EconomyHandler.canBuy(getOperation(type), player);
	}

	@Override
	public double pay(Player player, String type) {
		if (EconomyHandler.useEconomy())
			return EconomyHandler.pay(getOperation(type), player);
		return 0;
	}

	@Override
	public String getPaidMessage(Player player, HumanNPC npc, double paid,
			String type) {
		if (EconomyHandler.useEconomy())
			return MessageUtils.getPaidMessage(getOperation(type), paid,
					npc.getName(), type, true);
		return "";
	}

	@Override
	public String getNoMoneyMessage(Player player, HumanNPC npc, String type) {
		return MessageUtils.getNoMoneyMessage(getOperation(type), player);
	}

}
