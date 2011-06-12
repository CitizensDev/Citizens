package com.fullwall.resources.redecouverte.NPClib.Creatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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
		npc.getInventory()
				.setItemInHand(
						new ItemStack(weapons[new Random()
								.nextInt(weapons.length)], 1));
		npc.setEvil(true);
		super.onSpawn();
	}

	@Override
	public void doTick() {
		if (!this.npc.getEvil().isTame())
			super.doTick();
	}

	@Override
	public void onDeath() {
		this.getEntity()
				.getWorld()
				.dropItemNaturally(this.getEntity().getLocation(),
						new CraftItemStack(this.inventory.getItemInHand()));
		Random random = new Random();
		List<Integer> itemIDs = new ArrayList<Integer>();
		for (int id = 0; id < 400; id++) {
			if (Material.getMaterial(id) != null) {
				itemIDs.add(id);
			}
		}
		Material randomItem = Material.getMaterial(random.nextInt(itemIDs
				.size()));
		if (randomItem == null) {
			return;
		}
		this.getEntity()
				.getWorld()
				.dropItemNaturally(this.getEntity().getLocation(),
						new ItemStack(randomItem, random.nextInt(2)));
	}

	@Override
	public void onDamage(EntityDamageEvent event) {
	}

	@Override
	public CreatureNPCType getType() {
		return CreatureNPCType.EVIL;
	}

}
