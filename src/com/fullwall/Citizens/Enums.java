package com.fullwall.Citizens;

public class Enums {
	/**
	 * Guard types
	 */
	public enum GuardType {
		/**
		 * Protects land
		 */
		BOUNCER,
		/**
		 * Protects and follows players
		 */
		BODYGUARD,
		/**
		 * Default guard type
		 */
		NULL;

		public static GuardType parse(String string) {
			try {
				return GuardType.valueOf(string.toUpperCase());
			} catch (Exception ex) {
				return NULL;
			}
		}
	}

	/**
	 * Wizard modes
	 */
	public enum WizardMode {
		/**
		 * Teleports players
		 */
		TELEPORTER,
		/**
		 * Changes the time of the world
		 */
		TIME_CHANGER,
		/**
		 * Spawns mobs into the world
		 */
		MOB_SPAWNER;
	}
}