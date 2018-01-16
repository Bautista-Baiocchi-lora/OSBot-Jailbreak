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
		JarLoader jr = new JarLoader(new URL(Constants.JAR_URL));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
