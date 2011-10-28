package net.citizensnpcs.guards;

public interface Selection<T> extends Iterable<T> {
	void select(T selection);

	void deselect(T selection);

	boolean isSelected(T selection);

	void deselectAll();

	int size();
}
