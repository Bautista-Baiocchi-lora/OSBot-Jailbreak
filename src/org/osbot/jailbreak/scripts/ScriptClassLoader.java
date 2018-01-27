package org.osbot.jailbreak.scripts;


import org.osbot.jailbreak.hooks.HookManager;
import org.osbot.jailbreak.ui.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ethan on 1/17/2018.
 */
public class ScriptClassLoader extends ClassLoader {
	private final HashMap<String, byte[]> scriptByteMap;
	private final HashMap<String, Class<?>> scriptClassMap;
	public final AtomicReference<Class<?>> classAtomicReference;
	private final HashMap<String, byte[]> resourceMap;

	@Override
	public InputStream getResourceAsStream(String resource) {
		if (!this.resourceMap.containsKey(resource)) {
			return super.getResourceAsStream(resource);
		}
		byte[] resources = this.resourceMap.get(resource);
		return new ByteArrayInputStream(resources);
	}

	public ScriptClassLoader(HashMap<String, byte[]> scriptByteMap, HashMap<String, byte[]> resourceMap) {
		this.scriptClassMap = new HashMap<>();
		this.scriptByteMap = scriptByteMap;
		this.classAtomicReference = new AtomicReference<>();
		this.resourceMap = resourceMap;
		for (Map.Entry<String, byte[]> entry : scriptByteMap.entrySet()) {
			if (entry.getKey().endsWith(".class")) {
				String className = entry.getKey().replace('/', '.').replaceAll(".class", "");
				try {
					final Class<?> loadClass;
					if ((loadClass = this.loadClass(className)) == null || loadClass.getAnnotation(scriptManifestClass()) == null) {
						continue;
					}
					Logger.log(loadClass.getName());
					classAtomicReference.set(loadClass);
				} catch (Throwable t) {
					Logger.logException("Error in script classloader");
				}
			}
		}
	}

	public Class scriptManifestClass() {
		try {
			return ClassLoader.getSystemClassLoader().loadClass(HookManager.getHook(HookManager.Key.SCRIPT_MANIFEST).getClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<?> loadClass(final String className) throws ClassNotFoundException {
		if (this.scriptClassMap.containsKey(className)) {
			return this.scriptClassMap.get(className);
		}
		if (!this.scriptByteMap.containsKey(new StringBuilder().insert(0, className.replace('.', '/')).append(".class").toString())) {
			return super.loadClass(className);
		}
		final byte[] array = this.scriptByteMap.get(new StringBuilder().insert(0, className.replace('.', '/')).append(".class").toString());
		final int n = 0;
		final byte[] array2 = array;
		final Class<?> defineClass = super.defineClass(className, array2, n, array2.length);
		final Map<String, Class<?>> classMap = this.scriptClassMap;
		final Class<?> clazz = defineClass;
		classMap.put(className, clazz);
		return clazz;
	}
}