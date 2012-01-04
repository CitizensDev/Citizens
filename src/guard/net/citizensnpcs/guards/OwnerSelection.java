package net.citizensnpcs.guards;

import java.util.Iterator;
import java.util.Set;

import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;

import org.bukkit.entity.Player;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

public class OwnerSelection implements Selection<HumanNPC> {
	private final Set<Integer> selected = Sets.newHashSet();
	private final Player owner;

	public OwnerSelection(Player owner) {
		this.owner = owner;
	}

	@Override
	public void deselect(HumanNPC selection) {
		if (isOwner(selection.getUID()))
			selected.remove(selection.getUID());
	}

	@Override
	public void deselectAll() {
		selected.clear();
	}

	private boolean isOwner(int UID) {
		return NPCManager.isOwner(owner, UID);
	}

	@Override
	public boolean isSelected(HumanNPC selection) {
		return isOwner(selection.getUID())
				&& selected.contains(selection.getUID());
	}

	@Override
	public Iterator<HumanNPC> iterator() {
		return Iterators.transform(selected.iterator(), UIDToNPC);
	}

	@Override
	public void select(HumanNPC selection) {
		if (isOwner(selection.getUID()))
			selected.add(selection.getUID());
	}

	@Override
	public int size() {
		return selected.size();
	}

	private static Function<Integer, HumanNPC> UIDToNPC = new Function<Integer, HumanNPC>() {
		@Override
		public HumanNPC apply(Integer arg0) {
			return NPCManager.get(arg0);
		}
	};
}
