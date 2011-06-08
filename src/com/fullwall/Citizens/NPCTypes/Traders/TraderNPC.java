package com.fullwall.Citizens.NPCTypes.Traders;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Permission;
import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.Interfaces.Toggleable;
import com.fullwall.Citizens.NPCTypes.Traders.TraderManager.Mode;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.InventoryUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TraderNPC implements Toggleable, Clickable {

	private final HumanNPC npc;

	private boolean unlimited = false;
	private boolean free = true;

	private ConcurrentHashMap<Check, Stockable> stocking = new ConcurrentHashMap<Check, Stockable>();

	/**
	 * Holds information about a trader's stocked items, balance and whether it
	 * is free/unlimited.
	 * 
	 * @param npc
	 */
	public TraderNPC(HumanNPC npc) {
		this.npc = npc;
	}

	public ConcurrentHashMap<Check, Stockable> getStocking() {
		return stocking;
	}

	public void setStocking(ConcurrentHashMap<Check, Stockable> stocking) {
		this.stocking = stocking;
	}

	public void addStockable(Stockable s) {
		stocking.put(new Check(s.getStockingId(), s.isSelling()), s);
	}

	public Stockable fetchStockable(int itemID, boolean selling) {
		return stocking.get(new Check(itemID, selling));
	}

	public Stockable getStockable(int itemID, boolean selling, MaterialData data) {
		if (checkStockingIntegrity()) {
			if (fetchStockable(itemID, selling) != null) {
				Stockable stockable = fetchStockable(itemID, selling);
				if (checkData(stockable, data)) {
					return stockable;
				}
			}
		}
		return null;
	}

	public Stockable getStockable(Stockable s) {
		return getStockable(s.getStocking().getTypeId(), s.isSelling(), s
				.getStocking().getData());
	}

	public ArrayList<Stockable> getStockables(int itemID, boolean selling) {
		ArrayList<Stockable> stockables = new ArrayList<Stockable>();
		if (checkStockingIntegrity()) {
			for (Stockable s : stocking.values())
				if (itemID == s.getStockingId() && selling == s.isSelling()) {
					stockables.add(s);
				}
		}
		return stockables;
	}

	public ArrayList<Stockable> getStockables(boolean selling) {
		ArrayList<Stockable> stockables = new ArrayList<Stockable>();
		if (checkStockingIntegrity()) {
			for (Stockable s : stocking.values())
				if (selling == s.isSelling()) {
					stockables.add(s);
				}
		}
		return stockables;
	}

	public void removeStockable(int ID, boolean selling, MaterialData data) {
		if (checkStockingIntegrity()) {
			if (fetchStockable(ID, selling) != null) {
				if (checkData(fetchStockable(ID, selling), data)) {
					stocking.remove(new Check(ID, selling));
				}
			}
		}

	}

	public boolean isStocked(int itemID, boolean selling, MaterialData data) {
		if (checkStockingIntegrity()) {
			if (fetchStockable(itemID, selling) != null) {
				if (checkData(fetchStockable(itemID, selling), data)) {
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
		if (first != null) {
			data = first.getData();
		}
		if (second != null) {
			data2 = second.getData();
		}
		if (data == data2) {
			return true;
		}
		return false;
	}

	public boolean checkStockingIntegrity() {
		if (this.stocking == null || this.stocking.isEmpty()) {
			return false;
		}
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
	public void register() {
		PropertyManager.get(getType()).register(npc);
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (npc.getTrader().isFree()) {
			Mode mode;
			if (NPCManager.validateOwnership(player, npc.getUID())) {
				if (!Permission.canModify(player, npc, "trader")) {
					return;
				}
				mode = Mode.STOCK;
			} else if (npc.getTrader().isUnlimited()) {
				mode = Mode.INFINITE;
				if (!Permission.canUse(player, npc, "trader")) {
					return;
				}
			} else {
				mode = Mode.NORMAL;
				if (!Permission.canUse(player, npc, "trader")) {
					return;
				}
			}
			TraderTask task = new TraderTask(npc, player, mode);
			int id = Bukkit.getServer().getScheduler()
					.scheduleSyncRepeatingTask(Citizens.plugin, task, 2, 1);
			TraderManager.tasks.add(id);
			task.addID(id);
			npc.getTrader().setFree(false);
			InventoryUtils.showInventory(npc, player);
		} else {
			player.sendMessage(ChatColor.RED
					+ "Only one person may be served at a time!");
		}
	}
}