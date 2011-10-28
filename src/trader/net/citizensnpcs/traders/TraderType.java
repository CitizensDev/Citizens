package net.citizensnpcs.traders;

import net.citizensnpcs.commands.CommandHandler;
import net.citizensnpcs.npctypes.CitizensNPC;
import net.citizensnpcs.npctypes.CitizensNPCType;
import net.citizensnpcs.npctypes.NPCTypeManager;
import net.citizensnpcs.properties.Properties;

import org.bukkit.event.Event.Type;

public class TraderType extends CitizensNPCType {
	@Override
	public Properties getProperties() {
		return TraderProperties.INSTANCE;
	}

	@Override
	public CommandHandler getCommands() {
		return TraderCommands.INSTANCE;
	}

	@Override
	public String getName() {
		return "trader";
	}

	@Override
	public void registerEvents() {
		NPCTypeManager.registerEvent(Type.CUSTOM_EVENT, new CitizensListen());
	}

	@Override
	public CitizensNPC getInstance() {
		return new Trader();
	}
}