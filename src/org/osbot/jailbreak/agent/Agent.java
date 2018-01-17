package org.osbot.jailbreak.agent;


import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.scripts.HandleRefresh;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.util.reflection.ReflectionEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Random;

/**
 * Created by Ethan on 1/14/2018.
 */
public class Agent {
	private static long lastRand = 0;
	public static void agentmain(String args, Instrumentation instrumentation) {
		new MainFrame(instrumentation);
		Logger.log("[Agent] Successfully loaded into the JVM");
		for (String jarUrl : Constants.JAR_URLS) {
			try {
				Engine.setReflectionEngine(new ReflectionEngine(ClassLoader.getSystemClassLoader()));
				File jarFile = downloadJarFile(jarUrl);
				Engine.getLoadedFiles().add(jarFile);
				String localPath = jarFile.getAbsolutePath().substring(0, jarFile.getAbsolutePath().indexOf("\\tribot"));
				Engine.getReflectionEngine().setFieldValue("org.osbot.Constants", "IiiIIiiiiIIi", localPath); //WE change the path to local jars
				Logger.log("Downloaded");
			} catch (IOException e) {
				Logger.log(e.getLocalizedMessage());
			}
		}

	}


	private static File downloadJarFile(String url) throws IOException {
		Random rand = new Random();
		long r = 0;
		if(r == lastRand) {
			r = rand.nextLong();
			lastRand = r;
		}

		File tempFile = File.createTempFile("tribot"+r, ".jar");
		Logger.log(tempFile.getAbsolutePath());
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
