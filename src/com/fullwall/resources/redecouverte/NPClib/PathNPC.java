package com.fullwall.resources.redecouverte.NPClib;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PathEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import com.fullwall.Citizens.Constants;
import com.fullwall.resources.redecouverte.NPClib.NPCAnimator.Action;

public class PathNPC extends EntityPlayer {
	public HumanNPC npc;
	private PathEntity pathEntity;
	private Entity target;
	protected NPCAnimator animations = new NPCAnimator(this);

	private boolean targetAggro = false;
	private boolean hasAttacked = false;
	private boolean jumping = false;
	private int pathTicks = 0;
	private int pathTickLimit = -1;
	private int stationaryTicks = 0;
	private int stationaryTickLimit = -1;
	private int attackTimes = 0;
	private int attackTimesLimit = -1;
	private int prevX;
	private int prevY;
	private int prevZ;
	private float pathingRange = 16;

	public PathNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	public void updateMove() {
		hasAttacked = false;
		jumping = false;
		updateTarget();
		updatePathingState();
		if (this.pathEntity != null) {
			Vec3D vector = getVector();
			int yHeight = MathHelper.floor(this.boundingBox.b + 0.5D);
			boolean inWater = this.ac();
			boolean inLava = this.ad();

			if (vector != null) {
				double diffX = vector.a - this.locX;
				double diffZ = vector.c - this.locZ;
				double diffY = vector.b - yHeight;
				float diffYaw = getYawDifference(diffZ, diffX);

				this.yaw += diffYaw;
				if (diffY > 0.0D) {
					jumping = true;
				}
				move(); // Walk.
			}
			if (this.positionChanged && !this.pathFinished()) {
				jumping = true;
			}
			if (this.random.nextFloat() < 0.8F && (inWater || inLava)) {
				jumping = true;
			}
			if (jumping) {
				jump();
			}
		} else {
			super.c_();
			this.pathEntity = null;
		}
	}

	private float getYawDifference(double diffZ, double diffX) {
		float vectorYaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float diffYaw = vectorYaw - this.yaw;

		for (this.aA = this.aE; diffYaw < -180.0F; diffYaw += 360.0F) {
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
		// was * 2.0F;
		double length = (this.length * 1.9F);
		while (vec3d != null
				&& vec3d.d(this.locX, vec3d.b, this.locZ) < length * length) {
			// Increment path.
			this.pathEntity.a();
			// Is path finished?
			if (this.pathEntity.b()) {
				vec3d = null;
				reset();
			} else {
				vec3d = this.pathEntity.a(this);
			}
		}
		return vec3d;
	}

	private void updateTarget() {
		if (!this.hasAttacked && this.target != null) {
			this.pathEntity = this.world.findPath(this, this.target,
					pathingRange);
		}
		if (target != null) {
			// Target died.
			if (!this.target.S()) {
				resetTarget();
			}
			if (target != null && targetAggro) {
				if (this.attackTicks != 0)
					--this.attackTicks;
				float distanceToEntity = this.target.f(this);
				// If a direct line of sight exists
				if (this.e(this.target)) {
					// In range?
					if (withinAttackRange(this.target, distanceToEntity)) {
						// Attack.
						this.damageEntity(this.target);
					}
				}
			}
		}
	}

	private void updatePathingState() {
		Location loc = this.bukkitEntity.getLocation();
		if (prevX == loc.getBlockX() && prevY == loc.getBlockY()
				&& prevZ == loc.getBlockZ()) {
			++stationaryTicks;
		} else {
			stationaryTicks = 0;
		}
		++pathTicks;
		if ((pathTickLimit != -1 && pathTicks >= pathTickLimit)
				|| (stationaryTickLimit != -1 && stationaryTicks >= stationaryTickLimit)) {
			reset();
		}
		prevX = loc.getBlockX();
		prevY = loc.getBlockY();
		prevZ = loc.getBlockZ();
	}

	private void move() {
		this.a(this.az / 2, this.aA / 2);
	}

	private void jump() {
		boolean inWater = this.ac();
		boolean inLava = this.ad();
		if (inWater || inLava) {
			this.motY += 0.03999999910593033D;
		} else if (this.onGround) {
			this.motY = 0.41999998688697815D + Constants.JUMP_FACTOR;
		}
	}

	private void incrementAttackTimes() {
		if (this.attackTimesLimit != -1) {
			++this.attackTimes;
			if (this.attackTimes >= this.attackTimesLimit) {
				resetTarget();
			}
		}
	}

	private void reset() {
		this.pathTicks = 0;
		this.stationaryTicks = 0;
		this.pathEntity = null;
		this.npc.getNPCData().setLocation(npc.getPlayer().getLocation());
		this.pathTickLimit = -1;
		this.stationaryTickLimit = -1;
		this.pathingRange = 16;
	}

	private void resetTarget() {
		this.target = null;
		this.targetAggro = false;
		this.attackTimes = 0;
		this.attackTimesLimit = -1;
		reset();
	}

	private void attackEntity(EntityLiving entity) {
		this.performAction(Action.SWING_ARM);
		int damage = this.inventory.a(entity);
		LivingEntity e = (LivingEntity) entity.getBukkitEntity();
		e.damage(damage);
		hasAttacked = true;
		incrementAttackTimes();
	}

	private boolean withinAttackRange(Entity entity, float distance) {
		if (this.attackTicks <= 0 && distance < 1.5F
				&& entity.boundingBox.e > this.boundingBox.b
				&& entity.boundingBox.b < this.boundingBox.e)
			return true;
		return false;
	}

	private void damageEntity(Entity entity) {
		// Default is 20, changed to be less spammy.
		this.attackTicks = 30;
		this.attackEntity((EntityLiving) entity);
	}

	private float getBlockPathWeight(int i, int j, int k) {
		return 0.5F - this.world.m(i, j, k);
	}

	public Entity findClosestPlayer(double range) {
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
				int i1 = MathHelper.floor(this.locX + this.random.nextInt(13)
						- 6.0D);
				int j1 = MathHelper.floor(this.locY + this.random.nextInt(7)
						- 3.0D);
				int k1 = MathHelper.floor(this.locZ + this.random.nextInt(13)
						- 6.0D);
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

	private void createPathEntity(int x, int y, int z) {
		this.pathEntity = this.world.a(this, x, y, z, pathingRange);
	}

	public void setTarget(LivingEntity entity, boolean aggro, int maxTicks,
			int maxStationaryTicks, float pathingRange) {
		this.target = ((CraftLivingEntity) entity).getHandle();
		this.targetAggro = aggro;
		this.pathTickLimit = maxTicks;
		this.pathingRange = pathingRange;
		this.stationaryTickLimit = maxStationaryTicks;
	}

	public void setAttackTimes(int times) {
		this.attackTimesLimit = times;
	}

	public boolean pathFinished() {
		return pathEntity == null;
	}

	public void cancelPath() {
		reset();
	}

	public void cancelTarget() {
		resetTarget();
	}

	public void performAction(Action action) {
		this.animations.performAction(action);
	}
}