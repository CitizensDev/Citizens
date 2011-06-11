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

import com.fullwall.resources.redecouverte.NPClib.CreatureNPC;

public class EvilCreatureNPC extends CreatureNPC {

	public EvilCreatureNPC(MinecraftServer minecraftserver, World world,
			String s, ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	@Override
	public void onSpawn() {
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
	public boolean isValidSpawn(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CreatureNPCType getType() {
		return CreatureNPCType.EVIL;
	}

}
