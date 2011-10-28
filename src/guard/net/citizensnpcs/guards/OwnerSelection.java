package net.citizensnpcs.guards;

import java.util.Iterator;
import java.util.Set;

import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;

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
	public void select(HumanNPC selection) {
		if (isOwner(selection.getUID()))
			selected.add(selection.getUID());
	}

	@Override
	public void deselect(HumanNPC selection) {
		if (isOwner(selection.getUID()))
			selected.remove(selection.getUID());
	}

	@Override
	public boolean isSelected(HumanNPC selection) {
		return isOwner(selection.getUID())
				&& selected.contains(selection.getUID());
	}

	private boolean isOwner(int UID) {
		return NPCManager.isOwner(owner, UID);
	}

	public int size() {
		return selected.size();
	}

	@Override
	public Iterator<HumanNPC> iterator() {
		return Iterators.transform(selected.iterator(), UIDToNPC);
	}

	private static Function<Integer, HumanNPC> UIDToNPC = new Function<Integer, HumanNPC>() {
		@Override
		public HumanNPC apply(Integer arg0) {
			return NPCManager.get(arg0);
		}
	};

	@Override
	public void deselectAll() {
		selected.clear();
	}
}
