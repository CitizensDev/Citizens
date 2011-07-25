package com.citizens.economy;

import org.bukkit.entity.Player;

public class EconomyOperation {
	private String path;
	private String npcType;
	private int itemCurrencyID;
	private int itemPrice;
	private double econPluginPrice;

	public EconomyOperation(String path, String npcType, int itemCurrencyID,
			int itemPrice, double econPluginPrice) {
		this.path = "prices." + path;
		this.npcType = npcType;
		this.itemCurrencyID = itemCurrencyID;
		this.itemPrice = itemPrice;
		this.econPluginPrice = econPluginPrice;
	}

	public String getNPCType() {
		return this.npcType;
	}

	public String getPath() {
		return this.path;
	}

	public int getItemCurrencyID() {
		return this.itemCurrencyID;
	}

	public int getItemPrice() {
		return this.itemPrice;
	}

	public double getEconPluginPrice() {
		return this.econPluginPrice;
	}

	public boolean isFree() {
		if (EconomyManager.useEconomy()) {
			if (EconomyManager.useEconPlugin()) {
				return getEconPluginPrice() <= 0;
			}
			return getItemPrice() <= 0;
		}
		return true;
	}

	public boolean canBuy(Player player) {
		if (EconomyManager.useEconomy()) {
			if (EconomyManager.useEconPlugin()) {
				return ServerEconomyInterface
						.hasEnough(player, econPluginPrice);
			} else {
				return ItemInterface.hasEnough(player, getItemCurrencyID(),
						getItemPrice());
			}
		}
		return true;
	}

	public double pay(Player player) {
		if (EconomyManager.useEconomy()) {
			if (EconomyManager.useEconPlugin()) {
				return ServerEconomyInterface.pay(player, getEconPluginPrice());
			} else {
				return ItemInterface.pay(player, getItemCurrencyID(),
						getItemPrice());
			}
		}
		return 0;
	}
}
