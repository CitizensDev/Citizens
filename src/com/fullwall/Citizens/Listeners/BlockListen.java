package com.fullwall.Citizens.Listeners;

import org.bukkit.event.block.BlockListener;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Interfaces.Listener;

public class BlockListen extends BlockListener implements Listener {
	private Citizens plugin;
	private PluginManager pm;

	public BlockListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
	}
}
