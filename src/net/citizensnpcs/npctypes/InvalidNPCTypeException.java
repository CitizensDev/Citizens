package net.citizensnpcs.npctypes;

public class InvalidNPCTypeException extends Throwable {
	private static final long serialVersionUID = -7590280818602768146L;
	private String msg;

	public InvalidNPCTypeException(String msg) {
		this.msg = msg;
	}

	public String getMessage() {
		return msg;
	}
}