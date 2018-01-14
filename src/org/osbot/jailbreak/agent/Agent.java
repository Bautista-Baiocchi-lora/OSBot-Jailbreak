package org.osbot.jailbreak.agent;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.MainFrame;
import org.osbot.jailbreak.ui.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Ethan on 1/14/2018.
 */

public class Agent implements ClassFileTransformer {


	public static void agentmain(String args, Instrumentation instrumentation) {
		new MainFrame(instrumentation);
		Logger.log("[Agent] Successfully loaded into the JVM");

		try {

			Logger.log("Downloading...");
			String link = "https://www.dropbox.com/s/cn3z8hziawpr8od/KhalCrafter.jar?dl=1";
			URL url = new URL(link);

			DataInputStream dataInputStream = new DataInputStream(url.openStream());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			int read;
			while ((read = dataInputStream.read()) != -1) {
				byteArrayOutputStream.write(read);
			}
			dataInputStream.close();
			Logger.log("Download finished.");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			JarInputStream jarInputStream = new JarInputStream(byteArrayInputStream);
			ZipEntry entry;
			while ((entry = jarInputStream.getNextEntry()) != null) {
				Logger.log("Found class: " + entry.getName());
			}
			jarInputStream.close();
		} catch (Exception e) {
			Logger.log(e.getLocalizedMessage());
		}
	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
	                        Class<?> redefinedClass, ProtectionDomain protDomain, byte[] classBytes) {
		Logger.log("Adding: " + className + " to cache..");
		return classBytes;
	}
}
