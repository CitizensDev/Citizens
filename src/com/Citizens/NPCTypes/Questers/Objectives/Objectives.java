package com.citizens.NPCTypes.Questers.Objectives;

import java.util.ArrayDeque;
import java.util.Collections;

import com.citizens.NPCTypes.Questers.Objectives.Objective;

public class Objectives {
	private final ArrayDeque<Objective> objectives = new ArrayDeque<Objective>();

	public Objectives(Objective... objectives) {
		Collections.addAll(this.objectives, objectives);
	}

	public void add(Objective objective) {
		this.objectives.add(objective);
	}

	public ArrayDeque<Objective> all() {
		return objectives;
	}

	public ObjectiveCycler newCycler() {
		return new ObjectiveCycler(this);
	}

	public static class ObjectiveCycler {
		private ArrayDeque<Objective> objectives = new ArrayDeque<Objective>();

		public ObjectiveCycler(Objectives objectives) {
			this.objectives = objectives.all();
		}

		public Objective current() {
			if (this.objectives.size() > 0)
				return this.objectives.peek();
			return null;
		}

		public void cycle() {
			this.objectives.pop();
		}

		public int currentStep() {
			return this.objectives.size();
		}

		public boolean isCompleted() {
			return this.objectives.size() == 0;
		}
	}
}