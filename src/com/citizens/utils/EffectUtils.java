package com.citizens.utils;

import net.minecraft.server.Packet61;

import org.bukkit.Location;

import com.citizens.utils.EffectUtils.Effects.IEffect;

public class EffectUtils {
	public static class Effects {
		public interface IEffect {
			public int getIdentifier();
		}

		public static IEffect getByName(String name) {
			name = name.toUpperCase().replace(" ", "_");
			for (Effect effect : Effect.values()) {
				if (effect.name().startsWith(name)) {
					return effect;
				}
			}
			for (Sound sound : Sound.values()) {
				if (sound.name().startsWith(name)) {
					return sound;
				}
			}
			return null;
		}

		public static IEffect getByIdentifier(int identifier) {
			for (Effect effect : Effect.values()) {
				if (effect.getIdentifier() == identifier)
					return effect;
			}
			for (Sound sound : Sound.values()) {
				if (sound.getIdentifier() == identifier)
					return sound;
			}
			return null;
		}

		public enum Effect implements IEffect {
			/**
			 * Creates the particles that appear when dispenser is used, data is
			 * used as direction (up to 9).
			 */
			DISPENSER_PARTICLE(2000),
			/**
			 * Creates dig effects, uses the data as the block ID of the sound
			 * to play.
			 */
			DIG(2001);
			private final int effectIdentifier;

			Effect(int effectIdentifier) {
				this.effectIdentifier = effectIdentifier;
			}

			@Override
			public int getIdentifier() {
				return this.effectIdentifier;
			}

			@Override
			public String toString() {
				return "" + this.getIdentifier();
			}
		}

		public enum Sound implements IEffect {
			/**
			 * Plays the sound when dispensers are fired
			 */
			DISPENSER_FIRE(1000),
			/**
			 * Plays the sound when an empty dispenser attempts to fire
			 */
			DISPENSER_FIRE_EMPTY(1001),
			/**
			 * Plays the open/close door sound (50/50)
			 */
			DOOR_SOUND(1003),
			/**
			 * Plays the sound when a fire is extinguished
			 */
			FIRE_EXTINGUISH(1004),
			/**
			 * Plays the open/close trapdoor sound (50/50)
			 */
			TRAPDOOR_SOUND(1003),
			/**
			 * Plays the fired arrow sound
			 */
			PROJECTILE_FIRE(1002),
			/**
			 * Plays a record, uses data as the item ID of the record to play
			 */
			RECORD_PLAY(1005);
			private final int soundIdentifier;

			Sound(int soundIdentifier) {
				this.soundIdentifier = soundIdentifier;
			}

			@Override
			public int getIdentifier() {
				return this.soundIdentifier;
			}

			@Override
			public String toString() {
				return "" + this.getIdentifier();
			}
		}
	}

	public static void playSound(IEffect effect, Location location, int data) {
		int packetData = effect.getIdentifier();
		Packet61 packet = new Packet61(packetData, location.getBlockX(),
				location.getBlockY(), location.getBlockZ(), data);
		PacketUtils.sendPacketNearby(location, 64, packet);
	}
}