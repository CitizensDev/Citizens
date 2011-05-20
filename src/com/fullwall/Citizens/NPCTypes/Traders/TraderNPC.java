package com.fullwall.Citizens.NPCTypes.Traders;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderNPC implements Toggleable {
	/**
	 * Holds information about a trader's stocked items, balance and whether it
	 * is free/unlimited.
	 * 
	 * @param npc
	 */
	public TraderNPC(HumanNPC npc) {
		this.npc = npc;
	}

	private HumanNPC npc;

	private boolean unlimited = false;
	private boolean free = true;

	private ConcurrentHashMap<Check, Stockable> stocking = new ConcurrentHashMap<Check, Stockable>();

	public ConcurrentHashMap<Check, Stockable> getStocking() {
		return stocking;
	}

	public void setStocking(ConcurrentHashMap<Check, Stockable> stocking) {
		this.stocking = stocking;
	}

	public void addStockable(Stockable s) {
		stocking.put(new Check(s.getStockingId(), s.isSelling()), s);
	}

	public Stockable getStockable(int itemID, boolean selling) {
		if (checkStockingIntegrity()) {
			if (stocking.get(new Check(itemID, selling)) != null)
				return stocking.get(new Check(itemID, selling));
		}
		return null;
	}

	public ArrayList<Stockable> getStockables(int itemID, boolean selling) {
		ArrayList<Stockable> stockables = new ArrayList<Stockable>();
		if (checkStockingIntegrity()) {
			for (Stockable s : stocking.values())
				if (itemID == s.getStockingId() && selling == s.isSelling())
					stockables.add(s);
		}
		return stockables;
	}

	public ArrayList<Stockable> getStockables(boolean selling) {
		ArrayList<Stockable> stockables = new ArrayList<Stockable>();
		if (checkStockingIntegrity()) {
			for (Stockable s : stocking.values())
				if (selling == s.isSelling())
					stockables.add(s);
		}
		return stockables;
	}

	public void removeStockable(int ID, boolean selling) {
		if (checkStockingIntegrity()) {
			if (stocking.get(new Check(ID, selling)) != null) {
				stocking.remove(new Check(ID, selling));
			}
		}

	}

	public boolean isStocked(int itemID, boolean selling, MaterialData data) {
		if (checkStockingIntegrity()) {
			if (stocking.get(new Check(itemID, selling)) != null) {
				if (checkData(stocking.get(new Check(itemID, selling)), data)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isStocked(Stockable s) {
		return isStocked(s.getStockingId(), s.isSelling(), s.getStocking()
				.getData());
	}

	private boolean checkData(Stockable stockable, MaterialData second) {
		MaterialData first = stockable.getStocking().getData();
		int data = 0;
		int data2 = 0;
		if (first != null)
			data = first.getData();
		if (second != null)
			data2 = second.getData();
		if (data == data2)
			return true;
		return false;
	}

	public boolean checkStockingIntegrity() {
		if (this.stocking == null || this.stocking.isEmpty())
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

	@Override
	public void toggle() {
		npc.setTrader(!npc.isTrader());
	}

	@Override
	public boolean getToggle() {
		return npc.isTrader();
	}

	@Override
	public String getName() {
		return npc.getStrippedName();
	}

	@Override
	public String getType() {
		return "trader";
	}

	@Override
	public void saveState() {
		PropertyManager.get(getType()).saveState(npc);
	}

	@Override
	public void registerState() {
		PropertyManager.get(getType()).register(npc);
	}
}
