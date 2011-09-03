package net.citizensnpcs.npctypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.utils.Messaging;
import net.citizensnpcs.utils.ServerUtils;

public class CitizensNPCLoader {

	public static CitizensNPCType loadNPCType(File file, Citizens plugin) {
		try {
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();
			String mainClass = null;
			while (entries.hasMoreElements()) {
				JarEntry element = entries.nextElement();
				if (element.getName().equalsIgnoreCase("type.info")) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									jarFile.getInputStream(element)));
					mainClass = reader.readLine().substring(6);
					break;
				}
			}
			if (mainClass != null) {
				ClassLoader loader = URLClassLoader.newInstance(
						new URL[] { file.toURI().toURL() }, plugin.getClass()
								.getClassLoader());
				Class<?> clazz = Class.forName(mainClass, true, loader);
				for (Class<?> subclazz : clazz.getClasses()) {
					// load extended classes.
					Class.forName(subclazz.getName(), true, loader);
				}
				Class<? extends CitizensNPCType> typeClass = clazz
						.asSubclass(CitizensNPCType.class);
				CitizensNPCType type = typeClass.newInstance();
				if (type.getProperties() == null) {
					throw new InvalidNPCTypeException(type.getName()
							+ " is missing a valid Properties class.");
				}
				if (type.getCommands() == null) {
					throw new InvalidNPCTypeException(type.getName()
							+ " is missing a valid Commands class.");
				}
				return NPCTypeManager.registerType(type);
			} else {
				throw new InvalidNPCTypeException("Failed to load "
						+ file.getName()
						+ ". Does the .jar file contain a npctype.info file?");
			}
		} catch (InvalidNPCTypeException ex) {
			Messaging.log(ex.getMessage());
			return null;
		} catch (Exception ex) {
			ServerUtils.ErrorReport((Exception) ex);
			ex.printStackTrace();
			return null;
		}
	}
}