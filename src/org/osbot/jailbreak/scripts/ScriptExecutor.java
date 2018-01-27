package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.core.Core;
import org.osbot.jailbreak.hooks.Hook;
import org.osbot.jailbreak.hooks.HookManager;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.util.reflection.ReflectedClass;
import org.osbot.jailbreak.util.reflection.ReflectedMethod;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Ethan on 1/17/2018.
 */
public class ScriptExecutor {

	/**
	 * @Author Ethan & Bautista
	 * @Usage Emulates the script loading system for osbot
	 */
	public ScriptExecutor(String scriptName) {
		startScript(Core.getBot().getBot(), Core.getBot().getBottingPreferences(), scriptName);
	}

	/**
	 * @Class Wherever the script gets executed from - REMINDER - Can be in BotApplication where CLI settings are handled!
	 * @Method Class [Random]
	 * @Usage The insance for the class that holds bot preferences - Aka - User/Pass, dismiss? Breaks?
	 */
	public void startScript(Object bot, Object randoms, String scriptName) {
		try {
			Hook hook = HookManager.getHook(HookManager.Key.START_SCRIPT);
			final ReflectedClass clazz = Core.getReflectionEngine().getClass(hook.getClassName());
			for (ReflectedMethod m : clazz.getMethods()) {
				if (m.getName().equals(hook.getTarget())) {
					if (m.getParameterCount() == hook.getParameterCount()) {
						Logger.log("Starting script...");
						m.invoke(291, bot, randoms, scriptName, null);
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			Logger.log(e.getLocalizedMessage());
		}
	}


}
