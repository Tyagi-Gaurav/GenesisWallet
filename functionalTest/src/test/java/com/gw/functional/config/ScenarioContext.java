package com.gw.functional.config;

import com.gw.functional.domain.TestAccountCreateRequestDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ScenarioContext {

    private TestAccountCreateRequestDTO userCredentialsRequest;
    private String regularUserId;
    private String lastUserName;

    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public void storeCredentialsRequest(TestAccountCreateRequestDTO testAccountCreateRequestDTO) {
        this.userCredentialsRequest = testAccountCreateRequestDTO;
    }

    public TestAccountCreateRequestDTO getUserCredentialsRequest() {
        return userCredentialsRequest;
    }

    public void setUserIdForRegularUser(String userId) {
        this.regularUserId = userId;
    }

    public TestAccountCreateRequestDTO getCredentials(String role) {
        return this.userCredentialsRequest;
    }
}