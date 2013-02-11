package net.citizensnpcs.resources.npclib;

import net.citizensnpcs.Plugins;
import net.citizensnpcs.Settings;
import net.citizensnpcs.resources.npclib.NPCAnimator.Animation;
import net.citizensnpcs.resources.npclib.creatures.CreatureNPC;
import net.citizensnpcs.utils.PacketUtils;
import net.minecraft.server.v1_4_R1.Entity;
import net.minecraft.server.v1_4_R1.EntityHuman;
import net.minecraft.server.v1_4_R1.EntityPlayer;
import net.minecraft.server.v1_4_R1.MathHelper;
import net.minecraft.server.v1_4_R1.MinecraftServer;
import net.minecraft.server.v1_4_R1.Packet5EntityEquipment;
import net.minecraft.server.v1_4_R1.PathEntity;
import net.minecraft.server.v1_4_R1.PlayerInteractManager;
import net.minecraft.server.v1_4_R1.Vec3D;
import net.minecraft.server.v1_4_R1.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.flags.DefaultFlag;

//TODO: FIX UP THIS WHOLE CLASS
public class PathNPC extends EntityPlayer {
    protected final NPCAnimator animations = new NPCAnimator(this);
    private int attackTimes = 0;
    private int attackTimesLimit = -1;

    private AutoPathfinder autoPathfinder;
    protected boolean autoPathToTarget = true;
    private Location dest;
    private boolean hasAttacked = false;
    public HumanNPC npc;
    private PathEntity path;

    protected float pathingRange = 16;
    private int pathTickLimit = -1;
    private int pathTicks = 0;
    private int prevX, prevY, prevZ;
    protected boolean randomPather = false;
    private int stationaryTickLimit = -1;
    private int stationaryTicks = 0;
    protected boolean targetAggro = false;
    protected Entity targetEntity;

    public PathNPC(MinecraftServer minecraftserver, World world, String s, PlayerInteractManager iteminworldmanager) {
        this(minecraftserver, world, s, iteminworldmanager, new MinecraftAutoPathfinder());
    }

    public PathNPC(MinecraftServer minecraftserver, World world, String s, PlayerInteractManager iteminworldmanager,
            AutoPathfinder autoPathfinder) {
        super(minecraftserver, world, s, iteminworldmanager);
        this.autoPathfinder = autoPathfinder;
    }

    private void attackEntity(Entity entity) {
        this.attackTicks = 20; // Possibly causes attack spam (maybe higher?).
        if (isHoldingBow() && distance(entity) >= Settings.getDouble("MinArrowRange")) {
            NPCManager.faceEntity(this.npc, entity.getBukkitEntity());

            // make inaccuracies.
            boolean up = this.random.nextBoolean();
            this.yaw += this.random.nextInt(5) * (up ? 1 : -1);

            up = this.random.nextBoolean();
            this.pitch += this.random.nextInt(5) * (up ? 1 : -1);

            this.getBukkitEntity().launchProjectile(Arrow.class);
        } else {
            this.performAction(Animation.SWING_ARM);
            LivingEntity other = (LivingEntity) entity.getBukkitEntity();
            other.damage(this.inventory.a(entity));
        }
        hasAttacked = true;

        if (this.attackTimesLimit == -1)
            return;
        ++this.attackTimes;
        if (this.attackTimes >= this.attackTimesLimit) {
            cancelTarget();
        }
    }

    public void cancelPath() {
        this.path = null;
        this.dest = null;
        this.pathTicks = this.stationaryTicks = 0;
        this.pathTickLimit = this.stationaryTickLimit = -1;
        this.pathingRange = 16;
    }

    public void cancelTarget() {
        this.targetEntity = null;
        this.targetAggro = false;
        this.attackTimes = 0;
        this.attackTimesLimit = -1;
        cancelPath();
    }

    PathEntity createPathEntity(int x, int y, int z) {
        return this.world.a(this, x, y, z, pathingRange, true, false, false, true);
    }

    private double distance(Entity entity) {
        return entity.getBukkitEntity().getLocation().distance(this.getBukkitEntity().getLocation());
    }

    protected EntityHuman getClosestPlayer(double range) {
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, range);
        return entityhuman != null && isInSight(entityhuman) ? entityhuman : null;
    }

    private Vec3D getPathVector() {
        Vec3D vec3d = path.a(this);
        double length = (this.width * 2.0F);
        while (vec3d != null && vec3d.d(this.locX, vec3d.d, this.locZ) < length * length) {
            this.path.a(); // Increment path index.
            // Is path finished?
            if (this.path.b()) {
                vec3d = null;
                cancelPath();
            } else {
                vec3d = this.path.a(this);
            }
        }
        return vec3d;
    }

    public int getStationaryTicks() {
        return this.stationaryTicks;
    }

    private float getYawDifference(double diffZ, double diffX) {
        float vectorYaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float diffYaw = vectorYaw - this.yaw;

        this.bC = this.bG;
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
        boolean inWater = this.getBukkitEntity().getRemainingAir() < 20;
        boolean onFire = this.getBukkitEntity().getFireTicks() > 0;
        if (vector != null) {
            double diffX = vector.c - this.locX;
            double diffZ = vector.e - this.locZ;
            double diffY = vector.d - yHeight;
            float diffYaw = getYawDifference(diffZ, diffX);

            this.yaw += diffYaw;
            this.az += diffYaw;
            if (diffY > 0.0D) {
                jump();
            }
            // TODO: adjust pitch.
            // Walk.
            this.e(this.bC, this.bD);
        }
        if (this.positionChanged && !this.pathFinished()) {
            jump();
        }
        if (this.random.nextFloat() < 0.8F && (inWater || onFire)) {
            this.motY += 0.04D;
        }
    }

    public boolean isAutoPathfinder() {
        return autoPathfinder != null;
    }

    private boolean isHoldingBow() {
        return getBukkitEntity().getItemInHand() != null && getBukkitEntity().getItemInHand().getType() == Material.BOW;
    }

    public boolean isInSight(Entity entity) {
        return this.n(entity);
    }

    private boolean isWithinAttackRange(Entity entity, double distance) {
        // Distance from EntityCreature.
        return this.attackTicks <= 0
                && ((isHoldingBow() && (distance > Settings.getDouble("MinArrowRange") && distance < Settings
                        .getDouble("MaxArrowRange"))) || (distance < 1.5F && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e)
                        && isInSight(entity));
    }

    protected void jump() {
        if (this.onGround) {
            this.motY = 0.42D + JUMP_FACTOR;
            // Augment defaults to actually get over a block.
        }
    }

    private final net.minecraft.server.v1_4_R1.ItemStack[] previousEquipment = { null, null, null, null, null };

    private void updateEquipment() {
        for (int i = 0; i < previousEquipment.length; i++) {
            net.minecraft.server.v1_4_R1.ItemStack previous = previousEquipment[i];
            net.minecraft.server.v1_4_R1.ItemStack current = getEquipment(i);
            if (previous != current) {
                PacketUtils.sendPacketNearby(getBukkitEntity().getLocation(), 64, new Packet5EntityEquipment(id, i,
                        current));
                previousEquipment[i] = current;
            }
        }
    }

    public void moveTick() {
        if (this.dead) {
            if (this.targetEntity != null || this.path != null)
                cancelTarget();
            return;
        }
        updateEquipment();
        hasAttacked = false;
        if (randomPather) {
            takeRandomPath();
        }
        updateTarget();
        if (this.path != null || this.targetEntity != null) {
            updatePathingState();
        }
        if (this.path != null) {
            Vec3D vector = getPathVector();
            if (vector != null) {
                handleMove(vector);
            }
        }
        if (this.attackTicks > 0)
            --this.attackTicks;
        if (this.noDamageTicks > 0)
            --this.noDamageTicks; // Update entity
    }

    public boolean pathFinished() {
        return path == null;
    }

    public void performAction(Animation action) {
        this.animations.performAnimation(action);
    }

    public void setAutoPathfinder(boolean toggle) {
        if (toggle) {
            autoPathfinder = MinecraftAutoPathfinder.INSTANCE;
        } else
            autoPathfinder = null;
    }

    public void setTarget(LivingEntity entity, boolean aggro, int maxTicks, int maxStationaryTicks, double range) {
        if (Plugins.worldGuardEnabled() && Settings.getBoolean("DenyBlockedPVPTargets") && entity instanceof Player) {
            if (!Plugins.worldGuard.getGlobalRegionManager().allows(DefaultFlag.PVP, entity.getLocation()))
                return;
        }
        this.targetEntity = ((CraftLivingEntity) entity).getHandle();
        this.targetAggro = aggro;
        this.pathTickLimit = maxTicks;
        this.pathingRange = (float) range;
        this.stationaryTickLimit = maxStationaryTicks;
    }

    public boolean startPath(Location loc, int maxTicks, int maxStationaryTicks, double range) {
        this.pathTickLimit = maxTicks;
        this.stationaryTickLimit = maxStationaryTicks;
        this.pathingRange = (float) range;

        if (loc != null) {
            this.path = createPathEntity(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            this.dest = loc.clone();
        }
        return pathFinished();
    }

    private void takeRandomPath() {
        if (!hasAttacked && this.targetEntity != null && (this.path == null || this.random.nextInt(20) == 0)) {
            this.path = this.world.findPath(this, this.targetEntity, pathingRange, true, false, false, true);
            this.dest = this.targetEntity.getBukkitEntity().getLocation();
        } else if (!hasAttacked && this.path == null)
            autoPathfinder.find(this);
    }

    public void targetClosestPlayer(boolean aggro, double range) {
        this.targetEntity = this.getClosestPlayer(range);
        this.targetAggro = aggro;
    }

    public boolean toggleAutoPathfinder() {
        if (autoPathfinder == null) {
            autoPathfinder = MinecraftAutoPathfinder.INSTANCE;
            return true;
        }
        autoPathfinder = null;
        return false;
    }

    private void updatePathingState() {
        Location loc = this.bukkitEntity.getLocation();
        if (prevX == loc.getBlockX() && prevY == loc.getBlockY() && prevZ == loc.getBlockZ()) {
            ++stationaryTicks;
        } else {
            stationaryTicks = 0;
        }
        ++pathTicks;
        if ((pathTickLimit != -1 && pathTicks >= pathTickLimit)
                || (stationaryTickLimit != -1 && stationaryTicks >= stationaryTickLimit)) {
            if (dest != null && !(this instanceof CreatureNPC)) {
                this.getBukkitEntity().teleport(dest);
            }
            cancelPath();
        }
        prevX = loc.getBlockX();
        prevY = loc.getBlockY();
        prevZ = loc.getBlockZ();
    }

    private void updateTarget() {
        if (!this.hasAttacked && this.targetEntity != null && autoPathToTarget) {
            this.path = this.world.findPath(this, this.targetEntity, pathingRange, true, false, false, true);
            this.dest = this.targetEntity.getBukkitEntity().getLocation();
        }
        if (targetEntity == null)
            return;
        if (this.targetEntity.dead || !targetEntity.world.equals(this.world)) {
            cancelTarget();
            return;
        }
        NPCManager.faceEntity(this.npc, targetEntity.getBukkitEntity());
        if (!targetAggro)
            return;
        if (isWithinAttackRange(this.targetEntity, distance(this.targetEntity))) {
            this.attackEntity(this.targetEntity);
        }
    }

    private static final double JUMP_FACTOR = 0.07D;
}