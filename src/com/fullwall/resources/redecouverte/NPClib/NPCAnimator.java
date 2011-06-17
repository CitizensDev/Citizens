package com.fullwall.resources.redecouverte.NPClib;

import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.Packet40EntityMetadata;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Utils.PacketUtils;

public class NPCAnimator {
	private final PathNPC npc;

	public enum Action {
		ACT_HURT,
		CROUCH,
		SWING_ARM,
		UNCROUCH;
	}

	public NPCAnimator(PathNPC pathNPC) {
		this.npc = pathNPC;
	}

	public void performAction(Action action) {
		switch (action) {
		case ACT_HURT:
			actHurt();
			break;
		case CROUCH:
			crouch();
			break;
		case SWING_ARM:
			swingArm();
			break;
		case UNCROUCH:
			uncrouch();
			break;
		default:
        }
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