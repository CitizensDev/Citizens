package net.citizensnpcs.lib;

import net.citizensnpcs.api.event.NPCCreateEvent;
import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.npcdata.NPCDataManager;
import net.citizensnpcs.properties.PropertyManager;
import net.citizensnpcs.utils.ByIdArray;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NPCManager {
	private static final ByIdArray<HumanNPC> list = new ByIdArray<HumanNPC>();

	// Despawns an NPC.
	public static void despawn(int UID) {
		list.remove(UID).despawn();
	}

	// Despawns all NPCs.
	public static void despawnAll() {
		for (HumanNPC npc : list) {
			despawn(npc.getUID());
		}
	}

	// Rotates an NPC.
	public static void faceEntity(Entity from, Entity to) {
		if (from.getWorld() != to.getWorld())
			return;
		Location loc = from.getLocation(), pl = to.getLocation();
		double dX = pl.getX() - loc.getX();
		double dY = pl.getY() - loc.getY();
		double dZ = pl.getZ() - loc.getZ();
		double hypotenuse = Math.sqrt(dX * dX + dZ * dZ);
		double DistanceY = Math.sqrt(hypotenuse * hypotenuse + dY * dY);
		double yaw = (Math.acos(dX / hypotenuse) * 180 / Math.PI);
		double pitch = (Math.acos(dY / DistanceY) * 180 / Math.PI) - 90;
		if (dZ < 0.0) {
			yaw += (Math.abs(180 - yaw) * 2);
		}
		((CraftEntity) to).getHandle().yaw = (float) yaw - 90;
		((CraftEntity) to).getHandle().pitch = (float) pitch;
	}

	public static HumanNPC get(Entity entity) {
		if (entity == null) {
			return null;
		}
		net.minecraft.server.Entity mcEntity = ((CraftEntity) entity)
				.getHandle();
		if (mcEntity instanceof CraftNPC) {
			HumanNPC npc = ((CraftNPC) mcEntity).npc;
			if (npc == null)
				return null;
			// Compare object references to eliminate conflicting UIDs.
			if (get(npc.getUID()) == npc) {
				return npc;
			}
		}
		return null;
	}

	public static HumanNPC get(int UID) {
		return list.get(UID);
	}

	// Gets the list of NPCs.
	public static Iterable<HumanNPC> getNPCs() {
		return list;
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

	// Checks if a given entity is an npc.
	public static boolean isNPC(Entity entity) {
		return get(entity) != null;
	}

	// Checks if a player owns a given npc.
	public static boolean isOwner(Player player, int UID) {
		return get(UID).getOwner().equalsIgnoreCase(player.getName());
	}

	public static HumanNPC register(HumanNPC npc, NPCCreateReason reason) {
		if (npc == null)
			return null;
		npc.spawn();
		list.put(npc.getUID(), npc);
		npc.save();
		npc.getPlayer().setSleepingIgnored(true); // Fix beds.
		NPCCreateEvent event = new NPCCreateEvent(npc, reason,
				npc.getLocation());
		Bukkit.getServer().getPluginManager().callEvent(event);
		return npc;
	}

	// Spawns a new NPC and registers it.
	public static void register(int UID, NPCCreateReason reason) {
		register(HumanNPC.createAndLoad(UID), reason);
	}

	// Registers a new NPC.
	public static HumanNPC register(String name, Location loc,
			NPCCreateReason reason) {
		int UID = PropertyManager.getNewNpcID();
		return register(HumanNPC.createAndLoad(UID, name, loc), reason);
	}

	// Removes an NPC.
	public static void remove(int UID) {
		PropertyManager.remove(get(UID));
		despawn(UID);
	}

	// Removes all NPCs.
	public static void removeAll() {
		for (HumanNPC npc : list) {
			remove(npc.getUID());
		}
	}

	// Renames an npc.
	public static void rename(int UID, String changeTo) {
		HumanNPC npc = get(UID);
		npc.setName(changeTo);
	}

	public static int size() {
		return list.size();
	}
}