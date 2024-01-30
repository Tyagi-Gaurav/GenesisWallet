package com.gw.user.resource.domain;

import java.util.UUID;

public record UserProfile(UUID id, String authority, String token) {}
