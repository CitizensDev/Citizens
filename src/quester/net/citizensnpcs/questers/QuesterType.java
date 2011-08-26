package net.citizensnpcs.questers;

import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CitizensNPCType;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.api.Properties;
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
		CitizensManager.registerEvent(Type.CUSTOM_EVENT,
				new QuesterCitizensListen());
		QuesterEntityListen el = new QuesterEntityListen();
		CitizensManager.registerEvent(Type.ENTITY_DAMAGE, el);
		// block events
		QuesterBlockListen bl = new QuesterBlockListen();
		CitizensManager.registerEvent(Type.BLOCK_BREAK, bl);
		CitizensManager.registerEvent(Type.BLOCK_PLACE, bl);
		// player events
		QuesterPlayerListen pl = new QuesterPlayerListen();
		CitizensManager.registerEvent(Type.PLAYER_QUIT, pl);
		CitizensManager.registerEvent(Type.PLAYER_MOVE, pl);
		CitizensManager.registerEvent(Type.PLAYER_PICKUP_ITEM, pl);
		CitizensManager.registerEvent(Type.PLAYER_CHAT, pl);
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
