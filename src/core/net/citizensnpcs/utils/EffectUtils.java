package net.citizensnpcs.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.citizensnpcs.utils.EffectUtils.Effects.IEffect;
import net.minecraft.server.Packet61WorldEvent;

import org.bukkit.Location;

import com.google.common.base.Joiner;

public class EffectUtils {
	public static class Effects {
		public interface IEffect {
			public int getIdentifier();
		}

		public static Map<String, IEffect> names = new HashMap<String, IEffect>();
		private static Set<String> formatted = new HashSet<String>();

		public static IEffect getByName(String name) {
			name = name.toUpperCase().replace(" ", "_");
			for (String effect : names.keySet()) {
				if (effect.startsWith(name)) {
					return names.get(effect);
				}
			}
			return null;
		}

		public static IEffect getByIdentifier(int identifier) {
			for (IEffect effect : names.values()) {
				if (effect.getIdentifier() == identifier) {
					return effect;
				}
			}
			return null;
		}

		public static Set<String> getFormattedEffects() {
			return formatted;
		}

		public static void addFormatted(String name) {
			String[] split = name.toLowerCase().split("_");
			split[0] = StringUtils.capitalise(split[0]);
			formatted.add(Joiner.on(" ").join(split));
		}

		public enum Effect implements IEffect {
			/*
			 * Creates the particles that appear when dispenser is used, data is
			 * used as direction (up to 9).
			 */
			DISPENSER_PARTICLE_SPAWN(2000),
			/*
			 * Creates dig effects, uses the data as the block ID of the sound
			 * to play.
			 */
			DIG(2001),
			/*
			 * Plays a record, uses data as the item ID of the record to play
			 */
			RECORD_PLAY(1005);
			private final int effectIdentifier;

			Effect(int effectIdentifier) {
				this.effectIdentifier = effectIdentifier;
				Effects.names.put(this.name(), this);
				Effects.addFormatted(this.name());
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
			/*
			 * Plays the sound when dispensers are fired
			 */
			DISPENSER_FIRE(1000),
			/*
			 * Plays the sound when an empty dispenser attempts to fire
			 */
			DISPENSER_FIRE_EMPTY(1001),
			/*
			 * Plays the open/close door sound (50/50)
			 */
			DOOR_SOUND(1003),
			/*
			 * Plays the sound when a fire is extinguished
			 */
			FIRE_EXTINGUISH(1004),
			/*
			 * Plays the fired arrow sound
			 */
			PROJECTILE_FIRE(1002);
			private final int soundIdentifier;

			Sound(int soundIdentifier) {
				this.soundIdentifier = soundIdentifier;
				Effects.names.put(this.name(), this);
				Effects.addFormatted(this.name());
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

	public static class EffectData {
		private final IEffect effect;
		private final int data;

		public EffectData(IEffect effect, int data) {
			this.effect = effect;
			this.data = data;
		}

		public EffectData(IEffect effect) {
			this(effect, 0);
		}

		public IEffect getEffect() {
			return effect;
		}

		public int getData() {
			return data;
		}
	}

	public static void playSound(IEffect effect, Location location, int data) {
		int packetData = effect.getIdentifier();
		Packet61WorldEvent packet = new Packet61WorldEvent(packetData,
				location.getBlockX(), location.getBlockY(),
				location.getBlockZ(), data);
		PacketUtils.sendPacketNearby(location, 64, packet);
	}
}