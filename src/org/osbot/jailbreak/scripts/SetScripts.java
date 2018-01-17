package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.ui.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Created by Ethan on 1/17/2018.
 */
public class SetScripts {
    private Instrumentation instrumentation;
    private TreeMap<String, Object> newClassMap = new TreeMap<>();

    public SetScripts(Instrumentation instrumentation) {

        this.instrumentation = instrumentation;
        Logger.log("Starting the search for classes");
        addToTreeMap();
        new StartScript();
    }

    private void addToTreeMap() {
        newClassMap.put("Fuck fruity", getSDNScript(Constants.JAR_URLS[0]));
        Engine.getReflectionEngine().setFieldValue("org.osbot.LPT8", "iIIIiiiIiiII", newClassMap);
    }

    private synchronized Object getSDNScript(String link) {
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
            return newLocalScriptInstance(new ScriptClassLoader(classMap).classAtomicReference.get());
        } catch (Exception e) {
            Logger.log("Error in grabbing script");
        }
        return null;
    }

    private static byte[] getByteArray(String link) {
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


    private Object newLocalScriptInstance(Class<?> clazz) {
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            return classLoader.loadClass("org.osbot.SA").getDeclaredConstructor(Class.class, boolean.class).newInstance(clazz, false);
        } catch (Exception e) {
            Logger.log("ERROR: " + e.getLocalizedMessage());
        }
        return null;
    }

}
