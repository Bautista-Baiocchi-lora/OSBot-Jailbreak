package org.osbot.jailbreak.core;


import org.osbot.jailbreak.hooks.Hook;
import org.osbot.jailbreak.hooks.HookManager;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.util.NetUtils;
import org.osbot.jailbreak.util.reflection.ReflectedClass;
import org.osbot.jailbreak.util.reflection.ReflectedMethod;
import org.osbot.jailbreak.util.reflection.ReflectionEngine;

import javax.swing.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

;

/**
 * Created by Ethan & Bautsita on 1/14/2018.
 */
public class Agent {

	private static ReflectionEngine reflectionEngine;

	public static void agentmain(String args, Instrumentation instrumentation) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (IllegalAccessException | ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new MainFrame(instrumentation, args.split(":")[0]);
		try {
			Logger.log("Double checking permission...");
			if (getHWID().equalsIgnoreCase(args.split(":")[1])) {
				if (NetUtils.isValidHwid(args.split(":")[1])) {
					if (NetUtils.isVIP(args.split(":")[0])) {
						Logger.log("Access granted, Happy botting!");
					} else {
						Logger.log("Access denied.");
						System.exit(0);
					}
				} else {
					Logger.log("Access denied.");
					System.exit(0);
				}
			} else {
				Logger.log("Access denied.");
				System.exit(0);
			}
		} catch (Exception e) {
			Logger.log(e.getLocalizedMessage());
		}
		Logger.log("Preparing jailbreak...");
		new HookManager();
		Logger.log("Injecting jailbreak...");
		try {
			reflectionEngine = new ReflectionEngine(ClassLoader.getSystemClassLoader());
			Logger.log("Jailbreak injected.");
		} catch (IOException io) {
			Logger.logException("Jailbreak failed to inject!");
		}
		Logger.log("Granting VIP permissions.");
		Hook vipHook = HookManager.getHook(HookManager.Key.VIP);
		reflectionEngine.setFieldValue(vipHook.getClassName(), vipHook.getTarget(), true, getAccount());
		Hook devHook = HookManager.getHook(HookManager.Key.DEV);
		reflectionEngine.setFieldValue(devHook.getClassName(), devHook.getTarget(), true, getAccount());
		Logger.log("VIP permissions granted.");
		Logger.log("Spoofing OSBot Username");
		Hook name = HookManager.getHook(HookManager.Key.NAME);
		reflectionEngine.setFieldValue(name.getClassName(), name.getTarget(), "Alek", getAccount());
	}

	public static ReflectionEngine getReflectionEngine() {
		return reflectionEngine;
	}

	public static Object getBotAppInstance() {
		return reflectionEngine.getFieldValue(HookManager.getHook(HookManager.Key.BOT_APP_INSTANCE).getClassName(), HookManager.getHook(HookManager.Key.BOT_APP_INSTANCE).getTarget());
	}

	public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String s = "";
		final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
		final byte[] bytes = main.getBytes("UTF-8");
		final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		final byte[] md5 = messageDigest.digest(bytes);
		int i = 0;
		for (final byte b : md5) {
			s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
			if (i != md5.length - 1) {
				s += "-";
			}
			i++;
		}
		return s;
	}

	/**
	 * @Class BotApplication
	 * @Method Account OSBOT account details
	 * @Usage The details for OS-Bot account
	 */
	public static Object getAccount() {
		Hook hook = HookManager.getHook(HookManager.Key.ACCOUNT_INSTACE);
		return getAccountValue(hook.getClassName(), hook.getTarget(), hook.getParameterCount(), hook.getReturType(), getBotAppInstance());
	}

	/**
	 * @Class BotApplication
	 * @Method Account OSBOT account details
	 * @Usage The details for OS-Bot account
	 */
	public static Object getAccountValue(String className, String fieldName, int paramCount, String returnType, Object instance) {
		try {
			final ReflectedClass clazz = Agent.getReflectionEngine().getClass(className, instance);
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
