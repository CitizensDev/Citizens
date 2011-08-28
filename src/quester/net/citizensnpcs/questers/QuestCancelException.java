package net.citizensnpcs.questers;

public class QuestCancelException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String reason;

	public QuestCancelException(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return this.reason;
	}
}
