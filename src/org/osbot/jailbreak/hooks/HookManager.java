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

    public static Hook getHook(Key key) {
        return hooks.get(key);
    }

    private void loadHooks() {
        JSONArray hookArray = null;
        try {
            JSONParser parser = new JSONParser();
            hookArray = (JSONArray) parser.parse(NetUtils.getResponse("http://botupgrade.us/private/hooks/osbot_hooks162.txt"));
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

    public enum Key {
        BOT_INSTANCE("Bot instance"), BOT_APP_INSTANCE("Bot app instance"),
        SCRIPT_MANIFEST("Script manifest"),
        BOT_HOLDER_INSTANCE("Bot holder instance");

        private final String name;

        Key(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
