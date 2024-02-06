package com.gw.user.resource;

import com.gw.common.util.TokenManager;
import com.gw.user.cache.CacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserLogoutResourceTest {

    @Mock
    private TokenManager tokenManager;
    @Mock
    private CacheManager cacheManager;
    private UserLogoutResource userLogoutResource;

    @BeforeEach
    void setUp() {
        userLogoutResource = new UserLogoutResource(cacheManager, tokenManager);
    }

    @Test
    void logout() {
        String token = "dummyToken";
        String tokenHeader = "Bearer " + token;
        TokenManager.Token mockToken = mock(TokenManager.Token.class);

        TokenManager.Token parse = tokenManager.parse(token);
        when(parse).thenReturn(mockToken);
        when(mockToken.getUserId()).thenReturn("userId");

        when(cacheManager.invalidate(token, "userId")).thenReturn("");

        StepVerifier.create(userLogoutResource.logout(tokenHeader))
                .verifyComplete();
    }
}