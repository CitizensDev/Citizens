package net.citizensnpcs.lib.pathfinding;

import java.util.Iterator;
import java.util.List;

import net.citizensnpcs.Plugins;
import net.citizensnpcs.Settings;
import net.citizensnpcs.lib.CraftNPC;
import net.citizensnpcs.lib.pathfinding.PathCallback.PathCancelReason;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

public class PathController {
	private final List<PathCallback> callbacks = Lists.newArrayList();
	private final CraftNPC parent;
	private Path executing;
	private AutoPathfinder autoPathfinder;

	public PathController(CraftNPC parent) {
		this.parent = parent;
	}

	public void cancel() {
		if (executing != null) {
			doCallback(new CancelCallback(PathCancelReason.PLUGIN));
		}
		executing = null;
	}

	private void doCallback(CallbackExecutor<PathCallback> executor) {
		Iterator<PathCallback> itr = callbacks.iterator();
		while (itr.hasNext()) {
			PathCallback callback = itr.next();
			if (executor.call(callback))
				itr.remove();
		}
	}

	public void doTick() {
		if (executing == null) {
			if (autoPathfinder != null)
				autoPathfinder.find(parent);
			return;
		}
		boolean finished = executing.update();
		if (!finished)
			return;
		doCallback(new CallbackExecutor<PathCallback>() {
			@Override
			public boolean call(PathCallback t) {
				return t.onPathCompletion(PathController.this);
			}
		});
		executing = null;
	}

	public Path getCurrentPath() {
		return executing;
	}

	public boolean isPathing() {
		return executing != null;
	}

	public Path newPathWithStrategy(PathingStrategy strategy) {
		return setExecuting(Path.create(strategy));
	}

	public Path pathTo(Location destination) {
		return setExecuting(Path.create(new MinecraftPathingStrategy(parent,
				destination)));
	}

	public void registerCallback(PathCallback callback) {
		callbacks.add(callback);
	}

	public void setAutoPathfinder(AutoPathfinder autoPathfinder) {
		this.autoPathfinder = autoPathfinder;
	}

	private Path setExecuting(Path path) {
		if (executing != null)
			doCallback(new CancelCallback(PathCancelReason.REPLACE));
		executing = path;
		return executing;
	}

	public Path target(Entity target, boolean aggro) {
		if (Plugins.worldGuardEnabled()
				&& Settings.getBoolean("DenyBlockedPVPTargets")
				&& target instanceof Player) {
			if (!Plugins.worldGuard.getGlobalRegionManager().allows(
					DefaultFlag.PVP, target.getLocation()))
				return null;
		}
		return setExecuting(Path.create(new TargetPathingStrategy(parent,
				target, aggro)));
	}

	private static interface CallbackExecutor<T> {
		boolean call(T t);
	}

	private class CancelCallback implements CallbackExecutor<PathCallback> {
		private final PathCancelReason reason;

		private CancelCallback(PathCancelReason reason) {
			this.reason = reason;
		}

		@Override
		public boolean call(PathCallback t) {
			return t.onPathCancel(PathController.this, reason);
		}
	}
}
