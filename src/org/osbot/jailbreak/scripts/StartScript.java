package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.data.Engine;

/**
 * Created by Ethan on 1/17/2018.
 */
public class StartScript {


    public StartScript() {
        startScript("Fuck fruity");
    }


    public void startScript(String scriptName) {
        Engine.getReflectionEngine().startScript(getBot(), getCurrentTab(), scriptName);
    }

    public Object getBot() {
        return Engine.getReflectionEngine().getBotValue("org.osbot.BotApplication", "iiIIiiiIiIii", 0, "public class org.osbot.rs07.Bot", Engine.getReflectionEngine().getBotAppInstance());
    }

    public Object getKc() {
        return Engine.getReflectionEngine().getMethodValue("org.osbot.BotApplication", "iiIIiiiIiIii", 0, "public class org.osbot.Kc", Engine.getReflectionEngine().getBotAppInstance());
    }
    public Object getCurrentTab() {
        return Engine.getReflectionEngine().getMethodValue("org.osbot.Kc", "iiIIiiiIiIii", 1, "public class org.osbot.ad", getKc());
    }
}
