package net.citizensnpcs.listeners;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.Settings;
import net.citizensnpcs.TickTask;
import net.citizensnpcs.api.event.NPCTargetEvent;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.utils.ConversationUtils;
import net.citizensnpcs.utils.Web;

import org.bukkit.Bukkit;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerListen extends PlayerListener implements Listener {
	@Override
	public void registerEvents(Citizens plugin) {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_JOIN, this, Priority.Normal, plugin);
		pm.registerEvent(Type.PLAYER_LOGIN, this, Priority.Normal, plugin);
		pm.registerEvent(Type.PLAYER_QUIT, this, Priority.Normal, plugin);
		pm.registerEvent(Type.PLAYER_INTERACT, this, Priority.Normal, plugin);
		pm.registerEvent(Type.PLAYER_INTERACT_ENTITY, this, Priority.Normal,
				plugin);
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (PermissionManager.hasPermission(event.getPlayer(),
				"citizens.admin.notifyupdates")
				&& Settings.getBoolean("NotifyUpdates")) {
			(new Thread() {
				public void run() {
					Web.notifyUpdate(event.getPlayer());
				}
			}).start();
		}
	}

	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		CreatureTask.setDirty();
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		NPCDataManager.pathEditors.remove(event.getPlayer());
		TickTask.clearActions(event.getPlayer());
		CreatureTask.setDirty();
		ConversationUtils.verify();
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		NPCDataManager.handlePathEditor(event);
		if (NPCDataManager.equipmentEditors.containsKey(event.getPlayer())
				&& event.getAction() == Action.RIGHT_CLICK_AIR) {
			event.setUseItemInHand(Result.DENY);
		}
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

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		ConversationUtils.onChat(event);
	}
}