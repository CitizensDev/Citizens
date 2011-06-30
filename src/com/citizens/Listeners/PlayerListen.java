package com.citizens.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.citizens.Citizens;
import com.citizens.CreatureTask;
import com.citizens.Events.NPCTargetEvent;
import com.citizens.Interfaces.Listener;
import com.citizens.NPCTypes.Guards.GuardTask;
import com.citizens.NPCTypes.Questers.Quests.ChatManager;
import com.citizens.NPCTypes.Questers.Quests.QuestManager;
import com.citizens.NPCs.NPCManager;
import com.citizens.resources.redecouverte.NPClib.HumanNPC;

public class PlayerListen extends PlayerListener implements Listener {

	@Override
	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_LOGIN, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_MOVE, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, this,
				Event.Priority.Normal, Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_CHAT, this, Event.Priority.Normal,
				Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this,
				Event.Priority.Normal, Citizens.plugin);
		pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, this,
				Event.Priority.Normal, Citizens.plugin);
	}

	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		QuestManager.load(event.getPlayer());
		GuardTask.checkRespawn(event.getPlayer());
		CreatureTask.setDirty();
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		NPCManager.pathEditors.remove(event.getPlayer().getName());
		QuestManager.unload(event.getPlayer());
		CreatureTask.setDirty();
	}

	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		QuestManager.incrementQuest(event.getPlayer(), event);
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		QuestManager.incrementQuest(event.getPlayer(), event);
	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		String name = event.getPlayer().getName();
		if (!ChatManager.hasEditMode(name)) {
			return;
		}
		// TODO do stuff
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		NPCManager.handlePathEditor(event);
	}

	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		HumanNPC npc = NPCManager.get(event.getRightClicked());
		if (npc != null) {
			EntityTargetEvent rightClickEvent = new NPCTargetEvent(
					npc.getPlayer(), event.getPlayer());
			Bukkit.getServer().getPluginManager().callEvent(rightClickEvent);
		}
	}
}