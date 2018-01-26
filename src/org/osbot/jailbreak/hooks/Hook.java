package org.osbot.jailbreak.hooks;

import org.json.simple.JSONObject;

public class Hook {

	private final String className, target, returnType;
	private final int parameterCount;

	private Hook(Builder builder) {
		this.className = builder.className;
		this.target = builder.target;
		this.parameterCount = builder.parameterCount;
		this.returnType = builder.returnType;
	}

	public String returnType() {
		return returnType;
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

	public static Hook wrap(JSONObject object) {
		Builder builder = new Builder((String) object.get("class"));
		builder.target((String) object.getOrDefault("target", null));
		builder.parameterCount(((Long) object.getOrDefault("parameter count", 0)).intValue());
		builder.returnType((String) object.getOrDefault("return type", null));
		return new Hook(builder);
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
