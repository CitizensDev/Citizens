package com.fullwall.Citizens.Utils;

import net.minecraft.server.Packet61;

import org.bukkit.Location;

import com.fullwall.Citizens.Utils.EffectUtils.Effects.IEffect;

public class EffectUtils {
	public static class Effects {
		public interface IEffect {
			public int getIdentifier();
		}

		public enum Effect implements IEffect {
			DISPENSER_PARTICLE_SPAWN(2000),
			DIG(2001);
			private final int effectIdentifier;

			Effect(int effectIdentifier) {
				this.effectIdentifier = effectIdentifier;
			}

			@Override
			public int getIdentifier() {
				return this.effectIdentifier;
			}
		}

		public enum Sound implements IEffect {
			DISPENSER_FIRE(1000),
			DISPENSER_FIRE_EMPTY(1001),
			DOOR_SOUND(1003),
			FIRE_EXTINGUISH(1004),
			TRAPDOOR_SOUND(1003),
			PROJECTILE_FIRE(1002),
			RECORD_PLAY(1005);
			private final int soundIdentifier;

			Sound(int soundIdentifier) {
				this.soundIdentifier = soundIdentifier;
			}

			@Override
			public int getIdentifier() {
				return this.soundIdentifier;
			}
		}
	}

	public static void playSound(IEffect effect, Location location, int data) {
		int packetData = effect.getIdentifier();
		Packet61 packet = new Packet61(packetData, location.getBlockX(),
				location.getBlockY(), location.getBlockZ(), data);
		PacketUtils.sendPacketNearby(location, 64, packet);
	}
	/*
	 * Documentation for effects.
	 * 
	 * Projectile Fire - plays the fired arrow sound.
	 * Dispenser fire - sound when dispensers are fired.
	 * (Trap)Door Sound - plays at random the (trap)door open/close sound (50/50).
	 * Fire Extinguish - plays the 'extinguish fire' sound.
	 * Record Play - REQUIRES DATA - uses data as the record ID to play.
	 * Dispenser particle spawn - REQUIRES DATA - creates the particles
	 * that appear when dispenser is used (data is used as direction (up to 9)).
	 * Dig - REQUIRES DATA - uses the data as the block id of the
	 * sound to play, creates dig effects.
	 */

}
