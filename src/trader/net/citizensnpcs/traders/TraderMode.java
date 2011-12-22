package net.citizensnpcs.traders;

import net.citizensnpcs.permissions.PermissionManager;

import org.bukkit.entity.Player;

public enum TraderMode {
	NORMAL("citizens.trader.use.trade"),
	STOCK("citizens.trader.modify.stock"),
	INFINITE("citizens.trader.use.trade");

	private final String perm;

	TraderMode(String perm) {
		this.perm = perm;
	}

	public boolean hasPermission(Player player) {
		return PermissionManager.hasPermission(player, perm);
	}
}