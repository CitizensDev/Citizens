package net.citizensnpcs.traders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

public class Trader extends CitizensNPC {

	public static void loadGlobal() {
		Storage storage = UtilityProperties.getConfig();
		for (Object key : storage.getKeys("traders.global-prices")) {
			String path = "traders.global-prices." + key;
			int itemID = storage.getInt(path + ".id", 1);
			int amount = storage.getInt(path + ".amount", 1);
			short data = (short) storage.getInt(path + ".data");
			double price = storage.getDouble(path + ".price");
			boolean selling = !storage.getBoolean(path + ".selling", false);
			if (itemID > 0 && amount > 0) {
				Stockable stock = new Stockable(new ItemStack(itemID, amount,
						data), new ItemPrice(price), selling);
				globalStock.put(stock.createCheck(), stock);
			}
		}
	}

	private boolean unlimited = false;
	private boolean free = true;
	private boolean locked = false;
	private Map<Check, Stockable> stocking = new HashMap<Check, Stockable>();

	private boolean useGlobalBuy, useGlobalSell;

	private static Map<Check, Stockable> globalStock = Maps.newHashMap();

	public static void clearGlobal() {
		globalStock.clear();
	}

	public void addStockable(Stockable s) {
		stocking.put(s.createCheck(), s);
	}

	private Stockable fetchStockable(int itemID, short dataValue,
			boolean selling) {
		Check check = new Check(itemID, dataValue, selling);
		return stocking.containsKey(check) ? stocking.get(check) : (!selling
				&& useGlobalBuy || selling && useGlobalSell) ? globalStock
				.get(check) : null;
	}

	public Stockable getStockable(int itemID, short dataValue, boolean selling) {
		return fetchStockable(itemID, dataValue, selling);
	}

	public Stockable getStockable(Stockable stockable) {
		return getStockable(stockable.getStocking().getTypeId(), stockable
				.getStocking().getDurability(), stockable.isSelling());
	}

	public List<Stockable> getStockables(boolean selling) {
		List<Stockable> stockables = new ArrayList<Stockable>();
		for (Stockable s : stocking.values())
			if (selling == s.isSelling()) {
				stockables.add(s);
			}

		return stockables;
	}

	public Map<Check, Stockable> getStocking() {
		return stocking;
	}

	@Override
	public CitizensNPCType getType() {
		return new TraderType();
	}

	public boolean isFree() {
		return this.free;
	}

	public boolean isLocked() {
		return locked;
	}

	public boolean isStocked(int itemID, short dataValue, boolean selling) {
		return fetchStockable(itemID, dataValue, selling) != null;
	}

	public boolean isStocked(Stockable s) {
		return isStocked(s.getStocking().getTypeId(), s.getStocking()
				.getDurability(), s.isSelling());
	}

	public boolean isUnlimited() {
		return this.unlimited;
	}

	public boolean isUseGlobal(boolean sell) {
		return sell ? useGlobalSell : useGlobalBuy;
	}

	@Override
	public void load(Storage profiles, int UID) {
		unlimited = profiles.getBoolean(UID + ".trader.unlimited");
		locked = profiles.getBoolean(UID + ".trader.locked");
		useGlobalBuy = profiles
				.getBoolean(UID + ".trader.use-global.buy", true);
		useGlobalSell = profiles.getBoolean(UID + ".trader.use-global.sell",
				true);
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

	public void removeStockable(Check check) {
		this.stocking.remove(check);

	}

	public void removeStockable(int ID, short dataValue, boolean selling) {
		removeStockable(new Check(ID, dataValue, selling));
	}

	@Override
	public void save(Storage profiles, int UID) {
		profiles.setBoolean(UID + ".trader.unlimited", unlimited);
		profiles.setBoolean(UID + ".trader.locked", locked);
		profiles.setBoolean(UID + ".trader.use-global.sell", useGlobalSell);
		profiles.setBoolean(UID + ".trader.use-global.buy", useGlobalBuy);
		profiles.setString(UID + ".trader.stock",
				Joiner.on(";").join(stocking.values()));
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void setStocking(Map<Check, Stockable> stocking) {
		this.stocking = stocking;
	}

	public void setUnlimited(boolean unlimited) {
		this.unlimited = unlimited;
	}

	public void setUseGlobal(boolean useGlobal, boolean sell) {
		if (sell)
			this.useGlobalSell = useGlobal;
		else
			this.useGlobalBuy = useGlobal;
	}
}