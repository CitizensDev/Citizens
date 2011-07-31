package com.citizens.npctypes.guards;

public class FlagInfo {
	private final int priority;
	private final String name;
	private final boolean safe;

	private FlagInfo(String name, int priority, boolean safe) {
		this.name = name;
		this.priority = priority;
		this.safe = safe;
	}

	public int priority() {
		return priority;
	}

	public boolean isSafe() {
		return safe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null || getClass() != that.getClass()) {
			return false;
		}
		FlagInfo other = (FlagInfo) that;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	public String getName() {
		return this.name;
	}

	public static FlagInfo newInstance(String name) {
		return newInstance(name, 1);
	}

	public static FlagInfo newInstance(String name, boolean safe) {
		return newInstance(name, 1, safe);
	}

	public static FlagInfo newInstance(String name, int priority) {
		return newInstance(name, priority, true);
	}

	public static FlagInfo newInstance(String name, int priority, boolean safe) {
		if (priority < 1 || priority > 20) {
			throw new IllegalArgumentException(
					"priority must be between 1 and 20");
		}
		return new FlagInfo(name, priority, safe);
	}
}