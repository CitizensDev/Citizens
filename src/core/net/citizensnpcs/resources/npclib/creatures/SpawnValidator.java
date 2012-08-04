package net.citizensnpcs.resources.npclib.creatures;

import java.util.BitSet;

import net.citizensnpcs.resources.npclib.creatures.SpawnValidator.Spawn.Range;
import net.citizensnpcs.resources.npclib.creatures.SpawnValidator.Spawn.Type;

public class SpawnValidator {
    private final BitSet flags = new BitSet(256);
    public SpawnValidator() {
    }

    public SpawnValidator(Range range, boolean flag) {
        this.set(range, flag);
    }

    public SpawnValidator(Type type, int... ids) {
        this.change(type, ids);
    }

    private void all() {
        flags.set(0, 255);
    }

    public SpawnValidator allExcept(int... ids) {
        return this.change(Type.ALL_EXCEPT, ids);
    }

    public SpawnValidator change(Type type, int... ids) {
        int begin, end;
        switch (type) {
        case ALL:
            all();
            break;
        case ALL_EXCEPT:
            all();
            for (int id : ids) {
                set(id, false);
            }
            break;
        case BETWEEN:
            begin = ids[0];
            end = ids[ids.length - 1];
            setRange(begin, end, true);
            break;
        case JUST:
            reset();
        case INCLUDING:
            for (int id : ids) {
                set(id, true);
            }
            break;
        case NOT:
            for (int id : ids) {
                set(id, false);
            }
            break;
        case NOT_BETWEEN:
            begin = ids[0];
            end = ids[ids.length - 1];
            for (byte id = 0; id <= 255; ++id) {
                if (id < begin || id > end) {
                    set(id, true);
                }
            }
            break;
        }
        return this;
    }

    private boolean get(int index) {
        return flags.get(index);
    }

    public boolean isValid(int id) {
        return get(id);
    }

    private void reset() {
        flags.set(0, 255, false);
    }

    private void set(int index, boolean flag) {
        flags.set(index, flag);
    }

    private void set(int[] banned, boolean flag) {
        for (int id : banned) {
            set(id, flag);
        }
    }

    public SpawnValidator set(Range range, boolean flag) {
        switch (range) {
        case ALL:
            all();
            break;
        case DEFAULT:
            all();
            this.set(Range.LIQUIDS, false);
            int[] banned = { 6, 17, 18, 26, 50, 51, 63, 64, 71, 77, 83, 85, 90 };
            this.set(banned, false);
            this.set(0, false);
            break;
        case LIQUIDS:
            setRange(8, 11, flag);
            break;
        }
        return this;
    }

    private void setRange(int begin, int end, boolean flag) {
        for (int id = begin; id <= end; ++id) {
            set(id, flag);
        }
    }

    public static class Spawn {
        public enum Range {
            ALL,
            DEFAULT,
            LIQUIDS;
        }

        public enum Type {
            ALL,
            ALL_EXCEPT,
            BETWEEN,
            INCLUDING,
            JUST,
            NOT,
            NOT_BETWEEN;
        }
    }

    public static final SpawnValidator DEFAULT_SPAWNIN = new SpawnValidator(Type.JUST, 0),
            DEFAULT_SPAWNON = new SpawnValidator(Range.DEFAULT, false);
}