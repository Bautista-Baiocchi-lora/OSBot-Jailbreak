package org.osbot.jailbreak.hooks;

import java.util.HashMap;

public class HookManager {

	private final HashMap<String, Hook> hooks;

	public HookManager() {
		this.hooks = new HashMap<>();
		loadHooks();
	}

	private final void loadHooks() {
		//populate hooks map with online hooks
	}

	public Hook get(Key key) {
		return hooks.get(key.getName());
	}

	public static enum Key {
		BOT_INSTANCE("Bot instance");

		private final String name;

		Key(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
