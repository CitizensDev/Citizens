package com.citizens.Properties;

import java.util.HashMap;
import java.util.Map;

public class SettingsTree {
	private final Map<String, Branch> tree = new HashMap<String, Branch>();

	public void populate(String path) {
		StringBuilder progressive = new StringBuilder();
		short index = 0;
		String[] branches = path.split("\\.");
		Branch previous = null, temp = null;
		for (String branch : branches) {
			progressive.append(branch);
			temp = new Branch(progressive.toString(), previous);
			previous = temp;
			if (getTree().get(progressive.toString()) == null)
				getTree().put(progressive.toString(), previous);
			if (index != branches.length - 1)
				progressive.append(".");
			++index;
		}
	}

	public Branch get(String path) {
		return getTree().get(path);
	}

	public Map<String, Branch> getTree() {
		return tree;
	}

	public void set(String path, String value) {
		populate(path);
		get(path).set(value);
	}

	public void remove(String path) {
		get(path).removeUpwards();
		for (String key : get(path).getTree().keySet()) {
			tree.remove(key);
		}
		tree.remove(path);
	}

	public class Branch {
		private final Map<String, Branch> tree = new HashMap<String, Branch>();
		private String value = "";
		private final Branch parent;
		private final String path;

		public Branch(String path, Branch parent) {
			this.path = path;
			this.parent = parent;
			if (parent != null)
				parent.addBranch(path, this);
		}

		public void removeUpwards() {
			if (this.parent != null)
				this.parent.remove(path);
		}

		private void remove(String path) {
			this.tree.remove(path);
			if (this.parent != null)
				this.parent.remove(path);
		}

		public Map<String, Branch> getTree() {
			return this.tree;
		}

		private void addBranch(String path, Branch branch) {
			if (parent != null)
				parent.addBranch(path, branch);
			this.tree.put(path, branch);
		}

		public void set(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

		public Branch getBranch(String path) {
			return this.tree.get(path);
		}
	}
}
