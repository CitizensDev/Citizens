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

	public Deque<QuestStep> steps() {
		return steps;
	}

	public ObjectiveCycler newCycler() {
		return new ObjectiveCycler(this);
	}

	public static class ObjectiveCycler {
		// TODO: merge this and QuestStep.
		private final Deque<QuestStep> steps = new ArrayDeque<QuestStep>();
		private int index = 0;

		private ObjectiveCycler(Objectives objectives) {
			this.steps.addAll(objectives.steps);
		}

		public QuestStep current() {
			return this.steps.peek();
		}

		public Objective nextObjective() {
			return current().objectives().get(index++);
		}

		public boolean hasNext() {
			return index + 1 < current().objectives().size();
		}

		public void cycle() {
			index = 0;
			this.steps.pop();
		}

		public int currentStep() {
			return this.steps.size();
		}

		public boolean isCompleted() {
			return this.steps.size() - 1 <= 0;
			// Current quest is kept in deque, so we check if the size minus the
			// loaded step (1) would equal 0 (completed).
		}

		public int index() {
			return this.index;
		}
	}
}