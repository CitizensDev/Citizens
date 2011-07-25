package com.citizens.npctypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.citizens.Citizens;
import com.citizens.utils.Messaging;

public class CitizensNPCLoader {

	public static CitizensNPC loadNPCType(File file, Citizens plugin) {
		try {
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();

			String mainClass = null;
			// register type in npctype.info file like so:
			// main-class: com.citizens.Blacksmith.Blacksmith
			while (entries.hasMoreElements()) {
				JarEntry element = entries.nextElement();
				if (element.getName().equalsIgnoreCase("npctype.info")) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									jarFile.getInputStream(element)));
					mainClass = reader.readLine().substring(12);
					break;
				}
			}
			if (mainClass != null) {
				ClassLoader loader = URLClassLoader.newInstance(
						new URL[] { file.toURI().toURL() }, plugin.getClass()
								.getClassLoader());
				Class<?> clazz = Class.forName(mainClass, true, loader);
				for (Class<?> subclazz : clazz.getClasses()) {
					Class.forName(subclazz.getName(), true, loader);
				}
				Class<? extends CitizensNPC> typeClass = clazz
						.asSubclass(CitizensNPC.class);
				Constructor<? extends CitizensNPC> ctor = typeClass
						.getConstructor();
				CitizensNPC type = ctor.newInstance();
				if (type.getProperties() == null) {
					throw new InvalidNPCTypeException(type.getType()
							+ " is missing a valid Properties class.");
				}
				if (type.getPurchaser() == null) {
					throw new InvalidNPCTypeException(type.getType()
							+ " is missing a valid Purchaser class.");
				}
				if (type.getCommands() == null) {
					throw new InvalidNPCTypeException(type.getType()
							+ " is missing a valid Commands class.");
				}
				return CitizensNPCManager.registerType(type);
			} else {
				throw new InvalidNPCTypeException("Failed to load "
						+ file.getName()
						+ ". Does the .jar file contain a npctype.info file?");
			}
		} catch (InvalidNPCTypeException ex) {
			Messaging.log(ex.getMessage());
			ex.printStackTrace();
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}