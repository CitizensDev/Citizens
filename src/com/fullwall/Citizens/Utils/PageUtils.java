package com.fullwall.Citizens.Utils;

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

		public PageInstance(Player player) {
			this.player = player;
		}

		public void header(String header) {
			this.header = colour(header);
		}

		public void push(String line) {
			lines.add(line);
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
			String tempHeader = header;
			int pages = maxPages();

			tempHeader = header.replace("%x", "" + page);
			tempHeader = header.replace("%y", "" + pages);
			send(tempHeader);

			int highNum = (page * 9);
			int lowNum = (page - 1) * 9;
			for (int number = lowNum; (number < highNum && number < lines
					.size()); ++number) {
				send(lines.get(number));
			}
		}

		private void send(String line) {
			player.sendMessage(line);
		}

		private String colour(String line) {
			return StringUtils.colourise(line);
		}
	}
}