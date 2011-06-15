package com.fullwall.resources.redecouverte.NPClib.Creatures;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Properties.Properties.UtilityProperties;

public class EvilCreatureNPC extends CreatureNPC {
	public EvilCreatureNPC(MinecraftServer minecraftserver, World world,
			String s, ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	private final Integer[] weapons = { 267, 268, 272, 276, 283 };

	@Override
	public void onSpawn() {
		npc.getInventory().setItemInHand(
				new ItemStack(weapons[this.random.nextInt(weapons.length)], 1));
		super.onSpawn();
	}

	@Override
	public void doTick() {
		if (!this.npc.getEvil().isTame()) {
			if (!hasTarget() && findClosestPlayer(this.range) != null) {
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
				this.target = ((CraftEntity) entity).getHandle();
			}
		}
	}

	@Override
	public CreatureNPCType getType() {
		return CreatureNPCType.EVIL;
	}

	@Override
	public void onRightClick(Player player) {
		if (this.npc.isEvil()) {
			this.npc.getEvil().onRightClick(player, this.npc);
		}
	}

	@Override
	public void onLeftClick(Player player) {
	}
}