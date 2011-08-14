package net.citizensnpcs.traders;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.PermissionManager;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npcs.NPCManager;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.traders.TraderManager.TraderMode;
import net.citizensnpcs.utils.InventoryUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Trader extends CitizensNPC {
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
		stocking.put(
				new Check(s.getStockingId(), s.getStockingDataValue(), s
						.isSelling()), s);
	}

	public Stockable fetchStockable(int itemID, short dataValue, boolean selling) {
		return stocking.get(new Check(itemID, dataValue, selling));
	}

	public Stockable getStockable(int itemID, short dataValue, boolean selling) {
		if (checkStockingIntegrity()) {
			return fetchStockable(itemID, dataValue, selling);
		}
		return null;
	}

	public Stockable getStockable(Stockable s) {
		return getStockable(s.getStockingId(), s.getStockingDataValue(),
				s.isSelling());
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

	public void removeStockable(int ID, short dataValue, boolean selling) {
		if (checkStockingIntegrity()) {
			stocking.remove(new Check(ID, dataValue, selling));
		}
	}

	public void removeStockable(Check check) {
		this.stocking.remove(check);

	}

	public boolean isStocked(int itemID, short dataValue, boolean selling) {
		if (checkStockingIntegrity()) {
			if (fetchStockable(itemID, dataValue, selling) != null) {
				return true;
			}
		}
		return false;
	}

	public boolean isStocked(Stockable s) {
		return isStocked(s.getStockingId(), s.getStockingDataValue(),
				s.isSelling());
	}

	/*
	 * private boolean checkData(Stockable stockable, MaterialData second) {
	 * MaterialData first = stockable.getStocking().getData(); int data = 0; int
	 * data2 = 0; if (first != null) { data = first.getData(); } if (second !=
	 * null) { data2 = second.getData(); } return data == data2; }
	 */
	public boolean checkStockingIntegrity() {
		return !(this.stocking == null || this.stocking.isEmpty());
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
	public String getType() {
		return "trader";
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		Trader trader = npc.getType("trader");
		if (trader.isFree()) {
			TraderMode mode;
			if (NPCManager.validateOwnership(player, npc.getUID(), false)) {
				if (!PermissionManager.generic(player,
						"citizens.trader.modify.stock")) {
					return;
				}
				mode = TraderMode.STOCK;
			} else if (trader.isUnlimited()) {
				mode = TraderMode.INFINITE;
				if (!PermissionManager.generic(player,
						"citizens.trader.use.trade")) {
					return;
				}
			} else {
				mode = TraderMode.NORMAL;
				if (!PermissionManager.generic(player,
						"citizens.trader.use.trade")) {
					return;
				}
			}
			TraderTask task = new TraderTask(npc, player, mode);
			int id = Bukkit.getServer().getScheduler()
					.scheduleSyncRepeatingTask(Citizens.plugin, task, 2, 1);
			TraderManager.tasks.add(id);
			task.addID(id);
			trader.setFree(false);
			InventoryUtils.showInventory(npc, player);
		} else {
			player.sendMessage(ChatColor.RED
					+ "Only one person may be served at a time!");
		}
	}

	@Override
	public Properties getProperties() {
		return new TraderProperties();
	}

	@Override
	public CommandHandler getCommands() {
		return new TraderCommands();
	}
}