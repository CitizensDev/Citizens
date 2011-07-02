package com.citizens.Interfaces;

import com.citizens.Properties.PropertyManager;

public class NPCType {
	private final NPCFactory factory;
	private final String type;
	private final NPCPurchaser purchaser;
	private final Saveable saveable;

	public NPCType(String type, Saveable saveable, NPCPurchaser purchaser,
			Class<? extends Toggleable> clazz) {
		this.saveable = saveable;
		this.purchaser = purchaser;
		this.factory = new NPCFactory(clazz);
		this.type = type;
	}

	public void registerAutosave() {
		PropertyManager.add(type, this.saveable);
	}

	public NPCFactory factory() {
		return factory;
	}

	public String getType() {
		return this.type;
	}

	public NPCPurchaser getPurchaser() {
		return purchaser;
	}
}