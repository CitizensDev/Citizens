package net.citizensnpcs.lib;

import net.citizensnpcs.utils.PacketUtils;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet17EntityLocationAction;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.Packet40EntityMetadata;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCAnimator {
	private final CraftNPC npc;

	public NPCAnimator(CraftNPC handle) {
		this.npc = handle;
	}

	private Packet18ArmAnimation getArmAnimationPacket(int data) {
		return new Packet18ArmAnimation(this.npc, data);
	}

	private Player getPlayer() {
		return this.npc.getPlayer();
	}

	private DataWatcher getWatcher() {
		return this.npc.getDataWatcher();
	}

	public void performAnimation(Animation animation) {
		switch (animation) {
		case ACT_HURT:
			PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
					getArmAnimationPacket(2), getPlayer());
			break;
		case SNEAK:
			npc.setSneak(true);
			sendMetadataPacket();
			break;
		case SLEEP:
			sleep();
			break;
		case SWING_ARM:
			PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
					getArmAnimationPacket(1), getPlayer());
			break;
		case UNSLEEP:
			PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
					new Packet18ArmAnimation(this.npc, 3), getPlayer());
			break;
		case UNSNEAK:
			npc.setSneak(false);
			sendMetadataPacket();
			break;
		default:
			break;
		}
	}

	private void sendMetadataPacket() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				new Packet40EntityMetadata(this.npc.id, getWatcher()),
				getPlayer());
	}

	private void sleep() {
		Location loc = getPlayer().getLocation();
		Packet17EntityLocationAction packet17 = new Packet17EntityLocationAction(
				npc, 0, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

		// getPlayer().teleport(loc);
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, packet17,
				getPlayer());
	}

	public enum Animation {
		ACT_HURT,
		SNEAK,
		SLEEP,
		SWING_ARM,
		UNSLEEP,
		UNSNEAK;
	}
}