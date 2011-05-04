package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Quest type involving the placement of blocks
 */
public class BuildQuest implements Quest {
	private HumanNPC questGiver;
	private Block blockTypeToPlace;
	private Block[] blocksPlaced;

	public BuildQuest(HumanNPC questGiver, Block[] blocksPlaced) {
		this.questGiver = questGiver;
		this.blocksPlaced = blocksPlaced;
	}

	/**
	 * 
	 * @return the blocks placed by a player during a BuildQuest
	 */
	public Block[] getBlocksPlaced() {
		return blocksPlaced;
	}

	/**
	 * 
	 * @return the type of block that a player needs to place during a
	 *         BuildQuest
	 */
	public Block getBlockTypeToPlace() {
		return blockTypeToPlace;
	}

	/**
	 * 
	 * @param blockTypeToPlace
	 *            type of block that a player must place during a BuildQuest
	 */
	public void setBlockToPlace(Block blockTypeToPlace) {
		this.blockTypeToPlace = blockTypeToPlace;
	}

	/**
	 * Interface methods
	 */
	@Override
	public HumanNPC getQuestGiver() {
		return questGiver;
	}

	@Override
	public void setQuestGiver(HumanNPC questGiver) {
		this.questGiver = questGiver;
	}

	@Override
	public Player getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestType getType() {
		return QuestType.BUILD;
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
}