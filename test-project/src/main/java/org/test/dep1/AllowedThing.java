package org.test.dep1;

import java.util.logging.Logger;

public class AllowedThing {

    private static final Logger log = Logger.getLogger("AllowedThing");

    public static void logHello() {
        log.info("Hello!");
    }
}
