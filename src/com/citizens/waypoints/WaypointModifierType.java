package com.citizens.waypoints;

import java.lang.reflect.Constructor;

import com.citizens.waypoints.modifiers.ChatModifier;
import com.citizens.waypoints.modifiers.DelayModifier;
import com.citizens.waypoints.modifiers.EffectModifier;
import com.citizens.waypoints.modifiers.HealthModifier;
import com.citizens.waypoints.modifiers.TeleportModifier;

public enum WaypointModifierType {
	CHAT(ChatModifier.class),
	DELAY(DelayModifier.class),
	HEALTH(HealthModifier.class),
	TELEPORT(TeleportModifier.class),
	EFFECT(EffectModifier.class);

	private final Constructor<? extends WaypointModifier> constructor;

	WaypointModifierType(Class<? extends WaypointModifier> clazz) {
		Constructor<? extends WaypointModifier> temp = null;
		try {
			temp = clazz.getConstructor(Waypoint.class);
		} catch (Exception e) {
		}
		this.constructor = temp;
	}

	public WaypointModifier create(Waypoint waypoint) {
		try {
			return constructor.newInstance(waypoint);
		} catch (Exception e) {
			return null;
		}
	}

	public static WaypointModifierType value(String type) {
		try {
			return WaypointModifierType.valueOf(type);
		} catch (IllegalArgumentException excp) {
			return null;
		}
	}
}