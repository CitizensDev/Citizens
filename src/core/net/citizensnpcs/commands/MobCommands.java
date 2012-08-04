package net.citizensnpcs.commands;

import net.citizensnpcs.permissions.PermissionManager;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.sk89q.Command;
import net.citizensnpcs.resources.sk89q.CommandContext;
import net.citizensnpcs.resources.sk89q.CommandPermissions;
import net.citizensnpcs.resources.sk89q.CommandRequirements;

import org.bukkit.entity.Player;

public class MobCommands extends CommandHandler {
	@Override
	public void addPermissions() {
		PermissionManager.addPermission("creature.spawn");
	}

	@CommandRequirements()
	@Command(
			aliases = "npc",
			usage = "spawn [type]",
			desc = "spawn a creature npc",
			min = 2,
			max = 2)
	@CommandPermissions("creature.spawn")
	public static void spawn(CommandContext args, Player player, HumanNPC npc) {
	}
}