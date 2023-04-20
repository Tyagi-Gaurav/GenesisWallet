package com.gw.test.common.grpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public final class PortLookup {
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 9999;
    private static final int MAX_TRIES = 20;
    private static final Random random = new Random(System.nanoTime());

    public static int getRandomPort() {
        int tries = 0;
        while (tries < MAX_TRIES) {
            int nextPort = generateNextPortNumber();
            try {
                if (isAvailable(nextPort)) {
                    return nextPort;
                }
            } finally {
                ++tries;
            }
        }
        throw new IllegalStateException("No ports available");
    }

    public static boolean isAvailable(int nextPort) {
        try {
            new ServerSocket(nextPort);
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    private static int generateNextPortNumber() {
        return random.nextInt(MIN_PORT, MAX_PORT);
    }
}
