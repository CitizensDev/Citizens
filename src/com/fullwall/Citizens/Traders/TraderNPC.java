package com.fullwall.Citizens.Traders;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.Utils.TraderPropertyPool;
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
		TraderPropertyPool.saveStockables(npc.getUID(), stocking);
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
		Stockable s = null;
		if (checkStockingIntegrity()) {
			if (stocking.get(new Check(ID, selling)) != null) {
				s = getStocking().get(new Check(ID, selling));
				TraderPropertyPool.removeStockable(npc.getUID(), s);
				stocking.remove(new Check(ID, selling));
			}
		}

	}

	public boolean isStocked(int itemID, MaterialData data, boolean selling) {
		if (checkStockingIntegrity()) {
			if (stocking.get(new Check(itemID, selling)) != null) {
				if (checkData(stocking.get(new Check(itemID, selling))
						.getStocking().getData(), data)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isStocked(Stockable s) {
		return isStocked(s.getStockingId(), s.getPrice().getMaterialData(),
				s.isSelling());
	}

	private boolean checkData(MaterialData first, MaterialData second) {
		int data = 0;
		int data2 = 0;
		if (first != null)
			data = first.getData();
		if (second != null)
			data2 = second.getData();
		Citizens.log.info("" + data + " " + data2);
		if (data == data2 || data == Citizens.MAGIC_DATA_VALUE)
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
		TraderPropertyPool.saveState(npc);
	}

	@Override
	public void registerState() {
		TraderPropertyPool.saveTrader(npc.getUID(), true);
	}
}
