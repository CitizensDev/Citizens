package net.citizensnpcs.questers.quests;

import java.util.ArrayList;
import java.util.List;

public class QuestStep {
	private final List<Objective> objectives = new ArrayList<Objective>();

	public List<Objective> objectives() {
		return objectives;
	}

	public void add(Objective obj) {
		objectives.add(obj);
	}
}