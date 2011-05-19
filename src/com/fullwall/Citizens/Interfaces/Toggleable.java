package com.fullwall.Citizens.Interfaces;

public interface Toggleable {
	public void toggle();

	public boolean getToggle();

	public String getName();

	public String getType();

	public void saveState();

	public void registerState();
}