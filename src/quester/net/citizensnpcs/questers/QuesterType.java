package net.citizensnpcs.questers;

import java.util.List;

import net.citizensnpcs.Settings.SettingsType;
import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Setting;
import net.citizensnpcs.questers.data.QuestProperties;
import net.citizensnpcs.questers.listeners.QuesterBlockListen;
import net.citizensnpcs.questers.listeners.QuesterCitizensListen;
import net.citizensnpcs.questers.listeners.QuesterEntityListen;
import net.citizensnpcs.questers.listeners.QuesterPlayerListen;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import com.google.common.collect.Lists;

public class QuesterType extends CitizensNPCType {
	@Override
	public CommandHandler getCommands() {
		return QuesterCommands.INSTANCE;
	}

	@Override
	public void registerEvents() {
		QuestProperties.load();
		// custom events
		NPCTypeManager.registerEvent(Type.CUSTOM_EVENT,
				new QuesterCitizensListen());
		QuesterEntityListen el = new QuesterEntityListen();
		NPCTypeManager.registerEvent(Type.ENTITY_DEATH, el, Priority.Highest);
		// block events
		QuesterBlockListen bl = new QuesterBlockListen();
		NPCTypeManager.registerEvent(Type.BLOCK_BREAK, bl, Priority.Highest);
		NPCTypeManager.registerEvent(Type.BLOCK_PLACE, bl, Priority.Highest);
		// player events
		QuesterPlayerListen pl = new QuesterPlayerListen();
		NPCTypeManager.registerEvent(Type.PLAYER_QUIT, pl, Priority.Highest);
		NPCTypeManager.registerEvent(Type.PLAYER_MOVE, pl, Priority.Highest);
		NPCTypeManager.registerEvent(Type.PLAYER_PICKUP_ITEM, pl,
				Priority.Highest);
		NPCTypeManager.registerEvent(Type.PLAYER_DROP_ITEM, pl,
				Priority.Highest);
		NPCTypeManager.registerEvent(Type.PLAYER_CHAT, pl, Priority.Highest);
	}

	@Override
	public String getName() {
		return "quester";
	}

	@Override
	public CitizensNPC newInstance() {
		return new Quester();
	}

	@Override
	public List<Setting> getSettings() {
		List<Setting> nodes = Lists.newArrayList();
		nodes.add(new Setting("QuestSaveDelay", SettingsType.GENERAL,
				"quests.save.command-delay-ms", 5000));
		nodes.add(new Setting("ItemExploitCheckDelay", SettingsType.GENERAL,
				"quests.exploits.item-pickup.check-delay", 400));
		nodes.add(new Setting("CombatExploitTimes", SettingsType.GENERAL,
				"quests.exploits.combat.check-times", 2));
		nodes.add(new Setting("CombatExploitRadius", SettingsType.GENERAL,
				"quests.exploits.combat.check-radius", 20));
		nodes.add(new Setting("BlockTrackingRemoveDelay", SettingsType.GENERAL,
				"quests.exploits.blocks.tracking-remove-delay", 6000));
		return nodes;
	}
}