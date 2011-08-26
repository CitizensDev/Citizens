package net.citizensnpcs.healers;

import net.citizensnpcs.api.CitizensManager;
import net.citizensnpcs.api.CitizensNPC;
import net.citizensnpcs.api.CitizensNPCType;
import net.citizensnpcs.api.CommandHandler;
import net.citizensnpcs.api.Properties;
import net.citizensnpcs.healers.listeners.HealerCitizensListen;

import org.bukkit.event.Event.Type;

public class HealerType extends CitizensNPCType {

	@Override
	public Properties getProperties() {
		return HealerProperties.INSTANCE;
	}

	@Override
	public CommandHandler getCommands() {
		return HealerCommands.INSTANCE;
	}

	@Override
	public void registerEvents() {
		CitizensManager.registerEvent(Type.CUSTOM_EVENT,
				new HealerCitizensListen());
	}

	@Override
	public String getName() {
		return "healer";
	}

	@Override
	public CitizensNPC getInstance() {
		return new Healer();
	}
}