package net.citizensnpcs.guards;

import net.citizensnpcs.guards.types.Bodyguard;
import net.citizensnpcs.guards.types.Bouncer;
import net.citizensnpcs.guards.types.Soldier;

public enum GuardState {
	/**
	 * Protects land
	 */
	BOUNCER(new Bouncer()),
	/**
	 * Protects and follows players
	 */
	BODYGUARD(new Bodyguard()),
	/**
	 * Default guard type
	 */
	NULL(null),
	/**
	 * Commanded by players, groupable.
	 */
	SOLDIER(new Soldier());
	private final GuardUpdater updater;

	GuardState(GuardUpdater updater) {
		this.updater = updater;
	}

	public GuardUpdater getUpdater() {
		return updater;
	}

	public static GuardState parse(String string) {
		try {
			return GuardState.valueOf(string.toUpperCase().replace(" ", "_"));
		} catch (Exception ex) {
			return NULL;
		}
	}
}