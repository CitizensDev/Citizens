package com.fullwall.resources.redecouverte.NPClib;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import com.fullwall.Citizens.Constants;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class PathNPC extends EntityPlayer {
	public HumanNPC npc;
	private PathEntity pathEntity;
	private Entity target;

	private boolean targetAggro = false;
	private boolean hasAttacked = false;
	private boolean jumping = false;
	private int pathTicks = 0;
	private int stationaryTicks = 0;
	private int pathTickLimit;
	private int stationaryTickLimit;
	private int prevX;
	private int prevY;
	private int prevZ;
	private float pathingRange = 16;

	public PathNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	public void animateArmSwing() {
		this.netServerHandler.sendPacket(new Packet18ArmAnimation(this, 1));
	}

	public void updateMove() {
		hasAttacked = false;
		jumping = false;
		updateTarget();
		updatePathingState();
		if (this.pathEntity != null) {
			Vec3D vector = getVector();
			int yHeight = MathHelper.floor(this.boundingBox.b + 0.5D);
			boolean inWater = this.Z();
			boolean inLava = this.aa();

			if (vector != null) {
				double diffX = vector.a - this.locX;
				double diffZ = vector.c - this.locZ;
				double diffY = vector.b - (double) yHeight;
				float diffYaw = getYawDifference(diffZ, diffX);

				this.yaw += diffYaw;
				if (diffY > 0.0D)
					jumping = true;
				move(); // Walk.
			}
			if (this.positionChanged && !this.pathFinished())
				jumping = true;
			if (this.random.nextFloat() < 0.8F && (inWater || inLava))
				jumping = true;
			if (jumping)
				jump();
		} else {
			super.c_();
			this.pathEntity = null;
		}
	}

	private void updatePathingState() {
		Location loc = this.bukkitEntity.getLocation();
		if (prevX == loc.getBlockX() && prevY == loc.getBlockY()
				&& prevZ == loc.getBlockZ())
			++stationaryTicks;
		else
			stationaryTicks = 0;
		++pathTicks;
		if ((pathTickLimit != -1 && pathTicks >= pathTickLimit)
				|| (stationaryTickLimit != -1 && stationaryTicks >= stationaryTickLimit)) {
			reset();
		}
		prevX = loc.getBlockX();
		prevY = loc.getBlockY();
		prevZ = loc.getBlockZ();
	}

	private float getYawDifference(double diffZ, double diffX) {
		float vectorYaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float diffYaw = vectorYaw - this.yaw;

		for (this.aw = this.aA; diffYaw < -180.0F; diffYaw += 360.0F) {
		}
		while (diffYaw >= 180.0F) {
			diffYaw -= 360.0F;
		}
		if (diffYaw > 30.0F) {
			diffYaw = 30.0F;
		}
		if (diffYaw < -30.0F) {
			diffYaw = -30.0F;
		}
		return diffYaw;
	}

	private Vec3D getVector() {
		Vec3D vec3d = pathEntity.a(this);
		for (double d = width * 1.03F; vec3d != null
				&& vec3d.d(locX, vec3d.b, locZ) < d * d;) {
			// Increment path
			pathEntity.a();
			// Is finished?
			if (pathEntity.b()) {
				vec3d = null;
				reset();
			} else {
				vec3d = pathEntity.a(this);
			}
		}
		return vec3d;
	}

	private void updateTarget() {
		if (target != null) {
			// Target died.
			if (!this.target.P()) {
				this.target = null;
				targetAggro = false;
			} else
				this.pathEntity = this.world.findPath(this, this.target,
						pathingRange);
			if (target != null && targetAggro) {
				float distanceToEntity = this.target.f(this);
				// If we're close enough to attack...
				if (this.e(this.target)) {
					// Attack!
					this.damageEntity(this.target, distanceToEntity);
					animateArmSwing();
					hasAttacked = true;
				}
			}
		}
	}

	private void damageEntity(Entity entity, float f) {
		if (this.attackTicks <= 0 && f < 2.0F
				&& entity.boundingBox.e > this.boundingBox.b
				&& entity.boundingBox.b < this.boundingBox.e) {
			this.attackTicks = 20;
			this.attackEntity((EntityLiving) entity);
		}
	}

	private void move() {
		this.a(this.av, this.aw);
	}

	private void jump() {
		boolean inWater = this.Z();
		boolean inLava = this.aa();
		if (inWater || inLava) {
			this.motY += 0.03999999910593033D;
		} else if (this.onGround) {
			this.motY = 0.41999998688697815D + Constants.JUMP_FACTOR;
		}
	}

	private void reset() {
		pathTicks = 0;
		stationaryTicks = 0;
		this.pathEntity = null;
		npc.getNPCData().setLocation(npc.getPlayer().getLocation());
		pathTickLimit = 0;
		stationaryTickLimit = 0;

	}

	private float getBlockPathWeight(int i, int j, int k) {
		return 0.5F - this.world.l(i, j, k);
	}

	private Entity findClosestPlayer(double range) {
		EntityHuman entityhuman = this.world.a(this, range);
		return entityhuman != null && this.e(entityhuman) ? entityhuman : null;
	}

	public void targetClosestPlayer(boolean aggro, double range) {
		if (this.target == null) {
			this.target = this.findClosestPlayer(range);
			this.targetAggro = aggro;
			if (this.target != null) {
				this.pathEntity = this.world.findPath(this, this.target,
						pathingRange);
			}
		}
	}

	public void takeRandomPath() {
		if (!hasAttacked && this.target != null
				&& (this.pathEntity == null || this.random.nextInt(20) == 0)) {
			this.pathEntity = this.world.findPath(this, this.target,
					pathingRange);
		} else if (!hasAttacked
				&& (this.pathEntity == null && this.random.nextInt(80) == 0 || this.random
						.nextInt(80) == 0)) {
			boolean flag = false;
			int i = -1;
			int j = -1;
			int k = -1;
			float f2 = -99999.0F;

			for (int l = 0; l < 10; ++l) {
				int i1 = MathHelper.floor(this.locX
						+ (double) this.random.nextInt(13) - 6.0D);
				int j1 = MathHelper.floor(this.locY
						+ (double) this.random.nextInt(7) - 3.0D);
				int k1 = MathHelper.floor(this.locZ
						+ (double) this.random.nextInt(13) - 6.0D);
				float f3 = this.getBlockPathWeight(i1, j1, k1);

				if (f3 > f2) {
					f2 = f3;
					i = i1;
					j = j1;
					k = k1;
					flag = true;
				}
			}
			if (flag) {
				this.pathEntity = this.world.a(this, i, j, k, pathingRange);
			}
		}
	}

	public boolean startPath(Location loc, int maxTicks,
			int maxStationaryTicks, float pathingRange) {
		this.pathTickLimit = maxTicks;
		this.stationaryTickLimit = maxStationaryTicks;
		this.pathingRange = pathingRange;
		return createPath(loc);
	}

	private boolean createPath(Location loc) {
		createPathEntity(loc);
		return pathFinished();
	}

	private void createPathEntity(Location loc) {
		createPathEntity(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public void createPathEntity(int x, int y, int z) {
		this.pathEntity = this.world.a(this, x, y, z, pathingRange);
	}

	public void setTarget(CraftPlayer player, boolean aggro, int maxTicks,
			int maxStationaryTicks, float pathingRange) {
		this.target = player.getHandle();
		this.targetAggro = aggro;
		this.pathTickLimit = maxTicks;
		this.pathingRange = pathingRange;
		this.stationaryTickLimit = maxStationaryTicks;
	}

	public boolean pathFinished() {
		return pathEntity == null;
	}

	public void attackEntity(EntityLiving entity) {
		this.animateArmSwing();
		int damage = this.inventory.a(entity);
		LivingEntity e = (LivingEntity) entity.getBukkitEntity();
		e.damage(damage);
	}
}
