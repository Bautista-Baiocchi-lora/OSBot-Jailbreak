package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.ui.logger.Logger;

import java.lang.instrument.Instrumentation;
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
    }

    public TreeMap<String, Object> localScriptMap() {
        return null;
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
                Logger.log("Found one: " + clazz.getName());
            }
        }
    }


}
