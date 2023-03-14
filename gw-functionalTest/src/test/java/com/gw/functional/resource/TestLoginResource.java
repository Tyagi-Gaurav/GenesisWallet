package com.gw.functional.resource;

import com.gw.functional.config.ApiGatewayConfig;
import com.gw.functional.domain.TestLoginRequestDTO;
import com.gw.functional.util.ResponseHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TestLoginResource extends AbstractResource {

    @Autowired
    private ApiGatewayConfig apiGatewayConfig;

    @Autowired
    private ResponseHolder responseHolder;

    public void doLogin(TestLoginRequestDTO testLoginRequestDTO) {
        String fullUrl = getUrl(apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(),
                "/user/login", apiGatewayConfig.port());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/vnd.login.v1+json");
        HttpEntity<TestLoginRequestDTO> request = new HttpEntity<>(testLoginRequestDTO, headers);
        responseHolder.setResponse(this.post(fullUrl, request, String.class));

        if (responseHolder.getResponseCode() == 200) {
            TestLoginResponseDTO testLoginResponseDTO = responseHolder.readResponse(TestLoginResponseDTO.class);
            responseHolder.storeUserToken(testLoginResponseDTO.token());
            responseHolder.storeUserId(testLoginResponseDTO.id());
        }
    }

}