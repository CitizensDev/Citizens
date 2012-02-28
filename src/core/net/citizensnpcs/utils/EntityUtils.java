package net.citizensnpcs.utils;

import net.citizensnpcs.resources.npclib.creatures.CreatureNPCType;

import org.bukkit.entity.EntityType;

public class EntityUtils {

    public static boolean validType(String name) {
        return validType(name, false);
    }

    public static boolean validType(String name, boolean both) {
        String formatted = StringUtils.capitalise(name.toLowerCase()).replace(" ", "");
        if (formatted.equals("Pigzombie")) {
            formatted = "PigZombie";
        }
        if (formatted.equals("Cavespider")) {
            formatted = "CaveSpider";
        }
        if (formatted.equals("Enderdragon")) {
            formatted = "EnderDragon";
        }
        if (formatted.equals("Mushroomcow")) {
            formatted = "MushroomCow";
        }
        return EntityType.fromName(formatted) != null || (both && CreatureNPCType.fromName(formatted) != null);
    }
}
