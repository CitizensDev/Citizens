package net.citizensnpcs.resources.npclib;

import net.citizensnpcs.utils.PacketUtils;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet17EntityLocationAction;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.Packet40EntityMetadata;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCAnimator {
	private final PathNPC npc;

	public enum Animation {
		ACT_HURT,
		SNEAK,
		SLEEP,
		SWING_ARM,
		UNSLEEP,
		UNSNEAK;
	}

	public NPCAnimator(PathNPC pathNPC) {
		this.npc = pathNPC;
	}

	public void performAnimation(Animation animation) {
		switch (animation) {
		case ACT_HURT:
			actHurt();
			break;
		case SNEAK:
			sneak();
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
		case UNSNEAK:
			unsneak();
			break;
		default:
			break;
		}
	}

	private void sleep() {
		Location loc = getPlayer().getLocation();
		Packet17EntityLocationAction packet17 = new Packet17EntityLocationAction(
				npc, 0, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

		// getPlayer().teleport(loc);
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64, packet17,
				getPlayer());
	}

	private void unsleep() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				new Packet18ArmAnimation(this.npc, 3), getPlayer());
	}

	private DataWatcher getWatcher() {
		return this.npc.getDataWatcher();
	}

	private void sendMetadataPacket() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				new Packet40EntityMetadata(this.npc.id, getWatcher()),
				getPlayer());
	}

	private Packet18ArmAnimation getArmAnimationPacket(int data) {
		return new Packet18ArmAnimation(this.npc, data);
	}

	private void sneak() {
		npc.setSneak(true);
		sendMetadataPacket();
	}

	private void unsneak() {
		npc.setSneak(false);
		sendMetadataPacket();
	}

	private void swingArm() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				getArmAnimationPacket(1), getPlayer());
	}

	private void actHurt() {
		PacketUtils.sendPacketNearby(getPlayer().getLocation(), 64,
				getArmAnimationPacket(2), getPlayer());
	}

	private Player getPlayer() {
		return (Player) this.npc.getBukkitEntity();
	}
}