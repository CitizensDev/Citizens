package com.citizens.NPCTypes.Guards;

public class GuardManager {
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
}