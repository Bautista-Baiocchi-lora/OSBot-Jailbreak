package org.osbot.jailbreak.classloader;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.logger.Logger;

import javax.swing.text.Utilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan on 1/14/2018.
 */
public class EasyClassLoader extends ClassLoader {
    private Map<String, Class<?>> classMap = new HashMap<>();
    private Map<String, byte[]> classCache;

    public EasyClassLoader(Map<String, byte[]> map) {
        this.classCache = map;
    }

    public final void defineClasses() {
        try {
            Logger.log("Bout to define: " + classCache.size());
            for (Map.Entry<String, byte[]> entry : classCache.entrySet()) {
                if(entry.getValue() != null) {
                    Logger.log("Define: " + entry.getKey() + " || Length: " + entry.getValue().length);
                    String keyName = entry.getKey().replace('/', '.');
                    classMap.put(keyName, defineClass(keyName, entry.getValue()));
                }
            }

        } catch (Exception e) {
            Logger.log(e.getLocalizedMessage());
        }
    }
    private final Class<?> defineClass(String name, byte[] bytes) {
        if (super.findLoadedClass(name) != null) {
            return findLoadedClass(name);
        }
        return defineClass(name.replace('/', '.'), bytes, 0, bytes.length,
                getDomain());
    }
    private final ProtectionDomain getDomain() {
        CodeSource code = null;
        try {
            code = new CodeSource(new URL("http://127.0.0.1"), (Certificate[]) null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new ProtectionDomain(code, getPermissions());
    }

    private final Permissions getPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }

}

