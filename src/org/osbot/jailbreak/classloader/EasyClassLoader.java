package org.osbot.jailbreak.classloader;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.logger.Logger;

import javax.swing.text.Utilities;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Ethan on 1/14/2018.
 */
public class EasyClassLoader extends ClassLoader {

    private Map<String, byte[]> classCache;

    public EasyClassLoader(Map<String, byte[]> map) {
        this.classCache = map;
    }

    public final void defineClasses() {
        try {
            Logger.log("Bout to define: " + classCache.size());
            for (Map.Entry<String, byte[]> entry : classCache.entrySet()) {
                if(entry.getValue() != null) {
                    Logger.log("Define: " + entry.getKey() + " || Length: " + entry.getValue().length);
                    String keyName = entry.getKey().replace('/', '.');
                    Logger.log(keyName);
                    defineClass(keyName, entry.getValue(), 0, entry.getValue().length);
                }
            }

        } catch (Exception e) {
            Logger.log(e.getLocalizedMessage());
        }
    }
    private boolean stringContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }

}

