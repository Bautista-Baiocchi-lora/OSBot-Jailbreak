package org.osbot.jailbreak.agent;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.jar.JarFile;

/**
 * Created by Ethan on 1/14/2018.
 */

public class Agent implements ClassFileTransformer {


	public static void agentmain(String args, Instrumentation instrumentation) {
		new MainFrame(instrumentation);
		Logger.log("[Agent] Successfully loaded into the JVM");
		try {

			JarFile jarFile = new JarFile(Constants.ABSOLUTE_JAR_PATH);
			instrumentation.appendToSystemClassLoaderSearch(jarFile);

			Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("org.khal.khalcrafter.KhalAIOCrafter");
			if (clazz != null) {
				Logger.log("Class loader found.");
			} else {

			}
		} catch (Exception e) {
			Logger.log(e.getLocalizedMessage());
		}

	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
	                        Class<?> redefinedClass, ProtectionDomain protDomain, byte[] classBytes) {
		Logger.log("Adding: " + className + " to cache..");
		return classBytes;
	}
}
