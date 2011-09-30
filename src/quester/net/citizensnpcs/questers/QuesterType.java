package net.citizensnpcs.questers;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.questers.data.QuestProperties;
import net.citizensnpcs.questers.data.QuesterProperties;
import net.citizensnpcs.questers.listeners.QuesterBlockListen;
import net.citizensnpcs.questers.listeners.QuesterCitizensListen;
import net.citizensnpcs.questers.listeners.QuesterEntityListen;
import net.citizensnpcs.questers.listeners.QuesterPlayerListen;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

public class QuesterType extends CitizensNPCType {

	@Override
	public Properties getProperties() {
		return QuesterProperties.INSTANCE;
	}

	@Override
	public CommandHandler getCommands() {
		return QuesterCommands.INSTANCE;
	}

	@Override
	public void registerEvents() {
		QuestProperties.initialize();
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
	public CitizensNPC getInstance() {
		return new Quester();
	}
}