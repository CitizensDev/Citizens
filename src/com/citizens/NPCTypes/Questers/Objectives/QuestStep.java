package com.citizens.npctypes.questers.objectives;

import java.util.ArrayList;
import java.util.List;

public class QuestStep {
	private final List<Objective> objectives = new ArrayList<Objective>();
	private int index = 0;

	public List<Objective> all() {
		return objectives;
	}

	public void add(Objective obj) {
		objectives.add(obj);
	}

	public Objective current() {
		return objectives.get(index++);
	}
}