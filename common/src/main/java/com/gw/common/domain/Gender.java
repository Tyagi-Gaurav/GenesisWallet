package com.gw.common.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Gender {
    FEMALE,
    MALE,
    UNSPECIFIED;

    private static final Logger LOG = LoggerFactory.getLogger(Gender.class);

    public static Gender from(String value) {
        try {
            return Gender.valueOf(value);
        } catch(Exception e) {
            LOG.warn("Invalid value found while converting to Gender enum: {}", value);
            return null;
        }
    }
}
