package net.citizensnpcs.guards.flags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.citizensnpcs.guards.flags.FlagList.FlagType;
import net.citizensnpcs.permissions.CitizensGroup;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class FlagSorter {
    private final Predicate<LivingEntity> entitySorter = new Predicate<LivingEntity>() {
        @Override
        public boolean apply(LivingEntity entity) {
            if (CreatureTask.getCreature(entity) != null) {
                String search = StringUtils.format(CreatureTask.getCreature(entity).getType(), false);
                if (isTargetable(MOBS, search)) {
                    updateLowest(get(MOBS, search));
                    return true;
                }
                return false;
            } else if (entity instanceof Player) {
                Player player = (Player) entity;
                String name = player.getName().toLowerCase();
                if (isTargetable(PLAYERS, name)) {
                    updateLowest(get(PLAYERS, name));
                    return true;
                } else {
                    if (!PermissionManager.hasBackend()) {
                        return false;
                    }
                    if (groupCache.get(name) != null) {
                        return true;
                    }
                    Set<CitizensGroup> groups = PermissionManager.getGroups(player);
                    if (groups == null) {
                        return false;
                    }
                    List<String> transformed = Lists.newArrayList((Iterables.transform(
                            Iterables.filter(groups, groupSorter), groupToString)));
                    TreeSet<FlagInfo> sorted = getSortedFlags(GROUPS, transformed);
                    if (sorted.size() > 0) {
                        FlagInfo info = sorted.first();
                        groupCache.put(name, info);
                        updateLowest(info);
                    }
                    return sorted.size() != 0;
                }
            } else {
                if (isTargetable(MOBS, entity.getType().getName().toLowerCase())) {
                    updateLowest(get(MOBS, entity.getType().getName().toLowerCase()));
                    return true;
                }
                return false;
            }
        }

    };
    private final Map<String, FlagInfo> groupCache = new HashMap<String, FlagInfo>();
    private final FlagType GROUPS = FlagType.GROUP, PLAYERS = FlagType.PLAYER, MOBS = FlagType.MOB;
    private final Predicate<CitizensGroup> groupSorter = new Predicate<CitizensGroup>() {
        @Override
        public boolean apply(CitizensGroup group) {
            String name = group.getName().toLowerCase();
            if (!getByType(GROUPS).containsKey(name))
                name = "all";
            return getByType(GROUPS).containsKey(name) && !getByType(GROUPS).get(name).isSafe();
        }
    };

    private final Function<CitizensGroup, String> groupToString = new Function<CitizensGroup, String>() {
        @Override
        public String apply(CitizensGroup group) {
            return group.getName().toLowerCase();
        }
    };

    // TODO: perhaps we should make a sorted copy as well of the base flags.
    // Needs some cleaning up of code... perhaps less verbosity can be achieved.
    // Optimisations are definitely possible.
    private final FlagList list;
    private int lowestFound = 21;
    private final Comparator<FlagInfo> priorityComparer = new Comparator<FlagInfo>() {
        @Override
        public int compare(FlagInfo first, FlagInfo other) {
            int priority = first.priority(), second = other.priority();
            return second < priority ? 1 : second > priority ? -1 : 0;
        }
    };

    public FlagSorter(FlagList list) {
        this.list = list;
    }

    private FlagInfo get(FlagType type, String name) {
        Map<String, FlagInfo> source = getByType(type);
        if (!source.containsKey(name))
            name = "all";
        return source.get(name);
    }

    private final Map<String, FlagInfo> getByType(FlagType type) {
        return list.getFlags(type);
    }

    List<LivingEntity> getPossible(Iterable<LivingEntity> toProcess) {
        List<LivingEntity> processed = Lists.newArrayList();
        FlagInfo retrieved;
        for (LivingEntity entity : toProcess) {
            retrieved = null;
            if (NPCManager.isNPC(entity)) {
            } else if (CreatureTask.getCreature(entity) != null) {
                retrieved = get(MOBS, StringUtils.format(CreatureTask.getCreature(entity).getType(), false));
            } else if (entity instanceof Player) {
                String name = ((Player) entity).getName().toLowerCase();
                retrieved = get(PLAYERS, name) == null ? groupCache.get(name) : get(PLAYERS, name);
            } else {
                retrieved = get(MOBS, entity.getType().getName().toLowerCase());
            }
            if (retrieved != null && retrieved.priority() == lowestFound) {
                processed.add(entity);
            }
        }
        reset();
        return processed;
    }

    private TreeSet<FlagInfo> getSortedFlags(FlagType type, Collection<String> toProcess) {
        Map<String, FlagInfo> source = list.getFlags(type);
        List<FlagInfo> processed = new ArrayList<FlagInfo>();
        for (String string : toProcess) {
            if (source.get(string) == null) {
                throw new IllegalArgumentException("source didn't contain a corresponding flag");
            }
            processed.add(source.get(string));
        }
        TreeSet<FlagInfo> sorted = Sets.newTreeSet(priorityComparer);
        sorted.addAll(processed);
        return sorted;
    }

    Predicate<LivingEntity> getSorter() {
        return entitySorter;
    }

    private boolean isTargetable(FlagType type, String key) {
        Map<String, FlagInfo> search = getByType(type);
        if (!search.containsKey(key)) {
            key = "all";
        }
        return search.containsKey(key) && !search.get(key).isSafe();
    }

    private void reset() {
        lowestFound = 21;
    }

    List<LivingEntity> transformToLiving(List<Entity> entities) {
        return Lists.newArrayList(Iterables.transform(Iterables.filter(entities, livingFilterer), livingTransformer));
    }

    private void updateLowest(FlagInfo info) {
        if (info.priority() < lowestFound) {
            lowestFound = info.priority();
        }
    }

    private static final Predicate<Entity> livingFilterer = new Predicate<Entity>() {
        @Override
        public boolean apply(Entity entity) {
            return entity instanceof LivingEntity && !((LivingEntity) entity).isDead();
        }

    };

    private static final Function<Entity, LivingEntity> livingTransformer = new Function<Entity, LivingEntity>() {
        @Override
        public LivingEntity apply(Entity entity) {
            return (LivingEntity) entity;
        }
    };
}