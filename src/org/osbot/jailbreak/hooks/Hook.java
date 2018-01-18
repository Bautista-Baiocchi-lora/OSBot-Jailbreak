package org.osbot.jailbreak.hooks;

public class Hook {

	private final String className, target, returType;
	private final int parameterCount;

	private Hook(Builder builder) {
		this.className = builder.className;
		this.target = builder.target;
		this.parameterCount = builder.parameterCount;
		this.returType = builder.returnType;
	}

	public String getReturType() {
		return returType;
	}

	public String getClassName() {
		return className;
	}

	public String getTarget() {
		return target;
	}

	public int getParameterCount() {
		return parameterCount;
	}

	public static class Builder {

		private final String className;
		private String target, returnType;
		private int parameterCount;

		public Builder(String className) {
			this.className = className;
		}

		public Builder returnType(String returnType) {
			this.returnType = returnType;
			return this;
		}

		public Builder target(String target) {
			this.target = target;
			return this;
		}

		public Builder parameterCount(int parameterCount) {
			this.parameterCount = parameterCount;
			return this;
		}

		public Hook build() {
			return new Hook(this);
		}

	}
}
