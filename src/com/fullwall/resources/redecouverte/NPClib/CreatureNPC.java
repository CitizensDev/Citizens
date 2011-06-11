package com.fullwall.resources.redecouverte.NPClib;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.fullwall.Citizens.Constants;

public abstract class CreatureNPC extends CraftNPC {
	public enum CreatureNPCType {
		EVIL(Constants.maxEvilNPCs);
		private int max;

		CreatureNPCType(int max) {
			this.max = max;
		}

		public int getMaxSpawnable() {
			return this.max;
		}
	}

	public CreatureNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	public Player getEntity() {
		return (Player) this.bukkitEntity;
	}

	public abstract void onSpawn();

	public void doTick() {
		if (!hasTarget() && findClosestPlayer(25) != null) {
			targetClosestPlayer(true, 25);
		} else {
			updateMove();
		}
	}

	public abstract void onDeath();

	public abstract void onDamage(EntityDamageEvent event);

	public abstract boolean isValidSpawn(World world, int x, int y, int z);

	public abstract CreatureNPCType getType();

}
