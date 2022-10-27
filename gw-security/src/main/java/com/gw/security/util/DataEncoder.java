package com.gw.security.util;

import java.io.IOException;

public interface DataEncoder {
    String encode(String originalString) throws IOException;
}
