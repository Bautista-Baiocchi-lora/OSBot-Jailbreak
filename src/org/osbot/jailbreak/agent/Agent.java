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
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Ethan on 1/14/2018.
 */
public class Agent {
    private static File file;

    public static void agentmain(String args, Instrumentation instrumentation) {
        new MainFrame(instrumentation);
        System.setSecurityManager(null);
        Logger.log("[Agent] Successfully loaded into the JVM");
        for (String jarUrl : Constants.JAR_URLS) {
            try {
                file = downloadJarFile(jarUrl);
                Logger.log("Downloaded");
            } catch (IOException e) {
                Logger.log(e.getLocalizedMessage());
            }
        }
        String jarPath = file.getAbsolutePath();
        Logger.log(jarPath);
        try {
            instrumentation.appendToSystemClassLoaderSearch(new JarFile(jarPath));
            loadAllClasses(new JarFile(jarPath));
            Engine.setReflectionEngine(new ReflectionEngine(ClassLoader.getSystemClassLoader()));
        } catch (IOException io) {
            Logger.log("IO EX: appending to system");
        }
    }


    private static File downloadJarFile(String url) throws IOException {
        File tempFile = new File(System.getProperty("user.home") + File.separator + "test.jar");
        tempFile.deleteOnExit();
        URL download = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(download.openStream());
        FileOutputStream fileOut = new FileOutputStream(tempFile);
        fileOut.getChannel().transferFrom(rbc, 0, 1 << 24);
        fileOut.close();
        rbc.close();
        return tempFile;
    }

    public static void loadAllClasses(JarFile file) {
        Enumeration<JarEntry> entries = file.entries();
        String className = null;
        while (entries.hasMoreElements()) {
            JarEntry e = entries.nextElement();
            if (e.getName().endsWith(".class")) {
                try {
                    className = e.getName().replace('/', '.').replaceAll(".class", "");
                    ClassLoader.getSystemClassLoader().loadClass(className);
                }catch (ClassNotFoundException e1) {
                    Logger.log("CLASS NOT FOUND: "+className);
                }
            }
        }
    }
}
