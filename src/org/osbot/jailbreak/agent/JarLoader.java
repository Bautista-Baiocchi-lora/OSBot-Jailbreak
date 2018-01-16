package org.osbot.jailbreak.agent;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by Ethan on 1/16/2018.
 */

public class JarLoader extends ClassLoader {
    private Map<String, byte[]> entryMap;
    public Map<String, Class<?>> classCache;
    private URL url;

    public JarLoader(URL url) {
        this.entryMap = new HashMap<>();
        this.classCache = new HashMap<>();
        this.url = url;
        loadEntries();
    }

    public Map<String, byte[]> entries(){
        return entryMap;
    }
    public void loadEntries() {
        try {
            JarInputStream jis = new JarInputStream(url.openStream());
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int read;
                while ((read = jis.read(buf, 0, 1024)) > 0) {
                    bos.write(buf, 0, read);
                }
                entryMap.put(entry.getName().replaceAll("/", ".").replace(".class", ""), bos.toByteArray());
                bos.close();
            }
            jis.close();
        } catch (Exception ea) {
            ea.printStackTrace();
        }
    }
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return findClass(name);
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return getSystemClassLoader().loadClass(name);
        } catch (Exception ignored) {

        }
        String key = name.replace('.', '/');
        if (classCache.containsKey(key)) {
            return classCache.get(key);
        }
        if (entryMap.containsKey(name)) {
            byte[] value = entryMap.get(name);
            ClassReader cr = new ClassReader(value);
            ClassNode classNode = new ClassNode();
            cr.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES) {
                @Override
                protected String getCommonSuperClass(String one, String two) {
                    return "java/lang/Object";
                }
            };
            classNode.accept(cw);
            value = cw.toByteArray();
            Class<?> c = defineClass(name, value, 0, value.length);
            entryMap.remove(key);
            classCache.put(key, c);
            return c;
        }
        return getSystemClassLoader().loadClass(name);
    }

}
