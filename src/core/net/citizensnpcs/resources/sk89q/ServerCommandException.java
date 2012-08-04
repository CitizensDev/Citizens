package net.citizensnpcs.resources.sk89q;

import net.citizensnpcs.utils.MessageUtils;

public class ServerCommandException extends CommandException {

	public ServerCommandException() {
		super(MessageUtils.mustBeIngameMessage);
	}

	private static final long serialVersionUID = 9120268556899197316L;
}
