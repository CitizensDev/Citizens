package com.fullwall.Citizens.Utils;

import net.minecraft.server.Packet61;

import org.bukkit.Location;

public class SoundUtils {
	public enum Sound {
		BOW_FIRE(1002),
		CLICK1(1001),
		CLICK2(1000),
		DOOR_SOUND(1003),
		EXTINGUISH(1004),
		RECORD_PLAY(1005),
		SPAWN_FIRE_PARTICLE(2000),
		STEP_SOUND(2001);
		private final int soundIdentifier;

		Sound(int soundIdentifier) {
			this.soundIdentifier = soundIdentifier;
		}

		public int getSoundIdentifier() {
			return this.soundIdentifier;
		}
	}

	public static void playSound(Sound sound, Location location, int data) {
		int packetData = sound.getSoundIdentifier();
		Packet61 packet = new Packet61(packetData, location.getBlockX(),
				location.getBlockY(), location.getBlockZ(), data);
		PacketUtils.sendPacketNearby(location, 64, packet);
	}
	/*
	 * Documentation for sound.
	 * 
	 * Bow Fire - plays the fired arrow sound.
	 * Click1/2: self-explanatory. Click 1 has a slightly higher pitch.
	 * Door Sound - plays at random the door open/close sound (50/50).
	 * Extinguish - plays the 'extinguish fire' sound.
	 * Record Play - REQUIRES DATA - uses data as the record ID to play.
	 * Smoke - REQUIRES DATA - creates smoke particles (data is used as yaw).
	 * Step Sound - REQUIRES DATA - uses the data as the block id of the step
	 * sound to play.
	 */

}
