package net.citizensnpcs.npctypes;

public class InvalidNPCTypeException extends Exception {
	private static final long serialVersionUID = -7590280818602768146L;
	private final String msg;

	public InvalidNPCTypeException(String msg) {
		this.msg = msg;
	}

	@Override
	public String getMessage() {
		return msg;
	}
}