package net.citizensnpcs.questers;

import net.citizensnpcs.questers.quests.progress.ObjectiveProgress;
import net.citizensnpcs.utils.StringUtils;

public class QuestUtils {
	public static String defaultAmountProgress(ObjectiveProgress progress,
			String descriptor) {
		int amount = progress.getAmount();
		return StringUtils.wrap(amount)
				+ " "
				+ descriptor
				+ ". <br>"
				+ StringUtils
						.wrap(progress.getObjective().getAmount() - amount)
				+ " remaining.";
	}
}
