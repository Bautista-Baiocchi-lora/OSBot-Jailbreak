package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.core.Core;
import org.osbot.jailbreak.hooks.Hook;
import org.osbot.jailbreak.hooks.HookManager;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.util.reflection.ReflectedClass;
import org.osbot.jailbreak.util.reflection.ReflectedField;
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
		startScript(getBot(), getBottingPreferences(), scriptName);
	}

	/**
	 * @Class BotApplication
	 * @Method Class [Random]
	 * @Usage The insance for the class that holds bot preferences - Aka - User/Pass, dismiss? Breaks?
	 */
	public Object getPreferencesClassInstance() {
		Hook hook = HookManager.getHook(HookManager.Key.PREFERENCE_CLASS_INSTANCE);
		return getPreferenceValue(hook.getClassName(), hook.getTarget(),
				hook.getParameterCount(), hook.returnType(), getPreferenceHolderClassInstance(), "");
	}

	public Object getPreferenceHolderClassInstance() {
		Hook hook = HookManager.getHook(HookManager.Key.PREFERENCE_HOLDER_CLASS);
		return Core.getReflectionEngine().getMethodValue(hook.getClassName(), hook.getTarget(),
				hook.getParameterCount(), hook.returnType(), Core.getBotAppInstance());
	}

	/**
	 * @Class BottingPreferences [Random] - Can find in BotApplication
	 * @Method Random
	 * @Usage The insance for the current users bot preferences - Aka - User/Pass, dismiss? Breaks?
	 */
	public Object getBottingPreferences() {
		Hook hook = HookManager.getHook(HookManager.Key.BOT_PREFERENCES);
		return Core.getReflectionEngine().getMethodValue(hook.getClassName(), hook.getTarget(), hook.getParameterCount(), hook.returnType(), getPreferencesClassInstance());
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

	/**
	 * @Class BotApplication
	 * @Method Bot currentBot
	 * @Usage The instance for the current bot's tab.
	 */

	public Object getBot() {
		Hook hook = HookManager.getHook(HookManager.Key.BOT_INSTANCE);
		return getBotValue(hook.getClassName(), hook.getTarget(), hook.getParameterCount(), hook.returnType(), getBotClassInstance());
	}

	public Object getBotClassInstance() {
		Hook hook = HookManager.getHook(HookManager.Key.BOT_CLASS_INSTANCE);
		return getBotValue(hook.getClassName(), hook.getTarget(), hook.getParameterCount(), hook.returnType(), Core.getBotAppInstance());
	}
	/**
	 * @Class BotApplication
	 * @Method Bot currentBot
	 * @Usage The instance for the current bot's tab.
	 */
	public Object getBotValue(String className, String fieldName, int paramCount, String returnType, Object instance) {
		try {
			final ReflectedClass clazz = Core.getReflectionEngine().getClass(className, instance);
			for (ReflectedMethod m : clazz.getMethods()) {
				if (m.getName().equals(fieldName)) {
					if (m.getParameterCount() == paramCount) {
							if (m.getReturnType().toGenericString().equals(returnType)) {
							return m.invoke();
						}

					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			Logger.log(e.getLocalizedMessage());
		}
		return null;
	}

	public Object getPreferenceValue(String className, String fieldName, int paramCount, String returnType, Object instance, Object... params) {
		try {
			final ReflectedClass clazz = Core.getReflectionEngine().getClass(className, instance);
			for (ReflectedMethod m : clazz.getMethods()) {
				if (m.getName().equals(fieldName)) {
					if (m.getParameterCount() == paramCount) {
						if (m.getReturnType().toGenericString().equals(returnType)) {
							return m.invoke(params);
						}

					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			Logger.log(e.getLocalizedMessage());
		}
		return null;
	}
}
