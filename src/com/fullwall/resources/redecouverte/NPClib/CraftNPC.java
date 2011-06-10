package com.fullwall.resources.redecouverte.NPClib;

import java.util.logging.Logger;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

public class CraftNPC extends PathNPC {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("Minecraft");
	private int lastTargetId;
	private long lastBounceTick;
	private int lastBounceId;

	public CraftNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);

		NetworkManager netMgr = new NPCNetworkManager(new NPCSocket(),
				"npc mgr", new NetHandler() {
					@Override
					public boolean c() {
						return false;
					}
				});
		this.netServerHandler = new NPCNetHandler(minecraftserver, this, netMgr);
		// netMgr.a(this.netServerHandler);
		this.lastTargetId = -1;
		this.lastBounceId = -1;
		this.lastBounceTick = 0;
	}

	@Override
	public boolean a(EntityHuman entity) {
		EntityTargetEvent event = new NPCEntityTargetEvent(getBukkitEntity(),
				entity.getBukkitEntity(),
				NPCEntityTargetEvent.NpcTargetReason.NPC_RIGHTCLICKED);
		CraftServer server = ((WorldServer) this.world).getServer();
		server.getPluginManager().callEvent(event);

		return super.a(entity);
	}

	@Override
	public void b(EntityHuman entity) {
		if (lastTargetId == -1 || lastTargetId != entity.id) {
			EntityTargetEvent event = new NPCEntityTargetEvent(
					getBukkitEntity(), entity.getBukkitEntity(),
					NPCEntityTargetEvent.NpcTargetReason.CLOSEST_PLAYER);
			CraftServer server = ((WorldServer) this.world).getServer();
			server.getPluginManager().callEvent(event);
		}
		lastTargetId = entity.id;

		super.b(entity);
	}

	@Override
	public void c(Entity entity) {
		if (lastBounceId != entity.id
				|| System.currentTimeMillis() - lastBounceTick > 1000) {
			EntityTargetEvent event = new NPCEntityTargetEvent(
					getBukkitEntity(), entity.getBukkitEntity(),
					NPCEntityTargetEvent.NpcTargetReason.NPC_BOUNCED);
			CraftServer server = ((WorldServer) this.world).getServer();
			server.getPluginManager().callEvent(event);
			lastBounceTick = System.currentTimeMillis();
		}
		lastBounceId = entity.id;

		super.c(entity);
	}

	@Override
	public void a(Entity entity) {
		System.out.println(entity);
		super.a(entity);
	}

	@Override
	public void a(EntityLiving entityliving) {
		System.out.println(entityliving);
		super.a(entityliving);
	}

	public void applyGravity() {
		if (chunkLoaded()
				&& (!this.onGround || ((Player) this.getBukkitEntity())
						.getEyeLocation().getY() % 1 <= 0.62)) {
			float yaw = this.yaw;
			float pitch = this.pitch;
			this.a(0, 0);
			this.yaw = yaw;
			this.pitch = pitch;
		}
	}

	private boolean chunkLoaded() {
		return this
				.getBukkitEntity()
				.getWorld()
				.isChunkLoaded(
						this.getBukkitEntity().getLocation().getBlock()
								.getChunk());
	}
}