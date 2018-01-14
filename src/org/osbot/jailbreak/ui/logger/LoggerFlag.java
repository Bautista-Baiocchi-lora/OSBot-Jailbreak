package org.osbot.jailbreak.ui.logger;

import java.awt.*;

/**
 * Created by bautistabaiocchi-lora on 7/23/17.
 */
public enum LoggerFlag {

    EXCEPTION("Exception", Color.RED), WARNING("Warning", Color.YELLOW);

    private final String flag;
    private final Color color;

    LoggerFlag(String flag, Color color) {
        this.flag = flag;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getFlag() {
        return flag;
    }
}
