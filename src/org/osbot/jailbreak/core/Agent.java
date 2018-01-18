package org.osbot.jailbreak.core;


import org.osbot.jailbreak.hooks.Hook;
import org.osbot.jailbreak.hooks.HookManager;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;
import org.osbot.jailbreak.util.reflection.ReflectionEngine;

import javax.swing.*;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

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
		new MainFrame(instrumentation);
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
		Hook hook = HookManager.getHook(HookManager.Key.VIP);
		reflectionEngine.setFieldValue(hook.getClassName(), hook.getTarget(), true, getBotAppInstance());
		Logger.log("VIP permissions granted.");
	}

	public static ReflectionEngine getReflectionEngine() {
		return reflectionEngine;
	}

	public static Object getBotAppInstance() {
		return reflectionEngine.getFieldValue(HookManager.getHook(HookManager.Key.BOT_APP_INSTANCE).getClassName(), HookManager.getHook(HookManager.Key.BOT_APP_INSTANCE).getTarget());
	}


	private static String getMacAddress() throws UnknownHostException, SocketException {
		NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
		byte[] mac = network.getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		}
		return sb.toString();
	}

}
