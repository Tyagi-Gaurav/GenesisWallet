package com.gw.ui.resource;

import com.gw.ui.service.UserService;
import com.gw.ui.service.domain.ExternalUserConnectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
public class ExternalUserConnectController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/connect/google", produces = "application/json")
    public Mono<Map<String, Object>> user(@AuthenticationPrincipal OAuth2User principal,
                                          @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient oAuth2AuthorizedClient) {

        String email = principal.getAttribute("email");
        String locale = principal.getAttribute("locale");
        String picture = principal.getAttribute("picture");
        String givenName = principal.getAttribute("given_name");
        String familyName = principal.getAttribute("family_name");

        return userService.connectUser(new ExternalUserConnectRequest(
                oAuth2AuthorizedClient.getAccessToken().getTokenValue(),
                oAuth2AuthorizedClient.getAccessToken().getExpiresAt(),
                oAuth2AuthorizedClient.getAccessToken().getTokenType().getValue(),
                email,
                locale,
                picture,
                givenName,
                familyName
        )).thenReturn(Collections.singletonMap("name", givenName + " " + familyName));
    }
}
