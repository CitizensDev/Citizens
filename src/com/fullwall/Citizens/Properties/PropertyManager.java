package com.fullwall.Citizens.Properties;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.fullwall.Citizens.PropertyHandler;
import com.fullwall.Citizens.Interfaces.Saveable;

public class PropertyManager {
	public HashMap<String, Saveable> properties = new HashMap<String, Saveable>();

	public enum PropertyType {
		BASIC, TRADER, HEALER, WIZARD, QUESTER, BLACKSMITH;
	}

	public static PropertyHandler getHandler(Class<?> passedClass,
			String fieldName, Saveable saveable) {
		try {
			Field f = passedClass.getDeclaredField(fieldName);
			PropertyHandler handler = (PropertyHandler) f.get(saveable);
			return handler;
		} catch (Exception ex) {
			return null;
		}
	}
}
