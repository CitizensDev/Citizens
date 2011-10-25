package net.citizensnpcs.traders;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.citizensnpcs.properties.Node;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.properties.Storage;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class TraderProperties extends PropertyManager implements Properties {
	private static final String isTrader = ".trader.toggle";
	private static final String stock = ".trader.stock";

	public static final TraderProperties INSTANCE = new TraderProperties();

	private TraderProperties() {
	}

	private void setStockables(int UID, String set) {
		profiles.setString(UID + stock, set);
	}

	private void saveStockables(int UID, Map<Check, Stockable> stockables) {
		setStockables(UID, Joiner.on(";").join(stockables.values()));
	}

	private Map<Check, Stockable> getStockables(int UID) {
		Map<Check, Stockable> stockables = new ConcurrentHashMap<Check, Stockable>();
		int i = 0;
		label: for (String s : profiles.getString(UID + stock).split(";")) {
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
		return stockables;
	}

	@Override
	public void saveState(HumanNPC npc) {
		if (exists(npc)) {
			boolean is = npc.isType("trader");
			setEnabled(npc, is);
			if (is) {
				Trader trader = npc.getType("trader");
				trader.save(profiles, npc.getUID());
				saveStockables(npc.getUID(), trader.getStocking());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("trader");
			Trader trader = npc.getType("trader");
			trader.setStocking(getStockables(npc.getUID()));
			trader.load(profiles, npc.getUID());
		}
		saveState(npc);
	}

	@Override
	public void setEnabled(HumanNPC npc, boolean value) {
		profiles.setBoolean(npc.getUID() + isTrader, value);
	}

	@Override
	public boolean getEnabled(HumanNPC npc) {
		return profiles.getBoolean(npc.getUID() + isTrader);
	}

	@Override
	public List<String> getNodesForCopy() {
		return nodesForCopy;
	}

	private static final List<String> nodesForCopy = Lists.newArrayList(
			"trader.toggle", "trader.stock", "trader.unlimited",
			"trader.clear-oldest", "trader.last-bought");

	@Override
	public List<Node> getNodes() {
		return null;
	}

	public static void loadGlobal() {
		Storage storage = UtilityProperties.getSettings();
		for (Object key : storage.getKeys("traders.global-prices")) {
			String path = "traders.global-prices." + key;
			int itemID = storage.getInt(path + ".id", 1);
			int amount = storage.getInt(path + ".amount", 1);
			short data = (short) storage.getInt(path + ".data");
			double price = storage.getDouble(path + ".price");
			boolean selling = storage.getBoolean(path + ".selling", false);
			if (itemID > 0 && amount > 0) {
				Trader.addGlobal(new Stockable(new ItemStack(itemID, amount,
						data), new ItemPrice(price), selling));
			}
		}
	}
}