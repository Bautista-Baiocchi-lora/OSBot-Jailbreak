package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.ui.logger.Logger;

import java.util.List;

/**
 * Created by Ethan on 1/16/2018.
 */
public class HandleRefresh {

    public HandleRefresh() {
        Logger.log("Calling for refresh.");
        Engine.getReflectionEngine().getMethodValue("org.osbot.lPt7", "IiIiIiiIiiii", 0, getSelectorInstance());
        closeAllClassLoaders();
    }

    public Object getSelectorInstance() {
        return Engine.getReflectionEngine().getFieldValue("org.osbot.BotApplication", "IiIIIiiiIiii", Engine.getReflectionEngine().getBotAppInstance());
    }

    public List<Object> getClassLoaders() {
        return (List<Object>) Engine.getReflectionEngine().getFieldValue("org.osbot.Gb", "IIiiiiiIIIii");
    }

    public void closeAllClassLoaders() {
        Logger.log(getClassLoaders().size() + " : Size");
        for (Object obj : getClassLoaders()) {
            close(obj);
        }
        close();
        System.gc();

    }

    public void close(Object obj) {
        Engine.getReflectionEngine().getMethodValue("org.osbot.Lpt8", "close", 0, obj);
    }

    public void close() {
        try {
            Class clazz = java.net.URLClassLoader.class;
            java.lang.reflect.Field ucp = clazz.getDeclaredField("ucp");
            ucp.setAccessible(true);
            Object sun_misc_URLClassPath = ucp.get(this);
            java.lang.reflect.Field loaders =
                    sun_misc_URLClassPath.getClass().getDeclaredField("loaders");
            loaders.setAccessible(true);
            Object java_util_Collection = loaders.get(sun_misc_URLClassPath);
            for (Object sun_misc_URLClassPath_JarLoader :
                    ((java.util.Collection) java_util_Collection).toArray()) {
                try {
                    java.lang.reflect.Field loader =
                            sun_misc_URLClassPath_JarLoader.getClass().getDeclaredField("jar");
                    loader.setAccessible(true);
                    Object java_util_jar_JarFile =
                            loader.get(sun_misc_URLClassPath_JarLoader);
                    ((java.util.jar.JarFile) java_util_jar_JarFile).close();
                } catch (Throwable t) {
                    // if we got this far, this is probably not a JAR loader so skip it
                }
            }
        } catch (Throwable t) {
            // probably not a SUN VM
        }
        return;
    }
}
