package com.fullwall.Citizens;

public class Enums {
	public enum GuardType {
		BOUNCER, BODYGUARD, NULL;

		public static GuardType parse(String string) {
			try {
				return GuardType.valueOf(string.toUpperCase());
			} catch (Exception ex) {
				return NULL;
			}
		}
	}
}