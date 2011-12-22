package net.citizensnpcs.traders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.properties.DataKey;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

public class Trader extends CitizensNPC {

	public static void loadGlobal() {
		DataKey root = UtilityProperties.getConfig().getKey(
				"traders.global-prices");
		for (DataKey key : root.getSubKeys()) {
			int itemID = key.getInt("id", 1);
			int amount = key.getInt("amount", 1);
			short data = (short) key.getInt("data");
			double price = key.getDouble("price");
			boolean selling = !key.getBoolean("selling", false);
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
	public void load(DataKey root) {
		unlimited = root.getBoolean("unlimited");
		locked = root.getBoolean("locked");
		useGlobalBuy = root.getBoolean("use-global.buy", true);
		useGlobalSell = root.getBoolean("use-global.sell", true);
		Map<Check, Stockable> stockables = new ConcurrentHashMap<Check, Stockable>();
		int i = 0;
		label: for (String s : root.getString("stock").split(";")) {
			if (s.isEmpty()) {
				continue;
			}
			i = 0;
			ItemStack stack = new ItemStack(37);
			ItemPrice price = new ItemPrice(0);
			boolean selling = false;
			for (String main : s.split(",")) {
				switch (i) {
				case 0:
					String[] split = main.split("/");
					stack = new ItemStack(Integer.parseInt(split[0]),
							Integer.parseInt(split[1]),
							Short.parseShort(split[2]));
					break;
				case 1:
					String[] parts = main.split("/");
					if (parts.length == 1 || parts.length == 2) {
						// TODO: remove the second if and else after 1.1
						price = new ItemPrice(Double.parseDouble(parts[0]));
					} else {// Ignore old prices.
						continue label;
					}
					break;
				case 2:
					selling = Boolean.parseBoolean(main);
					break;
				}
				i += 1;
			}
			Stockable stock = new Stockable(stack, price, selling);
			stockables.put(stock.createCheck(), stock);
		}
		this.stocking = stockables;
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (!this.free) {
			Messaging.sendError(player,
					"Only one person may be served at a time!");
			return;
		}
		TraderMode mode;
		if (NPCManager.isOwner(player, npc.getUID())) {
			mode = TraderMode.STOCK;
		} else if (this.unlimited) {
			mode = TraderMode.INFINITE;
		} else {
			mode = TraderMode.NORMAL;
		}
		if (!mode.hasPermission(player))
			return;
		TraderTask task = new TraderTask(npc, player, mode);
		int id = Bukkit.getServer().getScheduler()
				.scheduleSyncRepeatingTask(Citizens.plugin, task, 0, 1);
		task.addID(id);
		this.free = false;
		InventoryUtils.showInventory(npc, player);
	}

	public void removeStockable(Check check) {
		this.stocking.remove(check);

	}

	public void removeStockable(int ID, short dataValue, boolean selling) {
		removeStockable(new Check(ID, dataValue, selling));
	}

	@Override
	public void save(DataKey root) {
		root.setBoolean("unlimited", unlimited);
		root.setBoolean("locked", locked);
		root.setBoolean("use-global.sell", useGlobalSell);
		root.setBoolean("use-global.buy", useGlobalBuy);
		root.setString("stock", Joiner.on(";").join(stocking.values()));
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
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