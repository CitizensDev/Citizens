package com.Citizens.resources.redecouverte.NPClib;

import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet17;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.Packet40EntityMetadata;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.Citizens.Utils.PacketUtils;

public class NPCAnimator {
	private final PathNPC npc;

	public enum Animation {
		ACT_HURT,
		CROUCH,
		SLEEP,
		SWING_ARM,
		UNSLEEP,
		UNCROUCH;
	}

	public NPCAnimator(PathNPC pathNPC) {
		this.npc = pathNPC;
	}

	public void performAnimation(Animation animation) {
		switch (animation) {
		case ACT_HURT:
			actHurt();
			break;
		case CROUCH:
			crouch();
			break;
		case SLEEP:
			sleep();
			break;
		case SWING_ARM:
			swingArm();
			break;
		case UNSLEEP:
			unsleep();
			break;
		case UNCROUCH:
			uncrouch();
			break;
		default:
		}
	}

	private void sleep() {
		Location loc = getPlayer().getLocation();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		Packet17 packet17 = new Packet17(npc, 0, x, y, z);

		// getPlayer().teleport(loc);
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, packet17,
				getPlayer());
	}

	private void unsleep() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				new Packet18ArmAnimation(this.npc, 3), getPlayer());
	}

	private DataWatcher getWatcher() {
		return this.npc.Z();
	}

	private void crouch() {
		npc.setSneak(true);
		DataWatcher watcher = getWatcher();
		Packet40EntityMetadata packet = new Packet40EntityMetadata(this.npc.id,
				watcher);
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, packet,
				getPlayer());
	}

	private void uncrouch() {
		npc.setSneak(false);
		DataWatcher watcher = getWatcher();
		Packet40EntityMetadata packet = new Packet40EntityMetadata(this.npc.id,
				watcher);
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, packet,
				getPlayer());
	}

	private void swingArm() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				new Packet18ArmAnimation(this.npc, 1), getPlayer());
	}

	private void actHurt() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				new Packet18ArmAnimation(this.npc, 2), getPlayer());
	}

	private Player getPlayer() {
		return (Player) this.npc.getBukkitEntity();
	}
}