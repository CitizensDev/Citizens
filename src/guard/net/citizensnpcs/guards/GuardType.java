package net.citizensnpcs.guards;

import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CitizensNPCType;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.api.Properties;
import net.citizensnpcs.guards.listeners.GuardCitizensListen;
import net.citizensnpcs.guards.listeners.GuardPlayerListen;

import org.bukkit.event.Event.Type;

public class GuardType extends CitizensNPCType {

	@Override
	public Properties getProperties() {
		return GuardProperties.INSTANCE;
	}

	@Override
	public CommandHandler getCommands() {
		return GuardCommands.INSTANCE;
	}

	@Override
	public void registerEvents() {
		CitizensManager.registerEvent(Type.PLAYER_LOGIN,
				new GuardPlayerListen());
		CitizensManager.registerEvent(Type.CUSTOM_EVENT,
				new GuardCitizensListen());
	}

	@Override
	public String getName() {
		return "guard";
	}

	@Override
	public CitizensNPC getInstance() {
		return new Guard();
	}
}
