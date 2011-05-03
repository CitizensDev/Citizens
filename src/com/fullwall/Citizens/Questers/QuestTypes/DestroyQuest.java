package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class DestroyQuest implements Quest {
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

	public Block[] getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public Block getBlockTypeToDestroy() {
		return blockTypeToDestroy;
	}

	public void setBlockToDestroy(Block blockTypeToDestroy) {
		this.blockTypeToDestroy = blockTypeToDestroy;
	}

	public ItemStack getTool() {
		return tool;
	}

	public void setTool(ItemStack tool) {
		this.tool = tool;
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
	public Player getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestType getType() {
		return QuestType.DESTROY;
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