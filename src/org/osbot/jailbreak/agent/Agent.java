package org.osbot.jailbreak.agent;

import org.osbot.jailbreak.classloader.EasyClassLoader;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Created by Ethan on 1/14/2018.
 */

public class Agent {
    private static Agent instance;
    private Map<String, byte[]> scripts = new HashMap<>();

    public static void agentmain(String args, Instrumentation instrumentation) {
        new MainFrame(instrumentation);
        Logger.log("[Agent] Successfully loaded into the JVM");

        try {
            String link = "https://www.dropbox.com/s/cn3z8hziawpr8od/KhalCrafter.jar?dl=1";
            doShit(link);
            EasyClassLoader easyClassLoader = new EasyClassLoader(instance.scripts);
            easyClassLoader.defineClasses();
        } catch (Exception e) {
            Logger.log(e.getLocalizedMessage());
        }
    }

    private static synchronized void doShit(String link) throws IOException {
        final byte[] bytes = getByteArray(link);
        final byte[] array = new byte[1024];
        final JarInputStream jarInputStream = new JarInputStream(new ByteArrayInputStream(bytes));
        ZipEntry nextEntry;
        while ((nextEntry = jarInputStream.getNextEntry()) != null) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JarInputStream jarInputStream2 = jarInputStream;
            int read;
            while ((read = jarInputStream2.read(array, 0, array.length)) != -1) {
                jarInputStream2 = jarInputStream;
                byteArrayOutputStream.write(array, 0, read);
            }
            if (nextEntry.getName().endsWith(".class")) {
                Logger.log("Populating" + nextEntry.getName());
                instance.scripts.put(nextEntry.getName(), byteArrayOutputStream.toByteArray());
            }
        }
    }

    public static byte[] getByteArray(String link) {
        try {
            URL url = new URL(link);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int read;
            while ((read = dataInputStream.read()) != -1) {
                byteArrayOutputStream.write(read);
            }
            dataInputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
