package com.fullwall.Citizens;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.PropertyPool;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class TickTask implements Runnable {

	private Citizens plugin;
	private double range;
	private HashMap<Integer, HashMap<String, Boolean>> hasSaidText = new HashMap<Integer, HashMap<String, Boolean>>();

	public TickTask(Citizens plugin, double range) {
		this.plugin = plugin;
		this.range = range / 2;
	}

	@Override
	public void run() {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			// TODO: MUST BE CHANGED TO USE ONLY BASIC UIDs FOR TALKING -
			// OTHERWISE IT'LL USE GUARDS ETC.
			for (Entry<Integer, HumanNPC> entry : NPCManager.getNPCList().entrySet()) {
				{
					if (checkLocation(entry.getValue().getBukkitEntity().getLocation(), p.getLocation())) {
						if(PropertyPool.getNPCLookWhenClose(entry.getValue().getUID()) == true){
							NPCManager.rotateNPCToPlayer(entry.getValue(), p);
						}
						
						if (PropertyPool.getNPCTalkWhenClose(entry.getValue().getUID())
								&& (!hasSaidText.containsKey(entry.getKey()) || (!hasSaidText
										.get(entry.getKey()).containsKey(
												p.getName()) || hasSaidText
										.get(entry.getKey()).get(p.getName()) == false))) {
							MessageUtils.sendText(entry.getValue(), p, plugin);
							HashMap<String, Boolean> players = new HashMap<String, Boolean>();
							if (hasSaidText.containsKey(entry.getKey()))
								players = hasSaidText.get(entry.getKey());
							players.put(p.getName(), true);
							hasSaidText.put(entry.getKey(), players);
						}
					} else if (PropertyPool.getNPCTalkWhenClose(entry.getValue().getUID())
							&& hasSaidText.containsKey(entry.getKey())
							&& hasSaidText.get(entry.getKey()).containsKey(
									p.getName())
							&& hasSaidText.get(entry.getKey()).get(p.getName()) == true) {
						hasSaidText.get(entry.getKey()).put(p.getName(), false);
					}
				}
			}
		}
		// Obviously outside the player for loop, otherwise gravity would be applied more then once per tick.
		for (Entry<Integer, HumanNPC> entry : NPCManager.getNPCList().entrySet()) {
			HumanNPC NPC = entry.getValue();
			NPC.applyGravity();
		}
	}

	private boolean checkLocation(Location loc, Location ploc) {
		if ((ploc.getX() <= loc.getX() + range && ploc.getX() >= loc.getX()
				- range)
				&& (ploc.getY() >= loc.getY() - range && ploc.getY() <= loc
						.getY() + range)
				&& (ploc.getZ() >= loc.getZ() - range && ploc.getZ() <= loc
						.getZ() + range))
			return true;
		else
			return false;
	}
}
