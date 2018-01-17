package org.osbot.jailbreak.data;

import org.osbot.jailbreak.util.reflection.ReflectionEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ethan on 1/12/2018.
 */
public class Engine {
    private static ReflectionEngine reflectionEngine;
    private static List<File> loadedFiles = new ArrayList<>();

    public static List<File> getLoadedFiles() {
        return loadedFiles;
    }

    public static ReflectionEngine getReflectionEngine() {

        return reflectionEngine;
    }

    public static void setReflectionEngine(ReflectionEngine reflectionEngine) {
        Engine.reflectionEngine = reflectionEngine;
    }

}
