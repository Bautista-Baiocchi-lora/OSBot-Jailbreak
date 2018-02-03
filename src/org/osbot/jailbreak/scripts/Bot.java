package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.core.Core;
import org.osbot.jailbreak.hooks.HookManager;

public class Bot {

    public static Object getBotAppInstance() {
        return Core.getReflectionEngine().getFieldValue(HookManager.getHook(HookManager.Key.BOT_APP_INSTANCE).getClassName(), HookManager.getHook(HookManager.Key.BOT_APP_INSTANCE).getTarget());
    }





}
