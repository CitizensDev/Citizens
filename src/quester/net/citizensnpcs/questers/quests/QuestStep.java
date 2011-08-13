package net.citizensnpcs.questers.quests;

import java.util.ArrayList;
import java.util.List;

public class QuestStep {
	private final List<Objective> objectives = new ArrayList<Objective>();

	public List<Objective> all() {
		return objectives;
	}

	public void add(Objective obj) {
		objectives.add(obj);
	}

	public Objective get(int index) {
		return objectives.get(index);
	}
}