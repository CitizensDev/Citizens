package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Questers.Goal;
import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.Reward;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class PlayerCombatQuest implements Quest {
	private HumanNPC questGiver;
	private Player killer;
	private Player victim;
	private ItemStack weapon;
	private Goal goal;
	private Reward reward;

	public PlayerCombatQuest(HumanNPC questGiver, Player killer, Player victim,
			ItemStack weapon, Goal goal, Reward reward) {
		this.questGiver = questGiver;
		this.killer = killer;
		this.victim = victim;
		this.weapon = weapon;
		this.goal = goal;
		this.reward = reward;
	}

	public Player getVictim() {
		return victim;
	}

	public ItemStack getWeapon() {
		return weapon;
	}

	public void setWeapon(ItemStack weapon) {
		this.weapon = weapon;
	}

	@Override
	public Player getPlayer() {
		return killer;
	}

	@Override
	public QuestType getType() {
		return QuestType.PLAYER_COMBAT;
	}

	@Override
	public Goal getGoal() {
		return goal;
	}

	@Override
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	@Override
	public String getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HumanNPC getQuestGiver() {
		return questGiver;
	}

	@Override
	public void setQuestGiver(HumanNPC questGiver) {
		this.questGiver = questGiver;
	}

	@Override
	public Reward getReward() {
		return reward;
	}

	@Override
	public void setReward(Reward reward) {
		this.reward = reward;
	}

	@Override
	public void rewardPlayer(Player player, Reward reward) {
		// TODO Auto-generated method stub
		
	}
}