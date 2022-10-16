package com.gw.common.util;

import java.io.IOException;

public interface DataEncoder {
    String encode(String originalString) throws IOException;
}
