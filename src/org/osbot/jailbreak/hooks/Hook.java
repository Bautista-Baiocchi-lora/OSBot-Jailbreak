package org.osbot.jailbreak.hooks;

public class Hook {

	private final String className;
	private String target;

	public Hook(String className) {
		this.className = className;
	}

	public Hook(String className, String target) {
		this(className);
		this.target = target;
	}

	public String getClassName() {
		return className;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}
}
