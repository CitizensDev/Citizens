package net.citizensnpcs.traders;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.citizensnpcs.api.Node;
import net.citizensnpcs.api.Properties;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.resources.npclib.HumanNPC;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Joiner;

public class TraderProperties extends PropertyManager implements Properties {
	private final String isTrader = ".trader.toggle";
	private final String stock = ".trader.stock";
	private final String unlimited = ".trader.unlimited";
	private final String balance = ".trader.balance";

	public static final TraderProperties INSTANCE = new TraderProperties();

	private TraderProperties() {
	}

	private void saveBalance(int UID, double traderBalance) {
		profiles.setDouble(UID + balance, traderBalance);
	}

	private double getBalance(int UID) {
		return profiles.getDouble(UID + balance);
	}

	private void saveUnlimited(int UID, boolean value) {
		profiles.setBoolean(UID + unlimited, value);
	}

	private boolean isUnlimited(int UID) {
		return profiles.getBoolean(UID + unlimited);
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
					if (parts.length == 2) {
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
				saveUnlimited(npc.getUID(), trader.isUnlimited());
				saveStockables(npc.getUID(), trader.getStocking());
				saveBalance(npc.getUID(), npc.getBalance());
			}
		}
	}

	@Override
	public void loadState(HumanNPC npc) {
		if (getEnabled(npc)) {
			npc.registerType("trader");
			Trader trader = npc.getType("trader");
			npc.setBalance(getBalance(npc.getUID()));
			trader.setUnlimited(isUnlimited(npc.getUID()));
			trader.setStocking(getStockables(npc.getUID()));
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
	public void copy(int UID, int nextUID) {
		if (profiles.pathExists(UID + isTrader)) {
			profiles.setString(nextUID + isTrader,
					profiles.getString(UID + isTrader));
		}
		if (profiles.pathExists(UID + stock)) {
			profiles.setString(nextUID + stock, profiles.getString(UID + stock));
		}
		if (profiles.pathExists(UID + unlimited)) {
			profiles.setString(nextUID + unlimited,
					profiles.getString(UID + unlimited));
		}
		if (profiles.pathExists(UID + balance)) {
			profiles.setString(nextUID + balance, UID + balance);
		}
	}

	@Override
	public List<Node> getNodes() {
		return null;
	}
}