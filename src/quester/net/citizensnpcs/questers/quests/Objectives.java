package net.citizensnpcs.questers.quests;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

public class Objectives {
	private final ArrayDeque<QuestStep> objectives = new ArrayDeque<QuestStep>();

	public Objectives(QuestStep... objectives) {
		Collections.addAll(this.objectives, objectives);
	}

	public void add(QuestStep step) {
		this.objectives.add(step);
	}

	public ArrayDeque<QuestStep> all() {
		return objectives;
	}

	public ObjectiveCycler newCycler() {
		return new ObjectiveCycler(this);
	}

	public static class ObjectiveCycler {
		private Deque<QuestStep> objectives = new ArrayDeque<QuestStep>();
		private int index = 0;

		private ObjectiveCycler(Objectives objectives) {
			this.objectives = objectives.all();
		}

		public QuestStep current() {
			return this.objectives.peek();
		}

		public Objective nextObjective() {
			return current().all().get(index++);
		}

		public void cycle() {
			index = 0;
			this.objectives.pop();
		}

		public int currentStep() {
			return this.objectives.size();
		}

		public boolean isCompleted() {
			return this.objectives.size() - 1 <= 0;
			// Current quest is kept in deque, so we check if the size minus the
			// loaded step (1) would equal 0 (completed).
		}
	}
}