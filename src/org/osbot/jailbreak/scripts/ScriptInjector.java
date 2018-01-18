package org.osbot.jailbreak.scripts;

import org.osbot.jailbreak.agent.Agent;
import org.osbot.jailbreak.hooks.Hook;
import org.osbot.jailbreak.hooks.HookManager;
import org.osbot.jailbreak.ui.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Created by Ethan on 1/17/2018.
 */
public class ScriptInjector {
	private TreeMap<String, Object> SDNClassMap = new TreeMap<>();
	private String scriptLink;
	private String scriptName;

	/**
	 * @Author Ethan & Bautista
	 * @Usage Downloads the ripped script and preps for run-time.
	 */
	public ScriptInjector(String creativeName, String link) {
		Logger.log("Preparing script...");
		this.scriptLink = link;
		this.scriptName = creativeName;
		Logger.log("Injecting script...");
		addToTreeMap();
		new ScriptExecutor(creativeName);
	}

	/**
	 * @Class ScriptLoader [Random]
	 * @Field [Random] - Generally the first TreeMap
	 * @Usage This TreeMap holds our SDN Script while it awaits being started.
	 */
	private void addToTreeMap() {
		SDNClassMap.put(scriptName, getSDNScript(scriptLink));
		Hook hook = HookManager.getHook(HookManager.Key.SCRIPT_MAP);
		Agent.getReflectionEngine().setFieldValue(hook.getClassName(), hook.getTarget(), SDNClassMap);
	}

	/**
	 * @return Creates a new script using the atomic reference inside the customclassloader
	 * @Usage This system downloads our SDN script.
	 * It will store the bytes for the classes & resources in hashmaps.
	 * We then create a new classloader containing those bytes. That class loader will search for the main class, then load it.
	 * It will also set a reference for the main class, which we will use to create a new script out of.
	 */
	private synchronized Object getSDNScript(String link) {
		try {
			byte[] bytes = getByteArray(link);
			final HashMap<String, byte[]> classMap = new HashMap<>();
			final HashMap<String, byte[]> resourceMap = new HashMap<>();
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
					classMap.put(nextEntry.getName(), byteArrayOutputStream.toByteArray());
				} else {
					resourceMap.put(nextEntry.getName(), byteArrayOutputStream.toByteArray());
				}
			}
			return newScript(new ScriptClassLoader(classMap).classAtomicReference.get());
		} catch (Exception e) {
			Logger.logException("Script injection error!");
		}
		return null;
	}

	/**
	 * @Usage returns the bytes for the scripts jar.
	 */
	private byte[] getByteArray(String link) {
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
			Logger.logException("Invalid data error!");
		}
		return null;
	}

	/**
	 * @Usage Creates the object of the Script. We will then pass that to TreeMap which holds our SDN script.
	 */
	private Object newScript(Class<?> clazz) {
		try {
			ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			return classLoader.loadClass(HookManager.getHook(HookManager.Key.SDN_SCRIPT).getClassName()).getDeclaredConstructor(Class.class, boolean.class).newInstance(clazz, false);
		} catch (Exception e) {
			Logger.logException("Script preparation error!");
		}
		return null;
	}

}
