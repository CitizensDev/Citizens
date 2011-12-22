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

	public static int size() {
		return list.size();
	}

	public static HumanNPC get(int UID) {
		return list.get(UID);
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

	// Gets the list of NPCs.
	public static Iterable<HumanNPC> getNPCs() {
		return list;
	}

	// Checks if a given entity is an npc.
	public static boolean isNPC(Entity entity) {
		return get(entity) != null;
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
	public static void rename(int UID, String changeTo) {
		HumanNPC npc = get(UID);
		npc.setName(changeTo);
	}

	// Spawns a new NPC and registers it.
	public static void register(int UID, NPCCreateReason reason) {
		HumanNPC npc = new HumanNPC(UID);
		npc.load();
		register(npc, reason);
	}

	public static HumanNPC register(HumanNPC npc, NPCCreateReason reason) {
		if (!npc.spawn())
			return null;
		list.put(npc.getUID(), npc);
		npc.save();
		npc.getPlayer().setSleepingIgnored(true); // Fix beds.
		NPCCreateEvent event = new NPCCreateEvent(npc, reason,
				npc.getLocation());
		Bukkit.getServer().getPluginManager().callEvent(event);
		return npc;
	}

	// Registers a new NPC.
	public static HumanNPC register(String name, Location loc,
			NPCCreateReason reason) {
		int UID = PropertyManager.getNewNpcID();
		HumanNPC npc = new HumanNPC(UID, name);
		npc.getNPCData().setLocation(loc);
		npc.load();
		return register(npc, reason);
	}
}