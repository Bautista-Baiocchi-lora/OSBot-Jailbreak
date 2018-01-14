package org.osbot.jailbreak.agent;

import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Ethan on 1/14/2018.
 */

public class Agent implements ClassFileTransformer {


	public static void agentmain(String args, Instrumentation instrumentation) {
		new MainFrame(instrumentation);
		Logger.log("[Agent] Successfully loaded into the JVM");
		try {
			String link = "https://www.dropbox.com/s/cn3z8hziawpr8od/KhalCrafter.jar?dl=1";
			URL url = new URL("jar:"+link+"!/");
			JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
			JarFile jar = getJar(jarConnection);

			if(jar != null) {
				Logger.log("We found the jar.");
				instrumentation.appendToSystemClassLoaderSearch(jar);
			}
			Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("org.khal.khalcrafter.KhalAIOCrafter");
			if(clazz != null) {
				Logger.log("WE LOADED");
			} else {
				Logger.log("NOPE");
			}
		} catch (Exception e) {
			Logger.log(e.getLocalizedMessage());
		}

	}

	public static JarFile getJar(URLConnection connection) {
		JarFile jarFile = null;
		try {
			if (connection != null && connection instanceof JarURLConnection) {
				JarURLConnection jarURLConnection = (JarURLConnection) connection;
				jarFile = jarURLConnection.getJarFile();
				Enumeration<JarEntry> entries = jarFile.entries();
				Logger.log("Size: "+jarFile.size());
				while (entries.hasMoreElements()) {
					JarEntry jarEntry = entries.nextElement();
					if (jarEntry.getName().endsWith(".class")) {
						Logger.log("Name: "+jarEntry.getName());
					}
				}
				return jarFile;
			}
		} catch (IOException ioe) {
			Logger.log("Caught error.");
		} finally {
			if (jarFile != null) {
				Logger.log("file exists wow.");
				try {
					jarFile.close();
				} catch (IOException ignored) {
					Logger.log("caught error 2");
				}
			}
		}
		Logger.log("Nope.");
		return null;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
	                        Class<?> redefinedClass, ProtectionDomain protDomain, byte[] classBytes) {
		Logger.log("Adding: " + className + " to cache..");
		return classBytes;
	}
}
