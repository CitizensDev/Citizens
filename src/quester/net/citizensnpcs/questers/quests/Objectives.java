package net.citizensnpcs.questers.quests;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

public class Objectives {
	private final Deque<QuestStep> steps = new ArrayDeque<QuestStep>();

	public Objectives(QuestStep... objectives) {
		Collections.addAll(this.steps, objectives);
	}

	public void add(QuestStep step) {
		this.steps.add(step);
	}

	public ObjectiveCycler newCycler() {
		return new ObjectiveCycler(this);
	}

	public Deque<QuestStep> steps() {
		return steps;
	}

	public static class ObjectiveCycler {
		private int index = 0;
		// TODO: merge this and QuestStep.
		private final Deque<QuestStep> steps = new ArrayDeque<QuestStep>();

		private ObjectiveCycler(Objectives objectives) {
			this.steps.addAll(objectives.steps);
		}

		public QuestStep current() {
			return this.steps.peek();
		}

		public int currentStep() {
			return this.steps.size();
		}

		public void cycle() {
			index = 0;
			this.steps.pop();
		}

		public boolean hasNext() {
			return index < current().objectives().size();
		}

		public int index() {
			return this.index;
		}

		public boolean isCompleted() {
			return this.steps.size() <= 0;
		}

		public Objective nextObjective() {
			return current().objectives().get(index++);
		}
	}
}