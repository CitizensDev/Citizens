package com.citizens.Resources.NPClib.Creatures;

import java.util.Random;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.citizens.Constants;
import com.citizens.CreatureTask;
import com.citizens.Permission;
import com.citizens.NPCs.NPCManager;
import com.citizens.Properties.Properties.UtilityProperties;
import com.citizens.Utils.InventoryUtils;
import com.citizens.Utils.MessageUtils;
import com.citizens.Utils.Messaging;
import com.citizens.Utils.StringUtils;

public class EvilCreatureNPC extends CreatureNPC {
	private boolean isTame = false;

	public EvilCreatureNPC(MinecraftServer minecraftserver, World world,
			String s, ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	@Override
	public void onSpawn() {
		npc.getInventory().setItemInHand(
				new ItemStack(weapons[this.random.nextInt(weapons.length)], 1));
		super.onSpawn();
	}

	@Override
	public void doTick() {
		if (!isTame) {
			if (!hasTarget() && getClosestPlayer(this.range) != null) {
				targetClosestPlayer(true, this.range);
			}
			super.doTick();
		}
	}

	@Override
	public void onDeath() {
		ItemStack item;
		if ((item = UtilityProperties.getRandomDrop()) != null) {
			this.getEntity().getWorld()
					.dropItemNaturally(this.getLocation(), item);
		}
	}

	@Override
	public void onDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			Entity damager = e.getDamager();
			if (damager != null) {
				this.targetAggro = true;
				this.targetEntity = ((CraftEntity) damager).getHandle();
				// TODO Uncomment after BukkitContrib is updated to MC 1.7.2
				/*
				 * if (e.getEntity().isDead() && damager instanceof Player) {
				 * ServerUtils.sendAchievement((Player) damager,
				 * "Removed Herobrine.", Material.DIAMOND_SWORD); }
				 */
			}
		}
	}

	@Override
	public CreatureNPCType getType() {
		return CreatureNPCType.EVIL;
	}

	@Override
	public void onRightClick(Player player) {
		if (UtilityProperties.getNPCCount(player.getName()) >= Constants.maxNPCsPerPlayer
				&& !Permission.isAdmin(player)) {
			Messaging
					.sendError(
							player,
							"You cannot tame this Evil NPC because you have reached the maximum NPC creation limit.");
			return;
		}
		if (npc.getHandle() instanceof CreatureNPC
				&& player.getItemInHand().getTypeId() == Constants.evilNPCTameItem) {
			if (new Random().nextInt(100) <= Constants.evilNPCTameChance) {
				InventoryUtils.decreaseItemInHand(player,
						Material.getMaterial(Constants.evilNPCTameItem));
				isTame = true;
				CreatureTask.despawn(this);
				NPCManager.register(npc.getName(), player.getLocation(),
						player.getName());
				player.sendMessage(ChatColor.GREEN + "You have tamed "
						+ StringUtils.wrap(npc.getStrippedName())
						+ "! You can now toggle it to be any type.");
				// TODO Uncomment after BukkitContrib is updated to MC 1.7.2
				// ServerUtils.sendAchievement(player, "Misunderstood.",
				// Material.CAKE);
			} else {
				Messaging
						.send(player,
								this.npc,
								ChatColor.RED
										+ "["
										+ npc.getStrippedName()
										+ "] "
										+ ChatColor.WHITE
										+ MessageUtils
												.getRandomMessage(Constants.failureToTameMessages));
			}
		}
	}

	@Override
	public void onLeftClick(Player player) {
	}
}