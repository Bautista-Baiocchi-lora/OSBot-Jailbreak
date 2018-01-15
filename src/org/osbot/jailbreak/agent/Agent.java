package org.osbot.jailbreak.agent;


import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.injector.DependencyDetector;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;

import java.io.*;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Created by Ethan on 1/14/2018.
 */

public class Agent {
	public static LinkedHashMap<String, byte[]> scripts = new LinkedHashMap<>();
	public static LinkedHashMap<String, ClassNode> classNodes = new LinkedHashMap<>();
	private static final DependencyDetector dependencies = new DependencyDetector();
	public static void agentmain(String args, Instrumentation instrumentation) {
		new MainFrame(instrumentation);
		Logger.log("[Agent] Successfully loaded into the JVM");
		try {
			downloadScript(Constants.JAR_URL);
		} catch (Exception e) {
			Logger.log(e.getLocalizedMessage());
		}
		loadIntoClassLoader(ClassLoader.getSystemClassLoader());
	}

	private static void downloadScript(String link) throws IOException {
		Logger.log("Downloading...");
		final byte[] bytes = getByteArray(link);
		final byte[] array = new byte[1024];
		final JarInputStream jarInputStream = new JarInputStream(new ByteArrayInputStream(bytes));
		ZipEntry nextEntry;
		while ((nextEntry = jarInputStream.getNextEntry()) != null) {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			JarInputStream jarInputStream2 = jarInputStream;
			int read;
			while ((read = jarInputStream2.read(array, 0, array.length)) != -1) {
				jarInputStream2 = jarInputStream;
				byteArrayOutputStream.write(array, 0, read);
			}
			if (nextEntry.getName().endsWith(".class")) {
				if (byteArrayOutputStream.toByteArray() != null) {
					Logger.log("Populating: " + nextEntry.getName());
					scripts.put(nextEntry.getName().replace(".class", ""), byteArrayOutputStream.toByteArray());
					loadClass(jarInputStream2);
				}
			}
		}
		for(Map.Entry<String, ClassNode> entry : classNodes.entrySet()) {
			Logger.log("Adding node: "+entry.getKey());
			dependencies.add(entry.getValue());
		}
		Logger.log("Download finished.");
	}

	private static void loadClass(InputStream inputStream) throws IOException {
		try {
			ClassReader cr = new ClassReader(inputStream);
			ClassNode cn = new ClassNode();
			cr.accept(cn, ClassReader.EXPAND_FRAMES);
			classNodes.put(cn.name, cn);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] getByteArray(String link) {
		try {
			URL url = new URL(link);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			int read;
			while ((read = dataInputStream.read()) != -1) {
				byteArrayOutputStream.write(read);
			}
			dataInputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void loadIntoClassLoader(ClassLoader loader) {
		String[] classNames = scripts.keySet().toArray(new String[0]);
		final String[] classNamesToLoad =
				dependencies.getClassesToLoad(classNames);
		Method defineClass = null;
		Method findLoadedClass = null;
		try {
			// crack ClassLoader wide open and force-feed it with our classes
			defineClass = ClassLoader.class.getDeclaredMethod(
					"defineClass", String.class, byte[].class,
					int.class, int.class);
			defineClass.setAccessible(true);
			findLoadedClass = ClassLoader.class.getDeclaredMethod(
					"findLoadedClass", String.class);
			findLoadedClass.setAccessible(true);
			for (String binaryName : classNamesToLoad) {
				String str = binaryName;
				Logger.log("String = "+str);
				if (!binaryName.startsWith("java.")) {
					if (findLoadedClass.invoke(loader, binaryName) == null) {
						byte[] bytecode = getBytecode(binaryName);
						defineClass.invoke(loader, binaryName, bytecode,
								0, bytecode.length);
					} else if (scripts.containsKey(binaryName)) {
						throw new RuntimeException(
								"Class " + binaryName + " was already loaded, " +
										"it must not be redeclared");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"could not load classes into ClassLoader", e);
		} finally {
			rehideMethod(findLoadedClass);
			rehideMethod(defineClass);
		}
	}
	private static byte[] getBytecode(String binaryName) {
		byte[] bytecode = scripts.get(binaryName);
		if (bytecode == null) {
			System.out.println("bytes null");
		}
		return bytecode;
	}
	private static void rehideMethod(Method m) {
		if (m != null) {
			try {
				m.setAccessible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
