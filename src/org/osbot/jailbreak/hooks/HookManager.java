package org.osbot.jailbreak.hooks;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.osbot.jailbreak.util.NetUtils;

import java.util.HashMap;

public class HookManager {

	private static HashMap<Key, Hook> hooks;

	public HookManager() {
		this.hooks = new HashMap<>();
		loadHooks();
	}

	private void loadHooks() {
		JSONArray hookArray = null;
		try {
			JSONParser parser = new JSONParser();
			hookArray = (JSONArray) parser.parse(NetUtils.getResponse("http://botupgrade.us/private/hooks/osbot_hooks.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (hookArray != null) {
			outter:
			for (Object arrayObject : hookArray) {
				JSONObject jsonObject = (JSONObject) arrayObject;
				Hook hook = Hook.wrap(jsonObject);
				String hookKey = (String) jsonObject.get("Key");
				for (Key key : Key.values()) {
					if (key.getName().equalsIgnoreCase(hookKey)) {
						hooks.put(key, hook);
						continue outter;
					}
				}
			}
		}
	}

	public static Hook getHook(Key key) {
		return hooks.get(key);
	}

	public enum Key {
		BOT_INSTANCE("Bot instance"), START_SCRIPT("Start script"), BOT_APP_INSTANCE("Bot app instance"),
		PREFERENCE_CLASS_INSTANCE("Preference class instance"), BOT_PREFERENCES("Bot preferences"),
		SDN_SCRIPT("SDN script"), SCRIPT_MAP("Script map"), SCRIPT_MANIFEST("Script manifest"), ACCOUNT_INSTACE("Account instance"),
		VIP("Vip"), DEV("Dev"), NAME("Name"), ACCOUNT_KEY("Account Key"), BOT_CLASS_INSTANCE("Bot class instance"),
		PREFERENCE_HOLDER_CLASS("Preference holder class");

		private final String name;

		Key(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
