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
        for(Object obj : getClassLoaders()) {
            close(obj);
        }
    }
    public void close(Object obj) {
        Engine.getReflectionEngine().getMethodValue("org.osbot.Lpt8", "close", 0, obj);
    }
}
