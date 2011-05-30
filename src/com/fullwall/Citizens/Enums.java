package com.fullwall.Citizens;

public class Enums {
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

	public enum GuardListType {
		/**
		 * Blacklist mobs
		 */
		BLACK,
		/**
		 * Whitelist players
		 */
		WHITE;

		public static GuardListType parse(String string) {
			try {
				return GuardListType.valueOf(string.toUpperCase());
			} catch (Exception ex) {
				return null;
			}
		}
	}
}