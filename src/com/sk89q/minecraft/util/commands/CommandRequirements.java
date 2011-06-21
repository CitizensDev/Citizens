package com.sk89q.minecraft.util.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandRequirements {
	boolean requireSelected() default false;

	boolean requireOwnership() default false;

	String requiredType() default "";
}
