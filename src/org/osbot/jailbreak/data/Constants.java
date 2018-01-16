package org.osbot.jailbreak.data;

import java.io.File;

public class Constants {

	public static final String LOCAL_JAR_PATH = System.getProperty("user.home") + File.separator + "Desktop";
	public static final String LOCAL_JAR_NAME = "Khal Crafter.jar";
	public static final String ABSOLUTE_JAR_PATH = LOCAL_JAR_PATH + File.separator + LOCAL_JAR_NAME;
	public static final String[] ignore = {"org.osbot", "jdk/internal", "jdk.internal", "java.util", "sun/reflect/", "java.",
			"sun.", "javax."};
	public static final String JAR_URL = "https://www.dropbox.com/s/ctuu80vhh7htxrr/eZulrah.jar?dl=1";
}
