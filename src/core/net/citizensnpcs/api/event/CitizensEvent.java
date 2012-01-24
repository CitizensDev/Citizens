package net.citizensnpcs.api.event;

import org.bukkit.event.Event;

public abstract class CitizensEvent extends Event {
    private static final long serialVersionUID = 1L;

    protected CitizensEvent(String name) {
        super(name);
    }
}