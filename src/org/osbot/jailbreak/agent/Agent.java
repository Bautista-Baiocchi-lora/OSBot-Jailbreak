package org.osbot.jailbreak.agent;



import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;
import java.lang.instrument.Instrumentation;
import java.net.URL;

/**
 * Created by Ethan on 1/14/2018.
 */
public class Agent  {
	public static void agentmain(String args, Instrumentation instrumentation) {
		new MainFrame(instrumentation);
		Logger.log("[Agent] Successfully loaded into the JVM");
		try {
			JarLoader sc = new JarLoader(new URL(Constants.JAR_URL));
			for (String entry : sc.entries().keySet().toArray(new String[sc.entries().keySet().size()])) {
				try {
					if(entry.contains("ExRuneCraftGUI")) {
						Class<?> clazz = sc.loadClass(entry);
						Logger.log("We loaded: " + clazz.getName());
					}
				} catch (Exception e) {
					// ignored
				}
			}
			ClassLoader c = ClassLoader.getSystemClassLoader();
			Class<?> clazz = c.loadClass("scripts.exrunecrafter.gui.ExRuneCraftGUI");
			if(clazz != null) {
				Logger.log("HEYYY");
			} else {
				Logger.log("No go bro");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
