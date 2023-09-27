package com.gw.common.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Gender {
    FEMALE,
    MALE,
    UNSPECIFIED;

    private static final Logger LOG = LogManager.getLogger("APP");

    public static Gender from(String value) {
        try {
            return Gender.valueOf(value);
        } catch(Exception e) {
            LOG.warn("Invalid value found while converting to Gender enum: {}", value);
            return null;
        }
    }
}
