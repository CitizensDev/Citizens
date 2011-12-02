package net.citizensnpcs.utils;

import net.minecraft.server.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtils {
	public static void sendPacketToPlayer(final Player ply, final Packet packet) {
		if (ply == null)
			return;
		((CraftPlayer) ply).getHandle().netServerHandler.sendPacket(packet);
	}

	public static void sendPacketNearby(final Location location,
			final double radius, final Packet packet) {
		sendPacketNearby(location, radius, packet, null);
	}

	public static void sendPacketNearby(final Location location, double radius,
			final Packet packet, final Player except) {
		radius *= radius;
		final World world = location.getWorld();
		for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
			if (ply == null || ply.equals(except) || world != ply.getWorld()) {
				continue;
			}
			if (location.distanceSquared(ply.getLocation()) > radius) {
				continue;
			}
			sendPacketToPlayer(ply, packet);
		}
	}

	public static void sendPacketToOnline(final Packet packet,
			final Player except) {
		for (Player ply : Bukkit.getServer().getOnlinePlayers()) {
			if (ply == null || ply.equals(except)) {
				continue;
			}
			sendPacketToPlayer(ply, packet);
		}
	}
}