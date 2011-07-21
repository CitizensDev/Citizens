package com.citizens.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.citizens.Citizens;
import com.citizens.CreatureTask;
import com.citizens.Permission;
import com.citizens.SettingsManager.Constant;
import com.citizens.events.NPCTargetEvent;
import com.citizens.interfaces.Listener;
import com.citizens.npcs.NPCDataManager;
import com.citizens.npcs.NPCManager;
import com.citizens.npctypes.guards.GuardTask;
import com.citizens.npctypes.questers.quests.ChatManager;
import com.citizens.npctypes.questers.quests.QuestManager;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.ConversationUtils;
import com.citizens.utils.ServerUtils;

public class PlayerListen extends PlayerListener implements Listener {

	@Override
	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this, Event.Priority.Normal,
				Citizens.plugin);
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
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (Permission.isAdmin(event.getPlayer())
				&& Constant.NotifyUpdates.toBoolean()) {
			ServerUtils.checkForUpdates(event.getPlayer());
		}
	}

	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		QuestManager.load(event.getPlayer());
		GuardTask.checkRespawn(event.getPlayer());
		CreatureTask.setDirty();
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		NPCDataManager.pathEditors.remove(event.getPlayer().getName());
		QuestManager.unload(event.getPlayer());
		CreatureTask.setDirty();
		ConversationUtils.verify();
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
		NPCDataManager.handlePathEditor(event);
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