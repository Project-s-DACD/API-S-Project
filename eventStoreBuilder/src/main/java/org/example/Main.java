package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length < 4) {
            log.warn("Usage: <brokerUrl> <clientId> <baseDir> <topic1> [<topic2> ...]");
            return;
        }
        String brokerUrl   = args[0];
        String clientId    = args[1];
        String baseDir     = args[2];
        String[] topics    = java.util.Arrays.copyOfRange(args, 3, args.length);

        Controller controller = new Controller();
        controller.start(brokerUrl, clientId, baseDir, topics);
    }
}
