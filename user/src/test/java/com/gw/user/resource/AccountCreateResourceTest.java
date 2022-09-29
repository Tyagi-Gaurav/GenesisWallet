package com.gw.user.resource;

import com.gw.user.resource.domain.AccountCreateRequestDTO;
import com.gw.user.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AccountCreateResourceTest {
    private AccountCreateResource accountCreateResource;

    @BeforeEach
    void setUp() {
        accountCreateResource = new AccountCreateResource();
    }

    @Test
    void shouldReturnSuccessWhenUserIsCreated() {
        AccountCreateRequestDTO accountCreateRequestDTO = TestUtils.testAccountCreateRequestDTO();

        StepVerifier.create(accountCreateResource.createAccount(accountCreateRequestDTO))
                .verifyComplete();
    }
}