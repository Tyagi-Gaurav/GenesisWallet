package com.gw.test.common.grpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public final class PortLookup {
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 49000;

    private static final int MAX_TRIES = 20;
    private static final Random random = new Random(System.nanoTime());

    public static int getRandomPort() {
        int tries = 0;
        while (tries < MAX_TRIES) {
            int nextPort = generateNextPortNumber();
            try {
                new ServerSocket(nextPort);
                return nextPort;
            } catch (IOException e) {
                //Already occupied
            } finally {
                ++tries;
            }
        }
        throw new IllegalStateException("No ports available");
    }

    private static int generateNextPortNumber() {
        return random.nextInt(MIN_PORT, MAX_PORT);
    }
}
