package com.temp.resources.redecouverte.NPClib.Creatures;

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

import com.temp.Constants;
import com.temp.CreatureTask;
import com.temp.NPCs.NPCManager;
import com.temp.Properties.Properties.UtilityProperties;
import com.temp.Utils.InventoryUtils;
import com.temp.Utils.MessageUtils;
import com.temp.Utils.Messaging;
import com.temp.Utils.StringUtils;

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
			Entity entity = e.getDamager();
			if (entity != null) {
				this.targetAggro = true;
				this.targetEntity = ((CraftEntity) entity).getHandle();
			}
		}
	}

	@Override
	public CreatureNPCType getType() {
		return CreatureNPCType.EVIL;
	}

	@Override
	public void onRightClick(Player player) {
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
			} else {
				Messaging
						.send(player,
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