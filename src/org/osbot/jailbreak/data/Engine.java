package org.osbot.jailbreak.data;

import org.osbot.jailbreak.util.reflection.ReflectionEngine;


/**
 * Created by Ethan on 1/12/2018.
 */
public class Engine {
    private static ReflectionEngine reflectionEngine;
    public static ReflectionEngine getReflectionEngine() {
        return reflectionEngine;
    }

    public static void setReflectionEngine(ReflectionEngine reflectionEngine) {
        Engine.reflectionEngine = reflectionEngine;
    }

}
