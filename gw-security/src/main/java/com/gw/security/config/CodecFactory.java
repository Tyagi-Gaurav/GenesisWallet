package com.gw.security.config;

import com.gw.security.util.DataEncoder;
import com.gw.security.util.DataEncoderImpl;
import org.bouncycastle.util.encoders.HexEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodecFactory {
    @Bean
    public DataEncoder dataEncoder() {
        return new DataEncoderImpl(new HexEncoder());
    }
}
