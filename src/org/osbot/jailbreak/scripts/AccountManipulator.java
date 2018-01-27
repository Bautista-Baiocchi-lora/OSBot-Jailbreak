package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.core.Core;
import org.osbot.jailbreak.hooks.Hook;
import org.osbot.jailbreak.hooks.HookManager;
import org.osbot.jailbreak.util.reflection.ReflectedClass;
import org.osbot.jailbreak.util.reflection.ReflectedMethod;

import java.lang.reflect.InvocationTargetException;

public class AccountManipulator {

	/**
	 * @Class BotApplication
	 * @Method Account OSBOT account details
	 * @Usage The details for OS-Bot account
	 */
	public static Object getAccount() {
		Hook hook = HookManager.getHook(HookManager.Key.ACCOUNT_INSTACE);
		return getAccountValue(hook.getClassName(), hook.getTarget(), hook.getParameterCount(), hook.returnType(), Bot.getBotAppInstance());
	}

	/**
	 * @Class BotApplication
	 * @Method Account OSBOT account details
	 * @Usage The details for OS-Bot account
	 */
	private static Object getAccountValue(String className, String fieldName, int paramCount, String returnType, Object instance) {
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
			e.printStackTrace();
		}
		return null;
	}
}
