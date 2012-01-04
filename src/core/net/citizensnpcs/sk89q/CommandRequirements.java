package net.citizensnpcs.sk89q;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Specifies a series of requirements specific to a command. This annotation can
 * be used on both classes and methods, but when used on methods it will
 * override the class definition.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandRequirements {
	double requiredMoney() default -1;

	String requiredType() default "";

	boolean requireEconomy() default false;

	boolean requireOwnership() default false;

	boolean requireSelected() default false;
}