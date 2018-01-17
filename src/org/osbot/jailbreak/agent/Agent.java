package org.osbot.jailbreak.agent;


import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.jar.JarFile;

/**
 * Created by Ethan on 1/14/2018.
 */
public class Agent {

	public static void agentmain(String args, Instrumentation instrumentation) {
		new MainFrame(instrumentation);
		Logger.log("[Agent] Successfully loaded into the JVM");
		for (String jarUrl : Constants.JAR_URLS) {
			try {
				File jarFile = downloadJarFile(jarUrl);
				Logger.log("Downloaded");
				instrumentation.appendToSystemClassLoaderSearch(new JarFile(jarFile));
				Class<?> c = ClassLoader.getSystemClassLoader().loadClass("org.osbot.maestro.script.MaestroSlayer");
				if (c != null) {
					Logger.log("Script injected.");
					jarFile.delete();
				} else {
					Logger.logException("Failed to inject script.");
					jarFile.delete();
				}
			} catch (IOException e) {
				Logger.log(e.getLocalizedMessage());
			} catch (ClassNotFoundException e) {
				Logger.log(e.getLocalizedMessage());
			}
		}
	}


	private static File downloadJarFile(String url) throws IOException {
		File tempFile = File.createTempFile("o", ".jar");
		tempFile.deleteOnExit();
		URL download = new URL(url);
		ReadableByteChannel rbc = Channels.newChannel(download.openStream());
		FileOutputStream fileOut = new FileOutputStream(tempFile);
		fileOut.getChannel().transferFrom(rbc, 0, 1 << 24);
		fileOut.close();
		rbc.close();
		return tempFile;
	}
}
