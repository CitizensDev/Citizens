package net.citizensnpcs.resources.npclib;

import java.util.Map;

import net.citizensnpcs.Settings;
import net.citizensnpcs.api.event.NPCCreateEvent;
import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.api.event.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.npcdata.NPCData;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.properties.PropertyManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.common.collect.MapMaker;

public class NPCManager {
	public static final Map<Integer, String> GlobalUIDs = new MapMaker()
			.makeMap();
	private static NPCList list = new NPCList();

	public static HumanNPC get(int UID) {
		return list.get(UID);
	}

	public static HumanNPC get(Entity entity) {
		return list.getNPC(entity);
	}

	// Gets the list of NPCs.
	public static NPCList getList() {
		return list;
	}

	// Checks if a given entity is an npc.
	public static boolean isNPC(Entity entity) {
		return list.getNPC(entity) != null;
	}

	// Rotates an NPC.
	public static void faceEntity(HumanNPC npc, Entity entity) {
		if (npc.getWorld() != entity.getWorld())
			return;
		Location loc = npc.getLocation(), pl = entity.getLocation();
		double xDiff = pl.getX() - loc.getX();
		double yDiff = pl.getY() - loc.getY();
		double zDiff = pl.getZ() - loc.getZ();
		double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
		double yaw = (Math.acos(xDiff / DistanceXZ) * 180 / Math.PI);
		double pitch = (Math.acos(yDiff / DistanceY) * 180 / Math.PI) - 90;
		if (zDiff < 0.0) {
			yaw = yaw + (Math.abs(180 - yaw) * 2);
		}
		npc.getHandle().yaw = (float) yaw - 90;
		npc.getHandle().pitch = (float) pitch;

		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (npc.getOwner().equalsIgnoreCase(player.getName())) {
				loc = npc.getNPCData().getLocation();
				loc.setPitch(npc.getLocation().getPitch());
				loc.setYaw(npc.getLocation().getYaw());
			}
		}
	}

	// Despawns an NPC.
	public static void despawn(int UID, NPCRemoveReason reason) {
		GlobalUIDs.remove(UID);
		NPCSpawner.despawnNPC(list.remove(UID), reason);
	}

	// Despawns all NPCs.
	public static void despawnAll(NPCRemoveReason reason) {
		for (int i : GlobalUIDs.keySet()) {
			despawn(i, reason);
		}
	}

	public static void safeDespawn(HumanNPC npc) {
		NPCSpawner.despawnNPC(npc, NPCRemoveReason.UNLOAD);
	}

	// Removes an NPC.
	public static void remove(int UID, NPCRemoveReason reason) {
		PropertyManager.remove(get(UID));
		despawn(UID, reason);
	}

	// Removes all NPCs.
	public static void removeAll(NPCRemoveReason reason) {
		for (int i : GlobalUIDs.keySet()) {
			remove(i, reason);
		}
	}

	// Removes an NPC, but not from the properties.
	public static void removeForRespawn(int UID) {
		PropertyManager.save(list.get(UID));
		despawn(UID, NPCRemoveReason.UNLOAD);
	}

	// Registers a UID in the global list.
	private static void registerUID(int UID, String name) {
		GlobalUIDs.put(UID, name);
	}

	// Checks if a player has an npc selected.
	public static boolean hasSelected(Player player) {
		return NPCDataManager.selectedNPCs.get(player.getName()) != null
				&& !NPCDataManager.selectedNPCs.get(player.getName())
						.toString().isEmpty();
	}

	// Checks if the player has selected the given npc.
	public static boolean hasSelected(Player player, int UID) {
		return hasSelected(player)
				&& NPCDataManager.selectedNPCs.get(player.getName()) == UID;
	}

	// Checks if a player owns a given npc.
	public static boolean isOwner(Player player, int UID) {
		return get(UID).getOwner().equalsIgnoreCase(player.getName());
	}

	// Renames an npc.
	public static void rename(int UID, String changeTo, String owner) {
		HumanNPC npc = get(UID);
		npc.getNPCData().setName(changeTo);
		removeForRespawn(UID);
		register(UID, owner, NPCCreateReason.RESPAWN);
	}

	// Sets the colour of an npc's name.
	public static void setColour(int UID, String owner) {
		removeForRespawn(UID);
		register(UID, owner, NPCCreateReason.RESPAWN);
	}

	// Spawns a new NPC and registers it.
	public static void register(int UID, String owner, NPCCreateReason reason) {
		Location loc = PropertyManager.getBasic().getLocation(UID);

		ChatColor colour = PropertyManager.getBasic().getColour(UID);
		String name = PropertyManager.getBasic().getName(UID);
		name = ChatColor.stripColor(name);
		if (!Settings.getString("SpaceChar").isEmpty()) {
			name = name.replace(Settings.getString("SpaceChar"), " ");
		}
		String npcName = name;
		if (colour != ChatColor.WHITE) {
			npcName = colour + name;
		}
		HumanNPC npc = NPCSpawner.spawnNPC(UID, npcName, loc);

		NPCCreateEvent event = new NPCCreateEvent(npc, reason, loc);
		Bukkit.getServer().getPluginManager().callEvent(event);

		npc.setNPCData(new NPCData(npcName, UID, loc, colour, PropertyManager
				.getBasic().getItems(UID), NPCDataManager.NPCTexts.get(UID),
				PropertyManager.getBasic().isTalk(UID), PropertyManager
						.getBasic().isLookWhenClose(UID), PropertyManager
						.getBasic().isTalkWhenClose(UID), owner));
		PropertyManager.getBasic().saveOwner(UID, owner);
		PropertyManager.load(npc);

		registerUID(UID, npcName);
		list.put(UID, npc);
		PropertyManager.save(npc);

		npc.getPlayer().setSleepingIgnored(true); // Fix beds.
	}

	// Registers a new NPC.
	public static int register(String name, Location loc, String owner,
			NPCCreateReason reason) {
		int UID = PropertyManager.getBasic().getNewNpcID();
		PropertyManager.getBasic().saveLocation(loc, UID);
		PropertyManager.getBasic().saveLookWhenClose(UID,
				Settings.getBoolean("DefaultLookAt"));
		PropertyManager.getBasic().saveTalkWhenClose(UID,
				Settings.getBoolean("DefaultTalkClose"));
		PropertyManager.getBasic().saveName(UID, name);
		register(UID, owner, reason);
		return UID;
	}
}