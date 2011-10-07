package net.citizensnpcs.traders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Trader extends CitizensNPC {
	private boolean unlimited = false;
	private boolean free = true;
	private Map<Check, Stockable> stocking = new ConcurrentHashMap<Check, Stockable>();

	public Map<Check, Stockable> getStocking() {
		return stocking;
	}

	public void setStocking(Map<Check, Stockable> stocking) {
		this.stocking = stocking;
	}

	public void addStockable(Stockable s) {
		stocking.put(
				new Check(s.getStockingId(), s.getStockingDataValue(), s
						.isSelling()), s);
	}

	public Stockable fetchStockable(int itemID, short dataValue, boolean selling) {
		return stocking.get(new Check(itemID, dataValue, selling));
	}

	public Stockable getStockable(int itemID, short dataValue, boolean selling) {
		return fetchStockable(itemID, dataValue, selling);
	}

	public Stockable getStockable(Stockable s) {
		return getStockable(s.getStockingId(), s.getStockingDataValue(),
				s.isSelling());
	}

	public List<Stockable> getStockables(int itemID, boolean selling) {
		List<Stockable> stockables = new ArrayList<Stockable>();
		for (Stockable s : stocking.values())
			if (itemID == s.getStockingId() && selling == s.isSelling()) {
				stockables.add(s);
			}
		return stockables;
	}

	public List<Stockable> getStockables(boolean selling) {
		List<Stockable> stockables = new ArrayList<Stockable>();
		for (Stockable s : stocking.values())
			if (selling == s.isSelling()) {
				stockables.add(s);
			}

		return stockables;
	}

	public void removeStockable(int ID, short dataValue, boolean selling) {
		stocking.remove(new Check(ID, dataValue, selling));

	}

	public void removeStockable(Check check) {
		this.stocking.remove(check);

	}

	public boolean isStocked(int itemID, short dataValue, boolean selling) {
		return fetchStockable(itemID, dataValue, selling) != null;
	}

	public boolean isStocked(Stockable s) {
		return isStocked(s.getStockingId(), s.getStockingDataValue(),
				s.isSelling());
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
	public void onRightClick(Player player, HumanNPC npc) {
		Trader trader = npc.getType("trader");
		if (trader.isFree()) {
			TraderMode mode;
			if (NPCManager.isOwner(player, npc.getUID())) {
				if (!PermissionManager.hasPermission(player,
						"citizens.trader.modify.stock")) {
					return;
				}
				mode = TraderMode.STOCK;
			} else if (trader.isUnlimited()) {
				mode = TraderMode.INFINITE;
				if (!PermissionManager.hasPermission(player,
						"citizens.trader.use.trade")) {
					return;
				}
			} else {
				mode = TraderMode.NORMAL;
				if (!PermissionManager.hasPermission(player,
						"citizens.trader.use.trade")) {
					return;
				}
			}
			TraderTask task = new TraderTask(npc, player, mode);
			int id = Bukkit.getServer().getScheduler()
					.scheduleSyncRepeatingTask(Citizens.plugin, task, 0, 1);
			task.addID(id);
			trader.setFree(false);
			InventoryUtils.showInventory(npc, player);
		} else {
			Messaging.sendError(player,
					"Only one person may be served at a time!");
		}
	}

	@Override
	public CitizensNPCType getType() {
		return new TraderType();
	}
}