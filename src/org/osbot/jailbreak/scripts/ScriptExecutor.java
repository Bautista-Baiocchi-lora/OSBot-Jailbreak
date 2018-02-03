package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.core.Core;
import org.osbot.jailbreak.hooks.Hook;
import org.osbot.jailbreak.hooks.HookManager;
import org.osbot.jailbreak.threadding.ThreadExecutor;
import org.osbot.jailbreak.ui.logger.Logger;


/**
 * Created by Ethan on 2/3/2018.
 */


public final class ScriptExecutor implements Runnable {
    public final String scriptName;
    private final Class<?> scriptMain;
    private final Object bot;

    public ScriptExecutor(String scriptName, Class<?> scriptMain) {
        this.bot = getBot();
        this.scriptName = scriptName;
        this.scriptMain = scriptMain;
        ThreadExecutor.startThread(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (bot == null) {
                    Logger.log("Please start a bot first.");
                    return;
                }
                final Object script = scriptMain.getConstructor((Class<?>[]) new Class[0]).newInstance(new Object[0]);
                if (script != null) {
                    Logger.log("Starting Script...");
                }
                startScript(script);
                return;
            } catch (Exception ex) {
                return;
            }
        }
    }

    public Object startScript(Object obj) {
        return Core.getReflectionEngine().getMethodValue("org.osbot.rs07.event.ScriptExecutor", "start", 1, "void", getScriptExecutor(), obj);
    }

    public Object getScriptExecutor() {
        return Core.getReflectionEngine().getMethodValue("org.osbot.rs07.Bot", "getScriptExecutor", 0, "public class org.osbot.rs07.event.ScriptExecutor", getBot());
    }

    public Object getBot() {
        Hook hook = HookManager.getHook(HookManager.Key.BOT_INSTANCE);
        return Core.getReflectionEngine().getMethodValue(hook.getClassName(), hook.getTarget(), hook.getParameterCount(), hook.returnType(), getBotHolder());
    }

    public Object getBotHolder() {
        Hook hook = HookManager.getHook(HookManager.Key.BOT_HOLDER_INSTANCE);
        return Core.getReflectionEngine().getMethodValue(hook.getClassName(), hook.getTarget(), hook.getParameterCount(), hook.returnType(), Core.getBotAppInstance());
    }

}

