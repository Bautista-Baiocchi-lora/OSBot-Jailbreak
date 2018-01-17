package org.osbot.jailbreak.agent;


import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.util.reflection.ReflectionEngine;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * Created by Ethan on 1/14/2018.
 */
public class Agent {

    public static void agentmain(String args, Instrumentation instrumentation) {
        new MainFrame(instrumentation);
        System.setSecurityManager(null);
        Logger.log("[Agent] Successfully loaded into the JVM");
        try {

            Engine.setReflectionEngine(new ReflectionEngine(ClassLoader.getSystemClassLoader()));
        } catch (IOException io) {
            Logger.log("IO EX: appending to system");
        }
    }
}
