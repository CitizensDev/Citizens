package net.citizensnpcs.resources.sk89q;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A marker annotation that marks a method as being usable from the command
 * line.
 * 
 * @author fullwall
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerCommand {

}
