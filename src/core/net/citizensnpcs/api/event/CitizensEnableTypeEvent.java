package net.citizensnpcs.api.event;

import net.citizensnpcs.npctypes.CitizensNPCType;

public class CitizensEnableTypeEvent extends CitizensEvent {
	private static final long serialVersionUID = 1L;
	private final CitizensNPCType type;

	public CitizensEnableTypeEvent(CitizensNPCType type) {
		super("CitizensEnableTypeEvent");
		this.type = type;
	}

	public CitizensNPCType getEnabledType() {
		return this.type;
	}
}
