package com.fullwall.resources.redecouverte.NPClib.Creatures;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class EvilCreatureNPC extends CreatureNPC {
	public EvilCreatureNPC(MinecraftServer minecraftserver, World world,
			String s, ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	private final Integer[] weapons = { 261, 267, 268, 272, 276, 283 };

	@Override
	public void onSpawn() {
		npc.getInventory().setItemInHand(
				new ItemStack(weapons[this.random.nextInt(weapons.length)], 1));
		npc.setEvil(true);
		super.onSpawn();
	}

	@Override
	public void doTick() {
		if (!this.npc.getEvil().isTame()) {
			if (!hasTarget() && findClosestPlayer(this.range) != null)
				targetClosestPlayer(true, this.range);
			super.doTick();

		}
	}

	@Override
	public void onDeath() {
		this.getEntity()
				.getWorld()
				.dropItemNaturally(this.getLocation(),
						new CraftItemStack(this.inventory.getItemInHand()));
		List<Integer> itemIDs = new ArrayList<Integer>();
		for (Material mat : Material.values())
			itemIDs.add(mat.getId());
		Material randomItem = Material.getMaterial(this.random.nextInt(itemIDs
				.size()));
		if (randomItem == null) {
			return;
		}
		this.getEntity()
				.getWorld()
				.dropItemNaturally(this.getLocation(),
						new ItemStack(randomItem, this.random.nextInt(2)));
	}

	@Override
	public void onDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			Entity entity = ((EntityDamageByEntityEvent) event).getDamager();
			if (entity != null) {
				this.targetAggro = true;
				this.target = ((CraftEntity) entity).getHandle();
			}
		}
		super.onDamage(event);
	}

	@Override
	public CreatureNPCType getType() {
		return CreatureNPCType.EVIL;
	}
}
