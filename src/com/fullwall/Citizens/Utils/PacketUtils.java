package com.fullwall.Citizens.Utils;

import net.minecraft.server.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PacketUtils {
	public static void sendPacketToPlayer(final Player ply,
			final Packet packet) {
		((CraftPlayer) ply).getHandle().netServerHandler.sendPacket(packet);
	}

	public static void sendPacketNearby(final Location location,
			final double radius, final Packet packet) {
		sendPacketNearby(location, radius, packet, null);
	}

	public static void sendPacketNearby(final Location location,
			double radius, final Packet packet, final Player except) {
		radius *= radius;
		final Vector locationVector = location.toVector();
		final World world = location.getWorld();
		for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
			if (ply.equals(except)) {
				continue;
			}
			if (world != ply.getWorld()) {
				continue;
			}
			if (locationVector.distanceSquared(ply.getLocation().toVector()) > radius) {
				continue;
			}
			sendPacketToPlayer(ply, packet);
		}
	}
}