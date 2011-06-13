package com.fullwall.Citizens.Listeners;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Interfaces.Listener;
import com.fullwall.Citizens.NPCTypes.Questers.Quests.QuestManager;

public class BlockListen extends BlockListener implements Listener {
	private final Citizens plugin;
	private PluginManager pm;

	public BlockListen(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void registerEvents() {
		pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_BREAK, this, Event.Priority.Normal,
				plugin);
		pm.registerEvent(Event.Type.BLOCK_PLACE, this, Event.Priority.Normal,
				plugin);
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		QuestManager.incrementQuest(event.getPlayer(), event);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		QuestManager.incrementQuest(event.getPlayer(), event);
	}
}