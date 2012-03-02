package net.citizensnpcs.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtils {

    // Remove items from a player's current held slot
    public static void decreaseItemInHand(Player player) {
        player.setItemInHand(decreaseItemStack(player.getItemInHand()));
    }

    public static ItemStack decreaseItemStack(ItemStack stack) {
        if (stack.getTypeId() == 0) {
            return null;
        }
        int amount = stack.getAmount() - 1;
        if (amount == 0) {
            stack = null;
        } else {
            stack.setAmount(amount);
        }
        return stack;
    }

    // Get that an item is a tool
    public static boolean isTool(int id) {
        return (id >= 256 && id <= 259) || (id >= 267 && id <= 279) || (id >= 283 && id <= 286)
                || (id >= 290 && id <= 294) || id == 346 || id == 359 || id == 261;
    }

    // Get if an item is armor
    public static boolean isArmor(int id) {
        return id >= 298 && id <= 317;
    }

    @SuppressWarnings("deprecation")
    public static void removeItems(Player player, Material material, int amount, int slot) {
        int remaining = amount;
        ItemStack[] contents = player.getInventory().getContents();
        if (slot != -1) {
            ItemStack item = contents[slot];
            if (item != null && item.getType() == material) {
                if (remaining - item.getAmount() < 0) {
                    item.setAmount(item.getAmount() - remaining);
                    remaining = 0;
                } else {
                    remaining -= item.getAmount();
                    item = null;
                }
                if (item != null && item.getAmount() == 0)
                    item = null;
                contents[slot] = item;
            }
        }
        if (remaining <= 0) {
            player.getInventory().setContents(contents);
            player.updateInventory();
            return;
        }
        for (int i = 0; i != contents.length; ++i) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == material) {
                if (remaining - item.getAmount() < 0) {
                    item.setAmount(item.getAmount() - remaining);
                    remaining = 0;
                } else {
                    remaining -= item.getAmount();
                    item = null;
                }
                if (item != null && item.getAmount() == 0)
                    item = null;
                contents[i] = item;
                if (remaining <= 0)
                    break;
            }
        }
        player.getInventory().setContents(contents);
        player.updateInventory();
    }

    public static void removeItems(Player player, Material material, int amount) {
        removeItems(player, material, amount, -1);
    }

    public static int getRemainder(Player player, Material material, int amount) {
        int remaining = amount;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                if (remaining - item.getAmount() < 0) {
                    remaining = 0;
                } else {
                    remaining -= item.getAmount();
                }
                if (remaining <= 0)
                    break;
            }
        }
        return remaining;
    }

    public static boolean has(Player player, Material material, int amount) {
        return getRemainder(player, material, amount) <= 0;
    }

    public static boolean has(Player player, ItemStack stack) {
        return has(player, stack.getType(), stack.getAmount());
    }

    public static void removeItems(Player player, ItemStack buying, int slot) {
        removeItems(player, buying.getType(), buying.getAmount(), slot);
    }

    public static boolean isHelmet(int itemID) {
        return itemID == 298 || itemID == 302 || itemID == 306 || itemID == 310 || itemID == 314 || itemID == 86
                || itemID == 91;
    }

    public static boolean isChestplate(int itemID) {
        return itemID == 299 || itemID == 303 || itemID == 307 || itemID == 311 || itemID == 315;
    }

    public static boolean isLeggings(int itemID) {
        return itemID == 300 || itemID == 304 || itemID == 308 || itemID == 312 || itemID == 316;
    }

    public static boolean isBoots(int itemID) {
        return itemID == 301 || itemID == 305 || itemID == 309 || itemID == 313 || itemID == 317;
    }

    public enum Armor {
        HELMET(0),
        CHESTPLATE(1),
        LEGGINGS(2),
        BOOTS(3);
        private final int slot;
        private static final Map<Integer, Armor> slots = new HashMap<Integer, Armor>();

        Armor(int slot) {
            this.slot = slot;
        }

        public ItemStack get(PlayerInventory inventory) {
            switch (this) {
            case HELMET:
                return inventory.getHelmet();
            case CHESTPLATE:
                return inventory.getChestplate();
            case LEGGINGS:
                return inventory.getLeggings();
            case BOOTS:
                return inventory.getBoots();
            }
            return null;
        }

        public int getSlot() {
            return this.slot;
        }

        // get Armor from a slot ID
        public static Armor getArmor(int slot) {
            return slots.get(slot);
        }

        // get Armor from an item ID
        public static Armor getArmorSlot(int itemID) {
            if (isHelmet(itemID)) {
                return Armor.HELMET;
            } else if (isChestplate(itemID)) {
                return Armor.CHESTPLATE;
            } else if (isLeggings(itemID)) {
                return Armor.LEGGINGS;
            } else if (isBoots(itemID))
                return Armor.BOOTS;
            return null;
        }

        public void set(PlayerInventory inventory, ItemStack item) {
            switch (this) {
            case HELMET:
                inventory.setHelmet(item);
                break;
            case CHESTPLATE:
                inventory.setChestplate(item);
                break;
            case LEGGINGS:
                inventory.setLeggings(item);
                break;
            case BOOTS:
                inventory.setBoots(item);
                break;
            }
        }

        static {
            for (Armor armor : values()) {
                slots.put(armor.getSlot(), armor);
            }
        }
    }
}