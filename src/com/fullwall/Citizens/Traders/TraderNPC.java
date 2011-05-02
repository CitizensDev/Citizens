package com.fullwall.Citizens.Traders;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.Citizens;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderNPC {

	public TraderNPC(HumanNPC npc) {
		this.npc = npc;
	}

	@SuppressWarnings("unused")
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
			if (buying.get(itemID) != null)
				return buying.get(itemID);
		}
		return null;
	}

	public Collection<Sellable> getSellables() {
		return this.selling.values();
	}

	public Sellable getSellable(int itemID) {
		if (checkSellingIntegrity()) {
			if (selling.get(itemID) != null)
				return selling.get(itemID);
		}
		return null;
	}

	public void removeBuyable(int ID) {
		if (checkBuyingIntegrity()) {
			if (buying.get(ID) != null)
				this.buying.remove(ID);
		}
	}

	public void removeSellable(int ID) {
		if (checkSellingIntegrity()) {
			if (selling.get(ID) != null)
				this.selling.remove(ID);
		}
	}

	public boolean isBuyable(int itemID, MaterialData data) {
		if (checkBuyingIntegrity()) {
			if (buying.get(itemID) != null) {
				if (checkData(buying.get(itemID).getBuying().getData(), data))
					return true;
			}
		}
		return false;
	}

	public boolean isSellable(int itemID, MaterialData data) {
		if (checkSellingIntegrity()) {
			if (selling.get(itemID) != null) {
				if (checkData(selling.get(itemID).getSelling().getData(), data))
					return true;
			}
		}
		return false;
	}

	private boolean checkData(MaterialData first, MaterialData second) {
		int data = 0;
		int data2 = 0;
		if (first != null)
			data = first.getData();
		if (second != null)
			data2 = second.getData();
		if (data == data2 || data == Citizens.MAGIC_DATA_VALUE)
			return true;
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
