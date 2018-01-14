package org.osbot.jailbreak.ui.logger;

/**
 * Created by bautistabaiocchi-lora on 7/16/17.
 */
public enum LogType {

    CLIENT("Client"), SCRIPT("Script"), DEBUG("Debug"), NA("N/A");

    private String type;

    LogType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}