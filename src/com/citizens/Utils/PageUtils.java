package com.citizens.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class PageUtils {
	public static PageInstance newInstance(Player player) {
		return new PageInstance(player);
	}

	public static class PageInstance {
		private String header = "";
		private final List<String> lines = new ArrayList<String>();
		private final Player player;
		private boolean smoothTransition = false;
		private boolean hasDisplayed = false;
		private int currentPage = 1;

		public PageInstance(Player player) {
			this.player = player;
		}

		public void header(String header) {
			this.header = colour(header);
		}

		public void push(String line) {
			lines.add(line);
		}

		public void setSmoothTransition(boolean smooth) {
			this.smoothTransition = smooth;
		}

		public int maxPages() {
			int pages = lines.size() / 9;
			if (lines.size() % 9 != 0)
				pages += 1;
			if (pages == 0)
				pages = 1;
			return pages;
		}

		public void process(int page) {
			if (!hasDisplayed)
				hasDisplayed = true;
			String tempHeader = header;
			int pages = maxPages();

			if (!(hasDisplayed && smoothTransition)) {
				tempHeader = header.replace("%x/%y", page + "/" + pages);
				send(tempHeader);
			}

			int highNum = (page * 9);
			int lowNum = (page - 1) * 9;
			for (int number = lowNum; (number < highNum && number < lines
					.size()); ++number) {
				send(lines.get(number));
			}
		}

		public void displayNext() {
			if (currentPage <= maxPages()) {
				++currentPage;
				process(currentPage);
			}
		}

		public int currentPage() {
			return this.currentPage;
		}

		private void send(String line) {
			Messaging.send(player, null, line);
		}

		private String colour(String line) {
			return StringUtils.colourise(line);
		}

		public int elements() {
			return lines.size();
		}
	}
}