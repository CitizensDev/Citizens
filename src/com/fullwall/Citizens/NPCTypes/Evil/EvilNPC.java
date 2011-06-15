package com.fullwall.Citizens.NPCTypes.Evil;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Constants;
import com.fullwall.Citizens.CreatureTask;
import com.fullwall.Citizens.Interfaces.Clickable;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Properties.PropertyManager;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.Messaging;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class EvilNPC implements Clickable {
	@SuppressWarnings("unused")
	private final HumanNPC npc;
	private boolean isTame = false;

	/**
	 * Evil NPC object
	 * 
	 * @param npc
	 */
	public EvilNPC(HumanNPC npc) {
		this.npc = npc;
	}

	/**
	 * Get whether the evil NPC is tamed
	 * 
	 * @return
	 */
	public boolean isTame() {
		return isTame;
	}

	/**
	 * Set the tamed state of an evil NPC
	 * 
	 * @param isTame
	 */
	public void setTame(boolean isTame) {
		this.isTame = isTame;
	}

	@Override
	public void onLeftClick(Player player, HumanNPC npc) {
	}

	@Override
	public void onRightClick(Player player, HumanNPC npc) {
		if (player.getItemInHand().getTypeId() == Constants.evilNPCTameItem) {
			if (new Random().nextInt(100) <= Constants.evilNPCTameChance) {
				setTame(true);
				npc.setEvil(false);
				CreatureTask.despawn(CreatureTask.getCreature(npc.getHandle()
						.getBukkitEntity()));
				int UID = NPCManager.register(npc.getName(),
						player.getLocation(), player.getName());
				PropertyManager.getBasic().saveNPCAmountPerPlayer(
						player.getName(),
						PropertyManager.getBasic().getNPCAmountPerPlayer(
								player.getName()) + 1);
				NPCManager.get(UID).getNPCData().setOwner(player.getName());
				player.sendMessage(ChatColor.GREEN + "You have tamed "
						+ StringUtils.wrap(npc.getStrippedName())
						+ "! You can now toggle it to be any type.");
			} else {
				Messaging
						.send(player,
								ChatColor.RED
										+ "["
										+ npc.getStrippedName()
										+ "] "
										+ ChatColor.WHITE
										+ MessageUtils
												.getRandomMessage(Constants.failureToTameMessages));
			}
		}
	}
}