package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Quest type involving killing players
 */
public class PlayerCombatQuest implements Quest {
	private HumanNPC questGiver;
	private Player killer;
	private Player victim;
	private ItemStack weapon;

	public PlayerCombatQuest(HumanNPC questGiver, Player killer, Player victim,
			ItemStack weapon) {
		this.questGiver = questGiver;
		this.killer = killer;
		this.victim = victim;
		this.weapon = weapon;
		weapon = killer.getItemInHand();
	}

	/**
	 * 
	 * @return the weapon used by a player to kill another player during a
	 *         PlayerCombatQuest
	 */
	public ItemStack getWeapon() {
		return weapon;
	}

	/**
	 * 
	 * @param weapon
	 *            the weapon used by a player to kill another player during a
	 *            PlayerCombatQuest
	 */
	public void setWeapon(ItemStack weapon) {
		killer.setItemInHand(weapon);
	}

	/**
	 * 
	 * @return the player killed during a PlayerCombatQuest
	 */
	public Player getVictim() {
		return victim;
	}

	/**
	 * Interface methods
	 */
	@Override
	public Player getPlayer() {
		return killer;
	}

	@Override
	public QuestType getType() {
		return QuestType.PLAYER_COMBAT;
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
}