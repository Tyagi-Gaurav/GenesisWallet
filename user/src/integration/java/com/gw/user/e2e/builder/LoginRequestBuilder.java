package com.gw.user.e2e.builder;


import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.resource.domain.UserCreateRequestDTO;

import java.util.UUID;

public class LoginRequestBuilder {
    public static LoginRequestDTO loginRequestUsing(UserCreateRequestDTO userCreateRequestDTO) {
        return new LoginRequestDTO(userCreateRequestDTO.userName(), userCreateRequestDTO.password());
    }

    public static LoginRequestDTO invalidPasswordLoginRequestUsing(UserCreateRequestDTO  userCreateRequestDTO) {
        return new LoginRequestDTO(userCreateRequestDTO.userName(), UUID.randomUUID().toString());
    }

    public static LoginRequestDTO invalidUserLoginRequestUsing(UserCreateRequestDTO  userCreateRequestDTO) {
        return new LoginRequestDTO(UUID.randomUUID().toString(), userCreateRequestDTO.password());
    }
}
