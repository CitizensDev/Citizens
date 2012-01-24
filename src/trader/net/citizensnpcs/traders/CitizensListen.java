package net.citizensnpcs.traders;

import net.citizensnpcs.api.event.CitizensEnableEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CitizensListen implements Listener {
    @EventHandler
    public void onCitizensEnable(CitizensEnableEvent event) {
        Trader.loadGlobal();
    }
}
