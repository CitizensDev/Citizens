// $Id$
/*
 * Copyright (C) 2010 sk89q <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.fullwall.resources.sk89q.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation indicates a command. Methods should be marked with this
 * annotation to tell {@link CommandsManager} that the method is a command. Note
 * that the method name can actually be anything.
 * 
 * @author sk89q
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
	/**
	 * A list of aliases for the command. The first alias is the most important
	 * -- it is the main name of the command. (The method name is never used for
	 * anything).
	 */
	String[] aliases();

	/**
	 * Usage instruction. Example text for usage could be
	 * <code>[-h] [name] [message]</code>.
	 */
	String usage() default "";

	/**
	 * A short description for the command.
	 */
	String desc();

	/**
	 * A modifier field that is used for determining between two commands of the
	 * same command label. The modifier determines which command to use based on
	 * the first command argument.
	 */
	String[] modifiers();

	/**
	 * The minimum number of arguments. This should be 0 or above.
	 */
	int min() default 0;

	/**
	 * The maximum number of arguments. Use -1 for an unlimited number of
	 * arguments.
	 */
	int max() default -1;

	/**
	 * Flags allow special processing for flags such as -h in the command,
	 * allowing users to easily turn on a flag. This is a string with each
	 * character being a flag. Use A-Z and a-z as possible flags.
	 */
	String flags() default "";
}
