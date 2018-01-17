package org.osbot.jailbreak.agent;


import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.util.reflection.ReflectionEngine;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * Created by Ethan & Bautsita on 1/14/2018.
 */
public class Agent {

	private static ReflectionEngine reflectionEngine;

	public static void agentmain(String args, Instrumentation instrumentation) {
		new MainFrame(instrumentation);
		Logger.log("Injecting jailbreak...");
		try {
			reflectionEngine = new ReflectionEngine(ClassLoader.getSystemClassLoader());
			Logger.log("Jailbreak injected.");
		} catch (IOException io) {
			Logger.logException("Jailbreak failed to inject!");
		}
	}

	public static ReflectionEngine getReflectionEngine() {
		return reflectionEngine;
	}

	public static Object getBotAppInstance() {
		return reflectionEngine.getFieldValue("org.osbot.BotApplication", "iiiiiiiiIiii");
	}

}
