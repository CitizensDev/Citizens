package com.citizens.Resources.sk89q;

import com.citizens.Utils.MessageUtils;

public class ServerCommandException extends CommandException {

	private static final long serialVersionUID = 9120268556899197316L;

	public ServerCommandException() {
		super(MessageUtils.mustBeIngameMessage);
	}
}
