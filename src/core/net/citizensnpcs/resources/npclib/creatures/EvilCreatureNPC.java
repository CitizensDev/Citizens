package net.citizensnpcs.resources.npclib.creatures;

import net.citizensnpcs.Settings;
import net.citizensnpcs.api.event.NPCCreateEvent.NPCCreateReason;
import net.citizensnpcs.api.event.NPCRemoveEvent.NPCRemoveReason;
import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.utils.InventoryUtils;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.StringUtils;
import net.minecraft.server.v1_6_R1.EntityHuman;
import net.minecraft.server.v1_6_R1.MinecraftServer;
import net.minecraft.server.v1_6_R1.PlayerInteractManager;
import net.minecraft.server.v1_6_R1.World;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EvilCreatureNPC extends CreatureNPC {
    private boolean isTame = false;

    public EvilCreatureNPC(MinecraftServer minecraftserver, World world, String s,
            PlayerInteractManager iteminworldmanager) {
        super(minecraftserver, world, s, iteminworldmanager);
    }

    @Override
    public void doTick() {
        if (isTame)
            return;
        EntityHuman closest = getClosestPlayer(this.range);
        if (!hasTarget() && closest != null) {
            if (!PermissionManager.hasPermission((Player) closest.getBukkitEntity(), "citizens.evils.immune")) {
                targetClosestPlayer(true, this.range);
            }
        }
        super.doTick();
    }

    @Override
    public CreatureNPCType getType() {
        return CreatureNPCType.EVIL;
    }

    @Override
    public void onDeath() {
        ItemStack item = UtilityProperties.getRandomDrop(Settings.getString("EvilDrops"));
        if (item == null)
            return;
        this.getEntity().getWorld().dropItemNaturally(this.getLocation(), item);
    }

    @Override
    public void onLeftClick(Player player) {
    }

    @Override
    public void onRightClick(Player player) {
        if (!PermissionManager.canCreate(player)) {
            Messaging.sendError(player,
                    "You cannot tame this Evil NPC because you have reached the NPC creation limit.");
            return;
        }
        if (player.getItemInHand().getTypeId() != Settings.getInt("EvilTameItem"))
            return;
        if (random.nextInt(100) <= Settings.getInt("EvilTameChance")) {
            InventoryUtils.decreaseItemInHand(player);
            isTame = true;
            CreatureTask.despawn(this, NPCRemoveReason.OTHER);
            NPCManager.register(npc.getName(), player.getLocation(), player.getName(), NPCCreateReason.RESPAWN);
            player.sendMessage(ChatColor.GREEN + "You have tamed " + StringUtils.wrap(npc.getName())
                    + "! You can now toggle it to be any type.");
        } else {
            Messaging.send(
                    player,
                    this.npc,
                    StringUtils.colourise(Settings.getString("ChatFormat").replace("%name%", npc.getName()))
                            + ChatColor.WHITE
                            + MessageUtils.getRandomMessage(Settings.getString("EvilFailedTameMessages")));
        }
    }

    @Override
    public void onSpawn() {
        npc.getInventory().setItemInHand(new ItemStack(weapons[this.random.nextInt(weapons.length)], 1));
        this.setHealth((float) Settings.getDouble("EvilHealth"));
        super.onSpawn();
    }
}