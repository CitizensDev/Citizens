package com.fullwall.resources.redecouverte.NPClib.Creatures;

import com.fullwall.resources.redecouverte.NPClib.Creatures.SpawnValidator.Spawn.Range;
import com.fullwall.resources.redecouverte.NPClib.Creatures.SpawnValidator.Spawn.Type;

public class SpawnValidator {
	private final byte[] flags = new byte[64];

	public SpawnValidator() {
	}

	public SpawnValidator(Range range, boolean flag) {
		this.set(range, flag);
	}

	public SpawnValidator(Type type, int... ids) {
		this.change(type, ids);
	}

	public SpawnValidator change(Type type, int... ids) {
		int begin, end;
		switch (type) {
		case ALL_EXCEPT:
			all();
			break;
		case BETWEEN:
			begin = ids[0];
			end = ids[ids.length - 1];
			setRange(begin, end, true);
			break;
		case JUST:
			reset();
		case INCLUDING:
			for (int id : ids)
				set(id, true);
			break;
		case NOT:
			for (int id : ids)
				set(id, false);
			break;
		case NOT_BETWEEN:
			begin = ids[0];
			end = ids[ids.length - 1];
			for (byte id = 0; id <= 255; ++id)
				if (id < begin || id > end)
					set(id, true);
			break;
		}
		return this;
	}

	public SpawnValidator set(Range range, boolean flag) {
		switch (range) {
		case ALL:
			all();
			break;
		case DEFAULT:
			all();
			this.set(Range.LIQUIDS, false);
			int[] banned = { 6, 18, 26, 50, 51, 63, 64, 71, 77, 83, 85, 90 };
			this.set(banned, false);
			this.set(0, false);
		case LIQUIDS:
			setRange(8, 11, flag);
			break;
		}
		return this;
	}

	public SpawnValidator allExcept(int... ids) {
		return this.change(Type.ALL_EXCEPT, ids);
	}

	public boolean isValid(int id) {
		return get(id);
	}

	private boolean get(int index) {
		index &= 255;
		int get = index / 4;
		index %= 4;
		return (flags[get] & 1 << index) != 0;
	}

	private void set(int index, boolean flag) {
		index &= 255;
		int get = index / 4;
		index %= 4;
		byte previous = flags[get];
		if (flag)
			flags[get] = (byte) (previous | 1 << index);
		else
			flags[get] = (byte) (previous & ~(1 << index));
	}

	private void set(int[] banned, boolean flag) {
		for (int id : banned)
			set(id, flag);
	}

	private void setRange(int begin, int end, boolean flag) {
		for (int id = begin; id <= end; ++id) {
			set(id, flag);
		}
	}

	private void all() {
		for (byte i = 0; i <= 63; ++i) {
			flags[i] = 0xF;
		}
	}

	private void reset() {
		for (byte i = 0; i <= 63; ++i) {
			flags[i] = 0;
		}
	}

	public static class Spawn {
		public enum Range {
			ALL,
			DEFAULT,
			LIQUIDS;
		}

		public enum Type {
			ALL_EXCEPT,
			BETWEEN,
			INCLUDING,
			JUST,
			NOT,
			NOT_BETWEEN;
		}
	}
}
