package com.fullwall.resources.redecouverte.NPClib.Creatures;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PirateCreatureNPC extends CreatureNPC {
	// Another Wall of Text Explaining Pirates -
	// spawn in water on boats come ashore to steal:
	// 1. player inventories
	// 2. chests/dispensers/furnaces (they place a sign that marks that they
	// stole from there)
	// 3. trader's stock
	// 4. player's economy money
	// once their inventory is full (or a certain amount of on-shore ticks have
	// passed), they return
	// to their boat to escape; if they die, they drop all of the items that
	// they stole and return all economy money .... anything else?
	public PirateCreatureNPC(MinecraftServer minecraftserver, World world,
			String s, ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onSpawn() {
		// perhaps it should create a boat for itself then get in.
		npc.getInventory().setItemInHand(
				new ItemStack(weapons[this.random.nextInt(weapons.length)], 1));
	}

	@Override
	public void doTick() {
		// findShore();
	}

	@Override
	public void onDeath() {
		for (ItemStack item : this.npc.getInventory().getContents()) {
			this.getEntity().getWorld()
					.dropItemNaturally(this.getLocation(), item);
		}
	}

	@Override
	public void onDamage(EntityDamageEvent event) {
	}

	@Override
	public CreatureNPCType getType() {
		return CreatureNPCType.PIRATE;
	}

	@Override
	public void onRightClick(Player player) {
	}

	@Override
	public void onLeftClick(Player player) {
	}
}