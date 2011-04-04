package com.fullwall.Citizens.Traders;

import java.util.ArrayList;

import com.fullwall.resources.redecouverte.NPClib.CraftNPC;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderNPC extends HumanNPC {

	private ArrayList<Buyable> buying = new ArrayList<Buyable>();
	private ArrayList<Sellable> selling = new ArrayList<Sellable>();

	public TraderNPC(CraftNPC entity, int UID, String name) {
		super(entity, UID, name);
	}

	public void addBuyable(Buyable buying) {
		this.buying.add(buying);
	}

	public void addSellable(Sellable selling) {
		this.selling.add(selling);
	}

	public ArrayList<Buyable> getBuyables() {
		return this.buying;
	}

	public ArrayList<Sellable> getSellables() {
		return this.selling;
	}

	public void removeBuyable(int index) {
		this.buying.remove(index);
	}

	public void removeSellable(int index) {
		this.selling.remove(index);
	}

	public boolean isBuyable(int itemID) {
		for (Buyable b : buying) {

		}
		return false;
	}

	public boolean isSellable(int itemID) {
		for (Sellable s : selling) {

		}
		return false;
	}
}
