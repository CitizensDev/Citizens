package com.citizens.Properties;

import java.util.HashMap;
import java.util.Map;

import com.citizens.Utils.Messaging;

public class SettingsTree {
	private final Map<String, Branch> tree = new HashMap<String, Branch>();

	public void populate(String path) {
		StringBuilder progressive = new StringBuilder();
		int index = 0;
		String[] branches = path.split("\\.");
		Branch previous = null, temp = null;
		for (String branch : branches) {
			progressive.append(branch);
			temp = new Branch(progressive.toString(), previous);
			previous = temp;
			if (getTree().get(progressive.toString()) == null) {
				getTree().put(progressive.toString(), previous);
			}
			if (index != branches.length - 1) {
				progressive.append(".");
			}
			++index;
		}
		tree.get(progressive.toString()).updateUpwards();
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
		get(path).updateUpwards();
		if (path.contains("0")) {
			Messaging.log(get(path).getTree().keySet().size(), "(22)");
		}
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

		public Branch(String path, Branch parent) {
			this.parent = parent;
			if (parent != null) {
				parent.addBranch(path, this);
				parent.updateUpwards();
			}
		}

		public void updateUpwards() {
			if (this.parent != null) {
				this.parent.update(tree);
			}
		}

		private void update(Map<String, Branch> tree) {
			this.tree.putAll(tree);
			if (this.parent != null) {
				this.parent.update(this.tree);
			}
		}

		public void removeUpwards() {
			if (this.parent != null) {
				this.parent.remove(tree);
			}
		}

		private void remove(Map<String, Branch> tree) {
			for (String string : tree.keySet()) {
				this.tree.remove(string);
			}
			if (this.parent != null) {
				this.parent.remove(tree);
			}
		}

		private void addBranch(String path, Branch branch) {
			this.tree.put(path, branch);
			if (parent != null) {
				parent.addBranch(path, branch);
			}
		}

		public Map<String, Branch> getTree() {
			return this.tree;
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