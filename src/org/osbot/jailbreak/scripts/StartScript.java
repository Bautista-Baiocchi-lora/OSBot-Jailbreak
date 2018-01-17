package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.util.reflection.ReflectedClass;
import org.osbot.jailbreak.util.reflection.ReflectedMethod;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Ethan on 1/17/2018.
 */
public class StartScript {

    /**
     * @Author Ethan & Bautista
     * @Usage Emulates the script loading system for osbot
     */
    public StartScript(String scriptName) {
        startScript(getBot(), getBottingPreferences(), scriptName);
    }

    /**
     * @Class BotApplication
     * @Method Class [Random]
     * @Usage The insance for the class that holds bot preferences - Aka - User/Pass, dismiss? Breaks?
     */
    public Object getPreferencesClassInstance() {
        return Engine.getReflectionEngine().getMethodValue("org.osbot.BotApplication", "iiIIiiiIiIii", 0, "public class org.osbot.Kc", Engine.getReflectionEngine().getBotAppInstance());
    }

    /**
     * @Class BottingPreferences [Random] - Can find in BotApplication
     * @Method Random
     * @Usage The insance for the current users bot preferences - Aka - User/Pass, dismiss? Breaks?
     */
    public Object getBottingPreferences() {
        return Engine.getReflectionEngine().getMethodValue("org.osbot.Kc", "iiIIiiiIiIii", 1, "public class org.osbot.ad", getPreferencesClassInstance());
    }

    /**
     * @Class Wherever the script gets executed from - REMINDER - Can be in BotApplication where CLI settings are handled!
     * @Method Class [Random]
     * @Usage The insance for the class that holds bot preferences - Aka - User/Pass, dismiss? Breaks?
     */
    public void startScript(Object bot, Object randoms, String scriptName) {
        try {
            final ReflectedClass clazz = Engine.getReflectionEngine().getClass("org.osbot.Gb");
            for (ReflectedMethod m : clazz.getMethods()) {
                if (m.getName().equals("iiIIiiiIiIii")) {
                    if (m.getParameterCount() == 4) {
                        Logger.log("We're invoking script start");
                        m.invoke(bot, randoms, scriptName, null);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Class BotApplication
     * @Method Bot currentBot
     * @Usage The instance for the current bot's tab.
     */
    public Object getBot() {
        return getBotValue("org.osbot.BotApplication", "iiIIiiiIiIii", 0, "public class org.osbot.rs07.Bot", Engine.getReflectionEngine().getBotAppInstance());
    }

    /**
     * @Class BotApplication
     * @Method Bot currentBot
     * @Usage The instance for the current bot's tab.
     */
    public Object getBotValue(String className, String fieldName, int paramCount, String returnType, Object instance) {
        try {
            final ReflectedClass clazz = Engine.getReflectionEngine().getClass(className, instance);
            for (ReflectedMethod m : clazz.getMethods()) {
                if (m.getName().equals(fieldName)) {
                    if (m.getParameterCount() == paramCount) {
                        if(m.getReturnType().toGenericString().equals(returnType)) {
                            Logger.log("Getting Bot Value");
                            return m.invoke();
                        }

                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
