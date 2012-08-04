package net.citizensnpcs.guards;

public interface Selection<T> extends Iterable<T> {
	void deselect(T selection);

	void deselectAll();

	boolean isSelected(T selection);

	void select(T selection);

	int size();
}
