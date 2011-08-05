package net.citizensnpcs.misc;

import java.lang.reflect.Method;

import net.citizensnpcs.utils.Messaging;

/*
 Copyright (c) 2005, Corey Goldberg

 StopWatch.java is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 */

public class StopWatch {

	private long startTime = 0;
	private long stopTime = 0;
	private boolean running = false;

	public void start() {
		this.startTime = System.nanoTime();
		this.running = true;
	}

	public void timeFunction(Class<?> call, String methodName, Object instance,
			Object... args) {
		try {
			Method[] methods = call.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					method.setAccessible(true);
					start();
					method.invoke(instance, args);
					stop();
					printElapsed(methodName);
				}
			}
		} catch (Exception ex) {
			Messaging.log("Error while calling method " + methodName
					+ ". Error " + ex.getMessage() + ".");
		}
	}

	public void stop() {
		this.stopTime = System.nanoTime();
		this.running = false;
	}

	public void print() {
		Messaging.log("Start time: " + convertHours(startTime) + "h. Elapsed: "
				+ toMilliseconds(getElapsedTime()) + "ms. Stopped: "
				+ convertHours(stopTime) + "h.");
	}

	public void printElapsed() {
		Messaging.log("Elapsed: " + getElapsedTime() + "ns / "
				+ toMilliseconds(getElapsedTime()) + "ms ("
				+ toSeconds(getElapsedTime()) + "s).");
	}

	public void printElapsed(String method) {
		Messaging.log("Elapsed: " + getElapsedTime() + "ns / "
				+ toMilliseconds(getElapsedTime()) + "ms ("
				+ toSeconds(getElapsedTime()) + "s) while calling " + method
				+ ".");
	}

	public long toMilliseconds(long nanoseconds) {
		return nanoseconds / 1000000;
	}

	public long toSeconds(long nanoseconds) {
		return nanoseconds / 1000000000;
	}

	public long toMinutes(long seconds) {
		return seconds / 60;
	}

	public long toHours(long minutes) {
		return minutes / 60;
	}

	public long convertHours(long nanoseconds) {
		return toHours(toMinutes(toSeconds(nanoseconds)));
	}

	// elapsed time in nanoseconds
	public long getElapsedTime() {
		long elapsed;
		if (running) {
			elapsed = (System.nanoTime() - startTime);
		} else {
			elapsed = (stopTime - startTime);
		}
		return elapsed;
	}
}