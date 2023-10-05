package com.gw.ui.resource;

import com.gw.ui.Application;
import com.gw.ui.service.UserService;
import com.gw.ui.service.domain.ExternalUserConnectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebFlux
@ActiveProfiles("ExternalUserConnectControllerTest")
class ExternalUserConnectControllerTest {
    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .build();
    }

    @Test
    @WithMockCustomUser
    void externalUserConnect() {
        when(userService.connectUser(any(ExternalUserConnectRequest.class)))
                .thenReturn(Mono.empty());

        webTestClient
                .mutateWith(mockOAuth2Login()
                        .authorities(new SimpleGrantedAuthority("SCOPE_message:read"),
                                new OAuth2UserAuthority(Map.of("email", "test_email"))))
                .get().uri("/connect/google")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }
}