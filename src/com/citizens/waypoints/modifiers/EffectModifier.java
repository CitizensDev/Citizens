package com.citizens.waypoints.modifiers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.citizens.interfaces.Storage;
import com.citizens.resources.npclib.HumanNPC;
import com.citizens.utils.ConversationUtils.ConversationMessage;
import com.citizens.utils.EffectUtils.Effects;
import com.citizens.utils.EffectUtils.Effects.IEffect;
import com.citizens.waypoints.Waypoint;
import com.citizens.waypoints.WaypointModifier;
import com.citizens.waypoints.WaypointModifierType;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class EffectModifier extends WaypointModifier {
	private final static int DELAY = 0;
	private final List<IEffect> effects = new ArrayList<IEffect>();

	public EffectModifier(Waypoint waypoint) {
		super(waypoint);
	}

	@Override
	public void onReach(HumanNPC npc) {

	}

	@Override
	public void parse(Storage storage, String root) {
		for (String effect : Splitter.on(",").split(
				storage.getString(root + ".effects"))) {
			effects.add(Effects.getByIdentifier(Integer.parseInt(effect)));
		}
	}

	@Override
	public void save(Storage storage, String root) {
		storage.setString(root + ".effects",
				Joiner.on(",").join(effects.toArray()));
	}

	@Override
	public WaypointModifierType getType() {
		return WaypointModifierType.EFFECT;
	}

	@Override
	public void begin(Player player) {

	}

	@Override
	public boolean converse(ConversationMessage message) {
		return false;
	}

	@Override
	public boolean allowExit() {
		return false;
	}

	@Override
	protected void onExit() {
		waypoint.addModifier(this);
	}
}
