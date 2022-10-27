package com.gw.common.config;

import com.gw.common.util.DataEncoder;
import com.gw.common.util.DataEncoderImpl;
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
