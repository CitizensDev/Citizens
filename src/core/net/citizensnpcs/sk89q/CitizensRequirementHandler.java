package net.citizensnpcs.sk89q;

import java.lang.annotation.Annotation;

import net.citizensnpcs.economy.Economy;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.utils.MessageUtils;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CitizensRequirementHandler implements RequirementHandler {

	@Override
	public Class<? extends Annotation> getRequirementAnnotation() {
		return CommandRequirements.class;
	}

	@Override
	public void evaluateRequirements(Annotation annotation, Object... arguments)
			throws RequirementMissingException {
		if (!(arguments[1] instanceof Player))
			return;
		CommandRequirements requirements = (CommandRequirements) annotation;
		Player player = (Player) arguments[1];
		HumanNPC npc = (HumanNPC) arguments[2];

		if (requirements.requireEconomy() && !Economy.hasMethod()) {
			throw new RequirementMissingException(MessageUtils.noEconomyMessage);
		}
		if (requirements.requiredMoney() != -1
				&& !Economy.hasEnough(player, requirements.requiredMoney())) {
			throw new RequirementMissingException("You need at least "
					+ StringUtils.wrap(
							Economy.format(requirements.requiredMoney()),
							ChatColor.RED));
		}
		if (requirements.requireSelected() && npc == null) {
			throw new RequirementMissingException(
					MessageUtils.mustHaveNPCSelectedMessage);
		}
		if (requirements.requireOwnership() && npc != null
				&& !NPCManager.isOwner(player, npc.getUID())) {
			throw new RequirementMissingException(MessageUtils.notOwnerMessage);
		}
		if (npc != null && !requirements.requiredType().isEmpty()) {
			if (!npc.isType(requirements.requiredType())) {
				throw new RequirementMissingException("Your NPC isn't a "
						+ requirements.requiredType() + " yet.");
			}
		}
	}
}
