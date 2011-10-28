package net.citizensnpcs.guards;

import java.util.Map;

import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.guards.types.GuardStatus;
import net.citizensnpcs.resources.npclib.HumanNPC;

import com.google.common.collect.Maps;

public class GuardTask implements Runnable {
	private final static Map<HumanNPC, GuardStatus> states = Maps.newHashMap();

	@Override
	public void run() {
		for (HumanNPC npc : CitizensManager.getList().values()) {
			if (!npc.isType("guard")) {
				continue;
			}
			Guard guard = npc.getType("guard");
			if (!states.containsKey(npc))
				states.put(npc, GuardStatus.NORMAL);
			states.put(npc, guard.updateStatus(states.get(npc), npc));
		}
	}
}