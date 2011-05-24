package com.fullwall.Citizens.NPCTypes.Evil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fullwall.Citizens.Citizens;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class EvilData {
	private File names = new File("plugins/Citizens/Evil NPCs/names.citizens");
	private File remarks = new File(
			"plugins/Citizens/Evil NPCs/remarks.citizens");
	private List<File> files = new ArrayList<File>();

	public EvilData() {
		files.add(names);
		files.add(remarks);
		createFiles();
	}

	/**
	 * Create the files for Evil NPC data
	 * 
	 * @return
	 */
	private void createFiles() {
		for (File file : files) {
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					Citizens.log.info("[Citizens] Error creating file "
							+ file.getName());
				}
			}
		}
	}

	/**
	 * Choose a random name to give to an Evil NPC from a file
	 * 
	 * @param npc
	 */
	public void chooseRandomName(HumanNPC npc) {
		try {
			Scanner scanner = new Scanner(names);
			while (scanner.hasNextLine()) {
				npc.setName(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			// Could not find names.citizens, so the NPCs will be named aPunch
			Citizens.log
					.info("[Citizens] Could not find names.citizens in plugins/Citizens/Evil NPCs folder.");
			npc.setName("aPunch");
		}
	}
}