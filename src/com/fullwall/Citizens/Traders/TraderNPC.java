package com.fullwall.Citizens.Traders;

import java.util.Collection;
import java.util.HashMap;

import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderNPC {

	public TraderNPC(HumanNPC npc) {
		this.npc = npc;
	}

	private HumanNPC npc;

	private int balance;

	private boolean unlimited = false;
	private boolean free = true;

	private HashMap<Integer, Buyable> buying = new HashMap<Integer, Buyable>();
	private HashMap<Integer, Sellable> selling = new HashMap<Integer, Sellable>();

	public void addBuyable(Buyable buying) {
		this.buying.put(buying.getBuyingId(), buying);
	}

	public void addSellable(Sellable selling) {
		this.selling.put(selling.getSellingId(), selling);
	}

	public int getBalance() {
		return this.balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public Collection<Buyable> getBuyables() {
		return this.buying.values();
	}

	public Buyable getBuyable(int itemID) {
		if (checkBuyingIntegrity()) {
			if (buying.containsKey(itemID))
				return buying.get(itemID);
		}
		return null;
	}

	public Collection<Sellable> getSellables() {
		return this.selling.values();
	}

	public Sellable getSellable(int itemID) {
		if (checkSellingIntegrity()) {
			if (selling.containsKey(itemID))
				return selling.get(itemID);
		}
		return null;
	}

	public void removeBuyable(int ID) {
		if (checkBuyingIntegrity()) {
			if (buying.containsKey(ID))
				this.buying.remove(ID);
		}
	}

	public void removeSellable(int ID) {
		if (checkSellingIntegrity()) {
			if (selling.containsKey(ID))
				this.selling.remove(ID);
		}
	}

	public boolean isBuyable(int itemID) {
		if (checkBuyingIntegrity()) {
			if (buying.containsKey(itemID))
				return true;

		}
		return false;
	}

	public boolean isSellable(int itemID) {
		if (checkSellingIntegrity()) {
			if (selling.containsKey(itemID))
				return true;
		}
		return false;
	}

	public boolean checkBuyingIntegrity() {
		if (this.buying == null || this.buying.isEmpty())
			return false;
		return true;
	}

	public boolean checkSellingIntegrity() {
		if (this.selling == null || this.selling.isEmpty())
			return false;
		return true;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public boolean isFree() {
		return this.free;
	}

	public void setUnlimited(boolean unlimited) {
		this.unlimited = unlimited;
	}

	public boolean isUnlimited() {
		return this.unlimited;
	}
}
