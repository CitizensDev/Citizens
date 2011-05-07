package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

/**
 * Quest type involving the destruction of blocks
 */
public class DestroyQuest {
	private HumanNPC questGiver;
	private Block blockTypeToDestroy;
	private Block[] blocksDestroyed;
	private ItemStack tool;

	public DestroyQuest(HumanNPC questGiver, Block[] blocksDestroyed,
			ItemStack tool) {
		this.questGiver = questGiver;
		this.blocksDestroyed = blocksDestroyed;
		this.tool = tool;
	}

	/**
	 * 
	 * @return the blocks destroyed by a player during a DestroyQuest
	 */
	public Block[] getBlocksDestroyed() {
		return blocksDestroyed;
	}

	/**
	 * 
	 * @return the type of block that a player needs to destroy during a
	 *         DestroyQuest
	 */
	public Block getBlockTypeToDestroy() {
		return blockTypeToDestroy;
	}

	/**
	 * 
	 * @param blockTypeToDestroy
	 *            type of block that a player must destroy during a DestroyQuest
	 */
	public void setBlockToDestroy(Block blockTypeToDestroy) {
		this.blockTypeToDestroy = blockTypeToDestroy;
	}

	/**
	 * 
	 * @return the tool that a player uses to destroy a block during a
	 *         DestroyQuest
	 */
	public ItemStack getTool() {
		return tool;
	}

	/**
	 * 
	 * @param tool
	 *            the tool that a player uses to destroy a block during a
	 *            DestroyQuest
	 */
	public void setTool(ItemStack tool) {
		this.tool = tool;
	}
}