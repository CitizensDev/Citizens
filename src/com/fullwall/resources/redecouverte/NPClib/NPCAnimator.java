package com.fullwall.resources.redecouverte.NPClib;

import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.Packet40EntityMetadata;

import org.bukkit.entity.Player;

import com.fullwall.Citizens.Utils.PacketUtils;

public class NPCAnimator {
	private PathNPC npc;

	public NPCAnimator(PathNPC pathNPC) {
		this.npc = pathNPC;
	}

	public DataWatcher getWatcher() {
		return this.npc.W();
	}

	public void crouch() {
		npc.setSneak(true);
		DataWatcher watcher = getWatcher();
		Packet40EntityMetadata packet = new Packet40EntityMetadata(this.npc.id,
				watcher);
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, packet,
				getPlayer());
	}

	public void uncrouch() {
		npc.setSneak(false);
		DataWatcher watcher = getWatcher();
		Packet40EntityMetadata packet = new Packet40EntityMetadata(this.npc.id,
				watcher);
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, packet,
				getPlayer());
	}

	public void swingArm() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				new Packet18ArmAnimation(this.npc, 1), getPlayer());
	}

	public void actHurt() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				new Packet18ArmAnimation(this.npc, 2), getPlayer());
	}

	public Player getPlayer() {
		return (Player) this.npc.getBukkitEntity();
	}
}
