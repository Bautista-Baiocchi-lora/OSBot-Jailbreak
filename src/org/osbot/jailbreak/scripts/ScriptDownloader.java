package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.ui.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Created by Ethan on 1/17/2018.
 */
public class ScriptDownloader {

    /**
     * @Author Ethan & Bautista
     * @Usage Downloads the ripped script and preps for run-time.
     */
    public ScriptDownloader(String creativeName, String link) {
        new ScriptExecutor(creativeName, getSDNScript(link));
    }

    /**
     * @return The main class along with a fresh classloader for givin' script.
     * @Usage This system downloads our SDN script.
     * It will store the bytes for the classes & resources in hashmaps.
     * We then create a new classloader containing those bytes. That class loader will search for the main class, then load it.
     * It will also set a reference for the main class, which we will use to create a new script out of.
     */
    private synchronized Class<?> getSDNScript(String link) {
        try {
            byte[] bytes = getByteArray(link);
            final HashMap<String, byte[]> classMap = new HashMap<>();
            final HashMap<String, byte[]> resourceMap = new HashMap<>();
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
                    classMap.put(nextEntry.getName(), byteArrayOutputStream.toByteArray());
                } else {
                    resourceMap.put(nextEntry.getName(), byteArrayOutputStream.toByteArray());
                }
            }
            return (new ScriptClassLoader(classMap, resourceMap).classAtomicReference.get());
        } catch (Exception e) {
            Logger.logException("Script injection error!");
        }
        return null;
    }

    /**
     * @Usage returns the bytes for the scripts jar.
     */
    private byte[] getByteArray(String link) {
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
            Logger.logException("Invalid data error!");
        }
        return null;
    }


}
