package com.fullwall.Citizens.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCManager;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent;
import com.fullwall.Citizens.Events.CitizensBasicNPCEvent.Reason;
import com.fullwall.Citizens.Utils.MessageUtils;
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
			if (e.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
				Player p = (Player)event.getTarget();
				NPCManager.NPCSelected.put(p.getName(), npc.getUID());
				Citizens.log.info(p.getName()+" selected NPC " + npc.getName() + " : " + npc.getUID());
				CitizensBasicNPCEvent ev = new CitizensBasicNPCEvent(
						npc.getName(), MessageUtils.getText(npc, e.getTarget(),
								plugin), npc, Reason.RIGHT_CLICK,
						(Player) e.getTarget());
				plugin.getServer().getPluginManager().callEvent(ev);
			} else if (e.getNpcReason() == NpcTargetReason.CLOSEST_PLAYER) {
			}
		}
	}

	@SuppressWarnings("unused")
	private float pointToPoint(double xa, double za, double xb, double zb) {
		double deg = 0;
		double x = xa - xb;
		double y = za - zb;
		if (x > 0) {
			if (y > 0) {
				deg = Math.tan(x / y);
			} else {
				deg = 90 + Math.tan(y / x);
			}
		} else {
			if (y > 0) {
				deg = 270 + Math.tan(y / x);
			} else {
				deg = 180 + Math.tan(x / y);
			}
		}
		return (float) deg;
	}
}
