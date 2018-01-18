package org.osbot.jailbreak.hooks;

import java.util.HashMap;

public class HookManager {

	private static HashMap<Key, Hook> hooks;

	public HookManager() {
		this.hooks = new HashMap<>();
		loadHooks();
	}

	private final void loadHooks() {
		hooks.put(Key.BOT_INSTANCE, new Hook.Builder("org.osbot.BotApplication").target("iiIIiiiIiIii").parameterCount(0).returnType("public class org.osbot.rs07.Bot").build());
		hooks.put(Key.START_SCRIPT, new Hook.Builder("org.osbot.Gb").target("iiIIiiiIiIii").parameterCount(4).build());
		hooks.put(Key.BOT_APP_INSTANCE, new Hook.Builder("org.osbot.BotApplication").target("iiiiiiiiIiii").build());
		hooks.put(Key.PREFERENCE_CLASS_INSTANCE, new Hook.Builder("org.osbot.BotApplication").target("iiIIiiiIiIii").returnType("public class org.osbot.Kc").parameterCount(0).build());
		hooks.put(Key.BOT_PREFERENCES, new Hook.Builder("org.osbot.Kc").target("iiIIiiiIiIii").parameterCount(1).returnType("public class org.osbot.ad").build());
		hooks.put(Key.SDN_SCRIPT, new Hook.Builder("org.osbot.SA").build());
		hooks.put(Key.SCRIPT_MAP, new Hook.Builder("org.osbot.LPT8").target("iIIIiiiIiiII").build());
		hooks.put(Key.SCRIPT_MANIFEST, new Hook.Builder("org.osbot.rs07.script.ScriptManifest").build());
		hooks.put(Key.VIP, new Hook.Builder("org.osbot.BotApplication").target("IIiIiiiiIiiI").build());
	}

	public static Hook getHook(Key key) {
		return hooks.get(key);
	}

	public static enum Key {
		BOT_INSTANCE("Bot instance"), START_SCRIPT("Start script"), BOT_APP_INSTANCE("Bot app instance"),
		PREFERENCE_CLASS_INSTANCE("Preference class instance"), BOT_PREFERENCES("Bot preferences"),
		SDN_SCRIPT("SDN script"), SCRIPT_MAP("Script map"), SCRIPT_MANIFEST("Script manifest"), VIP("Vip");

		private final String name;

		Key(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
