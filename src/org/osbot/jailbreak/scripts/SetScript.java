package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.ui.logger.Logger;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ethan on 1/13/2018.
 */
public class SetScript {
    private Object scriptSelectorInstance;
    private Map<String, Class<?>> classMap = new HashMap<>();
    public SetScript() {
        if (getSelectorInstance() == null) {
            Logger.log("Set Script Selector");
            setScriptSelectorInstance();
        }
        try {
            Logger.log("Set Script SA");
            setSA(ClassLoader.getSystemClassLoader().loadClass("org.osbot.maestro.script.MaestroSlayer"));
            Logger.log("INVOKE START");
            Engine.getReflectionEngine().getMethodValue("org.osbot.lPt7", "iiIIiiiIiIii", 0, getSelectorInstance());
        } catch (Exception e) {
            Logger.log(e.getLocalizedMessage());
        }
        }

    public void setScriptSelectorInstance() {
        Object obj = Engine.getReflectionEngine().getClass("org.osbot.lPt7").getNewInstance();
        Engine.getReflectionEngine().setFieldValue("org.osbot.BotApplication", "IiIIIiiiIiii", obj);
        scriptSelectorInstance = getSelectorInstance();
    }

    public Object getSelectorInstance() {
        return Engine.getReflectionEngine().getFieldValue("org.osbot.BotApplication", "IiIIIiiiIiii", Engine.getReflectionEngine().getBotAppInstance());
    }


    public void setSA(Class<?> clazz) {
        Engine.getReflectionEngine().setFieldValue("org.osbot.lPt7", "iIIIiiiiIIii", newLocalScriptInstance(clazz), getSelectorInstance());
    }


    public Object newLocalScriptInstance(Class<?> clazz) {
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            return classLoader.loadClass("org.osbot.SA").getDeclaredConstructor(Class.class, boolean.class).newInstance(clazz, true);
        } catch(Exception e) {
            Logger.log("ERROR: "+e.getLocalizedMessage());
        }
        return null;
    }


}
