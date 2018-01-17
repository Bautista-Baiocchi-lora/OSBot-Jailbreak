package org.osbot.jailbreak.scripts;

/**
 * Created by Ethan on 1/13/2018.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptManifest
{
    String name();

    String logo();

    String info();

    String author();

    double version();
}
