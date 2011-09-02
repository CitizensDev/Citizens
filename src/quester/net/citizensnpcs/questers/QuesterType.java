package net.citizensnpcs.questers;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Properties;
import net.citizensnpcs.questers.listeners.QuesterBlockListen;
import net.citizensnpcs.questers.listeners.QuesterCitizensListen;
import net.citizensnpcs.questers.listeners.QuesterEntityListen;
import net.citizensnpcs.questers.listeners.QuesterPlayerListen;

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
		// custom events
		NPCTypeManager.registerEvent(Type.CUSTOM_EVENT,
				new QuesterCitizensListen());
		QuesterEntityListen el = new QuesterEntityListen();
		NPCTypeManager.registerEvent(Type.ENTITY_DAMAGE, el);
		// block events
		QuesterBlockListen bl = new QuesterBlockListen();
		NPCTypeManager.registerEvent(Type.BLOCK_BREAK, bl);
		NPCTypeManager.registerEvent(Type.BLOCK_PLACE, bl);
		// player events
		QuesterPlayerListen pl = new QuesterPlayerListen();
		NPCTypeManager.registerEvent(Type.PLAYER_QUIT, pl);
		NPCTypeManager.registerEvent(Type.PLAYER_MOVE, pl);
		NPCTypeManager.registerEvent(Type.PLAYER_PICKUP_ITEM, pl);
		NPCTypeManager.registerEvent(Type.PLAYER_CHAT, pl);
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