package net.citizensnpcs.questers.quests;

import java.util.ArrayDeque;
import java.util.Collections;

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
		private ArrayDeque<QuestStep> objectives = new ArrayDeque<QuestStep>();

		private ObjectiveCycler(Objectives objectives) {
			this.objectives = objectives.all();
		}

		public QuestStep current() {
			return this.objectives.peek();
		}

		public void cycle() {
			this.objectives.pop();
		}

		public int currentStep() {
			return this.objectives.size();
		}

		public boolean isCompleted() {
			return this.objectives.size() - 1 == 0;
			// Current quest is kept in deque, so we check if the size minus the
			// loaded step (1) would equal 0 (completed).
		}
	}
}