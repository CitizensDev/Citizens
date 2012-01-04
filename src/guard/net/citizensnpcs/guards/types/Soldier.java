package net.citizensnpcs.guards.types;

import java.util.Map;

import net.citizensnpcs.guards.Guard;
import net.citizensnpcs.guards.GuardState;
import net.citizensnpcs.guards.GuardUpdater;
import net.citizensnpcs.guards.OwnerSelection;
import net.citizensnpcs.guards.Selection;
import net.citizensnpcs.lib.HumanNPC;
import net.citizensnpcs.lib.NPCManager;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Maps;

public class Soldier implements GuardUpdater {
	public Soldier() {
		PlayerListener listener = new SelectionHooks();
		NPCTypeManager.registerEvent(Type.PLAYER_QUIT, listener);
		NPCTypeManager.registerEvent(Type.PLAYER_LOGIN, listener);
		NPCTypeManager.registerEvent(Type.PLAYER_INTERACT, listener);
		NPCTypeManager.registerEvent(Type.PLAYER_INTERACT_ENTITY, listener);
	}

	@Override
	public void onDamage(HumanNPC npc, LivingEntity attacker) {
	}

	@Override
	public GuardStatus updateStatus(GuardStatus current, HumanNPC npc) {
		return current;
	}

	private static class SelectionHooks extends PlayerListener {
		private void attack(Player player, LivingEntity attack,
				Selection<HumanNPC> selection) {
			if (selection.size() == 0)
				return;
			for (HumanNPC npc : selection) {
				npc.getPathController().target(attack, true);
			}
			player.sendMessage(ChatColor.GREEN + "Set "
					+ StringUtils.wrap(selection.size())
					+ StringUtils.pluralise(" NPC", selection.size())
					+ " on your target.");
		}

		private boolean isSoldier(HumanNPC npc) {
			return npc.isType("guard")
					&& ((Guard) npc.getType("guard"))
							.isState(GuardState.SOLDIER);
		}

		@Override
		public void onPlayerInteract(PlayerInteractEvent event) {
			Selection<HumanNPC> selection = getSelection(event.getPlayer());
			if (event.getAction() != Action.RIGHT_CLICK_BLOCK
					|| selection.size() == 0
					|| !UtilityProperties.isHoldingTool("SoldierSelectTool",
							event.getPlayer())) {
				if ((event.getAction() == Action.RIGHT_CLICK_AIR || event
						.getAction() == Action.RIGHT_CLICK_BLOCK)
						&& selection.size() > 0
						&& UtilityProperties.isHoldingTool(
								"SoldierDeselectAllTool", event.getPlayer())) {
					event.getPlayer().sendMessage(
							ChatColor.GRAY
									+ "Deselecting "
									+ StringUtils.wrap(selection.size(),
											ChatColor.GRAY)
									+ StringUtils.pluralise(" NPC",
											selection.size()) + ".");
					selection.deselectAll();
				}
				return;
			}
			for (HumanNPC npc : getSelection(event.getPlayer())) {
				npc.getPathController().pathTo(
						event.getClickedBlock().getLocation());
			}
			event.getPlayer().sendMessage(
					StringUtils.wrap(selection.size()) + " "
							+ StringUtils.pluralise("NPC", selections.size())
							+ " enroute.");
		}

		@Override
		public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
			HumanNPC npc = NPCManager.get(event.getRightClicked());
			boolean holding = UtilityProperties.isHoldingTool(
					"SoldierSelectTool", event.getPlayer());
			if (npc == null) {
				if (holding && event.getRightClicked() instanceof LivingEntity) {
					attack(event.getPlayer(),
							(LivingEntity) event.getRightClicked(),
							getSelection(event.getPlayer()));
				}
				return;
			}
			if (isSoldier(npc)) {
				if (holding) {
					select(event.getPlayer(), npc,
							getSelection(event.getPlayer()));
				} else if (UtilityProperties.isHoldingTool("SoldierReturnTool",
						event.getPlayer())) {
					npc.getPathController().cancel();
					npc.teleport(npc.getBaseLocation());
				}
				return;
			}
		}

		@Override
		public void onPlayerQuit(PlayerQuitEvent event) {
			selections.remove(event.getPlayer());
		}

		private void select(Player player, HumanNPC npc,
				Selection<HumanNPC> selection) {
			if (selection.isSelected(npc)) {
				selection.deselect(npc);
			} else {
				selection.select(npc);
				npc.getPathController().target(player, false);
			}
			player.sendMessage(ChatColor.GREEN
					+ (selection.isSelected(npc) ? "Selected" : "Deselected")
					+ StringUtils.wrap(npc.getName()) + ". "
					+ StringUtils.wrap(selection.size())
					+ StringUtils.pluralise(" NPC", selection.size())
					+ " now selected.");

		}
	}

	private static final Map<Player, Selection<HumanNPC>> selections = Maps
			.newHashMap();

	private static Selection<HumanNPC> getSelection(Player player) {
		if (!selections.containsKey(player))
			selections.put(player, new OwnerSelection(player));
		return selections.get(player);
	}
}
