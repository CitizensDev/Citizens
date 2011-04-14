package com.fullwall.Citizens.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent.Reason;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Traders.TraderInterface;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent;
import com.fullwall.resources.redecouverte.NPClib.NPCEntityTargetEvent.NpcTargetReason;

/**
 * Listener
 * 
 * @author fullwall
 */
public class EntityListen extends EntityListener {
	private final Citizens plugin;

	public EntityListen(final Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		// Prevent NPCs from getting damaged.
		if (!(event instanceof EntityDamageByEntityEvent))
			return;
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player
				&& NPCManager.getNPC(e.getEntity()) != null) {
			e.setCancelled(true);
		}
	}

	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		if (!(event instanceof NPCEntityTargetEvent))
			return;
		NPCEntityTargetEvent e = (NPCEntityTargetEvent) event;
		HumanNPC npc = NPCManager.getNPC(e.getEntity());

		if (npc != null && event.getTarget() instanceof Player) {
			// The NPC lib handily provides a right click event.
			if (e.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
				Player p = (Player) event.getTarget();
				// Dispatch text event.
				CitizensBasicNPCEvent ev = new CitizensBasicNPCEvent(
						npc.getName(), MessageUtils.getText(npc, e.getTarget(),
								plugin), npc, Reason.RIGHT_CLICK,
						(Player) e.getTarget());
				plugin.getServer().getPluginManager().callEvent(ev);
				if (npc.isTrader()) {
					TraderInterface.handleRightClick(npc, p);
				}
				// If we're using a selection tool, select the NPC as well.
				// Check if we haven't already selected the NPC too.
				if (plugin.canSelect(p.getItemInHand().getTypeId()) == true
						&& !NPCManager.validateSelected(p, npc)) {
					NPCManager.NPCSelected.put(p.getName(), npc.getUID());
					p.sendMessage(ChatColor.GREEN
							+ "You selected NPC ["
							+ StringUtils.yellowify(npc.getStrippedName(),
									ChatColor.GREEN)
							+ "], ID ["
							+ StringUtils.yellowify("" + npc.getUID(),
									ChatColor.GREEN) + "]");
				}
			}
		}
	}
}
