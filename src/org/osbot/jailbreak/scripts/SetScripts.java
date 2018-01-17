package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.ui.logger.Logger;

import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

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
        setLocalScriptMap();
        new StartScript();
    }


    public void setLocalScriptMap() {
        Engine.getReflectionEngine().setFieldValue("org.osbot.LPT8", "iIIIiiiIiiII", newClassMap);
    }

    public Object newLocalScriptInstance(Class<?> clazz) {
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            return classLoader.loadClass("org.osbot.SA").getDeclaredConstructor(Class.class, boolean.class).newInstance(clazz, true);
        } catch (Exception e) {
            Logger.log("ERROR: " + e.getLocalizedMessage());
        }
        return null;
    }

    public Class scriptManifestClass() {
        try {
            return ClassLoader.getSystemClassLoader().loadClass("org.osbot.rs07.script.ScriptManifest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getScriptManifestName(Object obj) {
        return (String) Engine.getReflectionEngine().getFieldValue("org.osbot.rs07.script.ScriptManifest", "name", obj);
    }
    public void addToTreeMap() {
        for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
            if (clazz.isAnnotationPresent(scriptManifestClass())) {
                Logger.log("Manifest is present for: " + clazz.getName());
                final Annotation manifest = clazz.getAnnotation(scriptManifestClass());
                if (manifest != null) {
                    Logger.log("Adding to map: "+clazz.getName());
                    newClassMap.put("Fruity Money Snake", newLocalScriptInstance(clazz));
                }
            }
        }
    }

}
