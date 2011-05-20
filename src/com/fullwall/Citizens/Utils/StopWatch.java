package com.fullwall.Citizens.Utils;

import com.fullwall.Citizens.Citizens;

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

	public void stop() {
		this.stopTime = System.nanoTime();
		this.running = false;
	}

	public void print() {
		Citizens.log.info("Start time: " + convertHours(startTime)
				+ "h. Elapsed: " + toMilliseconds(getElapsedTime())
				+ "ms. Stopped: " + convertHours(stopTime) + "h.");
	}

	public void printElapsed() {
		Citizens.log.info("Elapsed: " + toMilliseconds(getElapsedTime())
				+ "ms.");
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