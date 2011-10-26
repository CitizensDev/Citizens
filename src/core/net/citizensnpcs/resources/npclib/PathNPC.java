package net.citizensnpcs.resources.npclib;

import net.citizensnpcs.Plugins;
import net.citizensnpcs.Settings;
import net.citizensnpcs.resources.npclib.NPCAnimator.Animation;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PathEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.flags.DefaultFlag;

public class PathNPC extends EntityPlayer {
	public HumanNPC npc;
	private PathEntity path;

	protected final NPCAnimator animations = new NPCAnimator(this);
	protected Entity targetEntity;
	protected boolean targetAggro = false;
	protected boolean jumping = false;
	protected boolean randomPather = false;
	protected boolean autoPathToTarget = true;
	protected float pathingRange = 16;

	private boolean hasAttacked = false;
	private int pathTicks = 0;
	private int pathTickLimit = -1;
	private int stationaryTicks = 0;
	private int stationaryTickLimit = -1;
	private int attackTimes = 0;
	private int attackTimesLimit = -1;
	private int prevX, prevY, prevZ;
	private final AutoPathfinder autoPathfinder;
	private static final double JUMP_FACTOR = 0.07D;

	public PathNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		this(minecraftserver, world, s, iteminworldmanager,
				new MinecraftAutoPathfinder());
	}

	public PathNPC(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager,
			AutoPathfinder autoPathfinder) {
		super(minecraftserver, world, s, iteminworldmanager);
		this.autoPathfinder = autoPathfinder;
	}

	private void attackEntity(Entity entity) {
		this.attackTicks = 20; // Possibly causes attack spam (maybe higher?).
		if (isHoldingBow()
				&& distance(entity) >= Settings.getDouble("MinArrowRange")) {
			NPCManager.faceEntity(this.npc, entity.getBukkitEntity());

			// make inaccuracies.
			boolean up = this.random.nextBoolean();
			this.yaw += this.random.nextInt(5) * (up ? 1 : -1);

			up = this.random.nextBoolean();
			this.pitch += this.random.nextInt(5) * (up ? 1 : -1);

			this.getPlayer().shootArrow();
		} else {
			this.performAction(Animation.SWING_ARM);
			LivingEntity e = (LivingEntity) entity.getBukkitEntity();
			e.damage(this.inventory.a(entity));
		}
		hasAttacked = true;
		incrementAttackTimes();
	}

	public void cancelPath() {
		reset();
	}

	public void cancelTarget() {
		resetTarget();
	}

	PathEntity createPathEntity(int x, int y, int z) {
		return this.world.a(this, x, y, z, pathingRange);
	}

	void createPathEntity(Location loc) {
		if (loc == null)
			return;
		this.path = createPathEntity(loc.getBlockX(), loc.getBlockY(),
				loc.getBlockZ());
	}

	private double distance(Entity entity) {
		return entity.getBukkitEntity().getLocation()
				.distance(this.getBukkitEntity().getLocation());
	}

	protected EntityHuman getClosestPlayer(double range) {
		EntityHuman entityhuman = this.world.findNearbyPlayer(this, range);
		return entityhuman != null && isInSight(entityhuman) ? entityhuman
				: null;
	}

	private Vec3D getPathVector() {
		Vec3D vec3d = path.a(this);
		double length = (this.length * 1.82F);
		// 2.0 -> 1.82 - closer to destination before stopping.
		while (vec3d != null
				&& vec3d.d(this.locX, vec3d.b, this.locZ) < length * length) {
			this.path.a(); // Increment path index.
			// Is path finished?
			if (this.path.b()) {
				vec3d = null;
				reset();
			} else {
				vec3d = this.path.a(this);
			}
		}
		return vec3d;
	}

	private Player getPlayer() {
		return (Player) this.bukkitEntity;
	}

	public int getStationaryTicks() {
		return this.stationaryTicks;
	}

	private float getYawDifference(double diffZ, double diffX) {
		float vectorYaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float diffYaw = vectorYaw - this.yaw;

		for (this.aQ = this.aU; diffYaw < -180.0F; diffYaw += 360.0F) {
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

	private void handleMove(Vec3D vector) {
		int yHeight = MathHelper.floor(this.boundingBox.b + 0.5D);
		boolean inWater = this.getPlayer().getRemainingAir() < 20;
		boolean onFire = this.getPlayer().getFireTicks() > 0;
		if (vector != null) {
			double diffX = vector.a - this.locX;
			double diffZ = vector.c - this.locZ;
			double diffY = vector.b - yHeight;
			float diffYaw = getYawDifference(diffZ, diffX);

			this.yaw += diffYaw;
			if (diffY > 0.0D) {
				jumping = true;
			}
			// Walk.
			moveOnCurrentHeading();
		}
		if (this.positionChanged && !this.pathFinished()) {
			jumping = true;
		}
		if (this.random.nextFloat() < 0.8F && (inWater || onFire)) {
			jumping = true;
		}
		if (jumping) {
			jump(inWater, onFire);
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

	private boolean isHoldingBow() {
		return getPlayer().getItemInHand() != null
				&& getPlayer().getItemInHand().getType() == Material.BOW;
	}

	private boolean isInSight(Entity entity) {
		return this.f(entity);
	}

	private boolean isWithinAttackRange(Entity entity, double distance) {
		// Bow distance from EntitySkeleton.
		// Other from EntityCreature.
		return this.attackTicks <= 0
				&& ((isHoldingBow() && (distance > Settings
						.getDouble("MinArrowRange") && distance < Settings
						.getDouble("MaxArrowRange"))) || (distance < 1.5F
						&& entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e));
	}

	private void jump(boolean inWater, boolean inLava) {
		// Both magic values taken from minecraft source.
		if (inWater || inLava) {
			this.motY += 0.04D;
		} else if (this.onGround) {
			this.motY = 0.42D + JUMP_FACTOR;
			// Augment defaults to actually get over a block.
		}
	}

	private void moveOnCurrentHeading() {
		this.a(this.aP, this.aQ);
	}

	public void moveTick() {
		if (this.dead) {
			if (this.targetEntity != null || this.path != null)
				resetTarget();
			return;
		}
		hasAttacked = false;
		jumping = false;
		if (randomPather) {
			takeRandomPath();
		}
		updateTarget();
		updatePathingState();
		if (this.path != null) {
			Vec3D vector = getPathVector();
			if (vector != null) {
				handleMove(vector);
			}
		} else {
			this.path = null;
		}
		--this.attackTicks;
		--this.noDamageTicks; // Update entity
	}

	public boolean pathFinished() {
		return path == null;
	}

	public void performAction(Animation action) {
		this.animations.performAnimation(action);
	}

	private void reset() {
		this.path = null;
		this.pathTicks = this.stationaryTicks = 0;
		this.pathTickLimit = this.stationaryTickLimit = -1;
		this.pathingRange = 16;
	}

	private void resetTarget() {
		this.targetEntity = null;
		this.targetAggro = false;
		this.attackTimes = 0;
		this.attackTimesLimit = -1;
		reset();
	}

	public void setAttackTimes(int times) {
		this.attackTimesLimit = times;
	}

	public void setTarget(LivingEntity entity, boolean aggro, int maxTicks,
			int maxStationaryTicks, double range) {
		if (Plugins.worldGuardEnabled()
				&& Settings.getBoolean("DenyBlockedPVPTargets")
				&& entity instanceof Player) {
			if (!Plugins.worldGuard.getGlobalRegionManager().allows(
					DefaultFlag.PVP, entity.getLocation()))
				return;
		}
		this.targetEntity = ((CraftLivingEntity) entity).getHandle();
		this.targetAggro = aggro;
		this.pathTickLimit = maxTicks;
		this.pathingRange = (float) range;
		this.stationaryTickLimit = maxStationaryTicks;
	}

	public boolean startPath(Location loc, int maxTicks,
			int maxStationaryTicks, double range) {
		this.pathTickLimit = maxTicks;
		this.stationaryTickLimit = maxStationaryTicks;
		this.pathingRange = (float) range;

		createPathEntity(loc);
		return pathFinished();
	}

	private void takeRandomPath() {
		if (!hasAttacked && this.targetEntity != null
				&& (this.path == null || this.random.nextInt(20) == 0)) {
			this.path = this.world.findPath(this, this.targetEntity,
					pathingRange);
		} else if (!hasAttacked && this.path == null)
			autoPathfinder.find(this);
	}

	public void targetClosestPlayer(boolean aggro, double range) {
		this.targetEntity = this.getClosestPlayer(range);
		this.targetAggro = aggro;
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

	private void updateTarget() {
		if (!this.hasAttacked && this.targetEntity != null && autoPathToTarget) {
			this.path = this.world.findPath(this, this.targetEntity,
					pathingRange);
		}
		if (targetEntity != null) {
			if (this.targetEntity.dead) {
				resetTarget();
			}
			if (targetEntity != null && targetAggro) {
				if (isInSight(this.targetEntity)) {
					if (isWithinAttackRange(this.targetEntity,
							distance(this.targetEntity))) {
						this.attackEntity(this.targetEntity);
					}
				}
			}
		}
	}
}