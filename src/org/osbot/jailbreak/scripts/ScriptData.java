package org.osbot.jailbreak.scripts;


import org.osbot.jailbreak.data.Engine;
import org.osbot.jailbreak.ui.logger.Logger;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Ethan on 1/12/2018.
 */
public class ScriptData {

    public ScriptData() {
            mapIterate();
    }
    public TreeMap<String, Object> getMap() {
        return (TreeMap) Engine.getReflectionEngine().getFieldValue("org.osbot.LPT8", "IIiiiiiIIIii");
    }

    public void mapIterate() {
        for (Map.Entry<String, Object> entry : getMap().entrySet()) {
            String key = entry.getKey();
            String str1 = getStringOne(entry.getValue());
            String str2 = getStringTwo(entry.getValue());
            String str3 = getStringThree(entry.getValue());
            String str4 = getStringFour(entry.getValue());
            Logger.log("Key: "+key+ " : str1: "+str1+ " : str2: "+str2+ " : str3: "+str3+" : str4: "+str4);
        }
    }
    public String getStringOne(Object obj) {
        return (String) Engine.getReflectionEngine().getFieldValue("org.osbot.LPT3", "iIIIiiiiIIii", obj);
    }
    public String getStringTwo(Object obj) {
        return (String) Engine.getReflectionEngine().getFieldValue("org.osbot.LPT3", "IiiIIiiiiIIi", obj);
    }
    public String getStringThree(Object obj) {
        return (String) Engine.getReflectionEngine().getFieldValue("org.osbot.LPT3", "IiiIiiiIiIIi", obj);
    }
    public String getStringFour(Object obj) {
        return (String) Engine.getReflectionEngine().getFieldValue("org.osbot.LPT3", "IIiiiiiIIIii", obj);
    }
}
