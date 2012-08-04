package net.citizensnpcs.questers;

public class QuestCancelException extends Exception {
	private final String reason;
	public QuestCancelException(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return this.reason;
	}

	private static final long serialVersionUID = 1L;
}
