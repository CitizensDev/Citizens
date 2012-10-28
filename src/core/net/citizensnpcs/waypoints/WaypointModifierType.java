package net.citizensnpcs.waypoints;

import java.lang.reflect.Constructor;

import net.citizensnpcs.waypoints.modifiers.ChatModifier;
import net.citizensnpcs.waypoints.modifiers.DelayModifier;
import net.citizensnpcs.waypoints.modifiers.HealthModifier;
import net.citizensnpcs.waypoints.modifiers.TeleportModifier;

public enum WaypointModifierType {
    CHAT(ChatModifier.class),
    DELAY(DelayModifier.class),
    HEALTH(HealthModifier.class),
    TELEPORT(TeleportModifier.class);

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