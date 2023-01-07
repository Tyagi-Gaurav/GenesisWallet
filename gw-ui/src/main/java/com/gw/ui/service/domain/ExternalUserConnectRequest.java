package com.gw.ui.service.domain;

import java.time.Instant;

public record ExternalUserConnectRequest(String accessToken,
                                         Instant expiryTime,
                                         String tokenType,
                                         String userName,
                                         String locale,
                                         String pictureUrl,
                                         String firstName,
                                         String lastName) {
}
