package org.osbot.jailbreak.core;

public class PermissionManager extends SecurityManager {

	@Override
	public void checkPackageAccess(final String pkg) {
		if (pkg.equalsIgnoreCase("java.lang.reflect")) {
			System.exit(0);
		}
	}
}
