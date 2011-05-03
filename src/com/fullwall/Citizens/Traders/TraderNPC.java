package com.fullwall.Citizens.Traders;

import java.util.HashMap;

import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Utils.TraderPropertyPool;
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

	private HashMap<Check, Stockable> stocking = new HashMap<Check, Stockable>();

	public int getBalance() {
		return this.balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public HashMap<Check, Stockable> getStocking() {
		return stocking;
	}

	public void setStocking(HashMap<Check, Stockable> stocking) {
		this.stocking = stocking;
	}

	public void addStockable(Stockable stocking) {
		this.getStocking().put(
				new Check(stocking.getStockingId(), stocking.isSelling()),
				stocking);
		TraderPropertyPool.saveStockables(this.getStocking());
	}

	public Stockable getStockable(int itemID, boolean selling) {
		if (checkStockingIntegrity()) {
			if (getStocking().get(new Check(itemID, selling)) != null)
				return getStocking().get(new Check(itemID, selling));
		}
		return null;
	}

	public void removeStockable(int ID, boolean selling) {
		if (checkStockingIntegrity()) {
			if (getStocking().get(new Check(ID, selling)) != null)
				this.getStocking().remove(new Check(ID, selling));
		}
		TraderPropertyPool.saveStockables(getStocking());
	}

	public boolean isStockable(int itemID, MaterialData data, boolean selling) {
		if (checkStockingIntegrity()) {
			if (getStocking().get(new Check(itemID, selling)) != null) {
				if (checkData(getStocking().get(new Check(itemID, selling))
						.getStocking().getData(), data))
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

	public boolean checkStockingIntegrity() {
		if (this.getStocking() == null || this.getStocking().isEmpty())
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
