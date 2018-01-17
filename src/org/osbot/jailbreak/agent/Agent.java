package org.osbot.jailbreak.agent;


import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.scripts.SetScripts;
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
import java.util.jar.JarFile;

/**
 * Created by Ethan on 1/14/2018.
 */
public class Agent {
    private static long lastRand = 0;
    private static File jarFile;

    public static void agentmain(String args, Instrumentation instrumentation) {
        new MainFrame(instrumentation);
        Logger.log("[Agent] Successfully loaded into the JVM");
        for (String jarUrl : Constants.JAR_URLS) {
            try {
                jarFile = downloadJarFile(jarUrl);
                Logger.log("Downloaded");
                instrumentation.appendToSystemClassLoaderSearch(new JarFile(jarFile));
                Logger.log("Injected script");
                Class c = ClassLoader.getSystemClassLoader().loadClass("uk.co.ramyun.sandcrabs.Main");
                if(c != null) {
                    Logger.log("We found it");
                } else {
                    Logger.log("what the fuck");
                }
            } catch (IOException | ClassNotFoundException e) {
                Logger.log("what the fuck");
                e.printStackTrace();
                Logger.log(e.getLocalizedMessage());
            }
        }
        try {
            Engine.setReflectionEngine(new ReflectionEngine(ClassLoader.getSystemClassLoader()));
            new SetScripts(instrumentation);
        } catch (Exception e) {
            Logger.log(e.getLocalizedMessage());
        }
    }


    private static File downloadJarFile(String url) throws IOException {
        Random rand = new Random();
        long r = rand.nextLong();
        if (r == lastRand) {
            lastRand = r;
        }
        File tempFile = new File(System.getProperty("user.home") + File.separator + "test " + r + ".jar");
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
