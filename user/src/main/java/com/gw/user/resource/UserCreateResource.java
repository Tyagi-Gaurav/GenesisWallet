package com.gw.user.resource;

import com.gw.common.domain.User;
import com.gw.common.exception.ApplicationAuthenticationException;
import com.gw.security.util.DataEncoder;
import com.gw.common.util.TokenManager;
import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.resource.domain.LoginResponseDTO;
import com.gw.user.resource.domain.UserCreateRequestDTO;
import com.gw.user.service.UserService;
import com.gw.user.service.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@RestController
public class UserCreateResource {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private DataEncoder dataEncoder;
//    @Autowired
//    private VaultTemplate vaultTemplate;
//    private String encryptionKey;

//    @PostConstruct
//    public void setEncryptionKey() {
//        VaultKeyValueOperations userPasswordEncryption =
//                vaultTemplate.opsForKeyValue("UserPasswordEncryption", VaultKeyValueOperationsSupport.KeyValueBackend.KV_1);
//        VaultResponse vaultResponse = userPasswordEncryption.get("user_service/passwordEncryptionKey");
//        Map<String, Object> keyValueMap = Optional.ofNullable(vaultResponse)
//                .map(VaultResponseSupport::getData).orElseGet(Map::of);
//        encryptionKey = keyValueMap.get("encryptionKey").toString();
//    }

    @PostMapping(consumes = "application/vnd+user.create.v1+json",
            produces = "application/vnd+user.create.v1+json",
            path = "/user/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Void> createUser(@Valid @RequestBody UserCreateRequestDTO userCreateRequestDTO) throws IOException {
        return userService.addUser(new User(
                UUID.randomUUID(),
                userCreateRequestDTO.firstName(),
                userCreateRequestDTO.lastName(),
                userCreateRequestDTO.userName(),
                dataEncoder.encode(userCreateRequestDTO.password()),
                dataEncoder.encode(userCreateRequestDTO.password()),
                userCreateRequestDTO.dateOfBirth(),
                userCreateRequestDTO.gender(),
                userCreateRequestDTO.homeCountry(),
                Role.REGISTERED_USER.name()));
    }

    @PostMapping(consumes = "application/vnd.login.v1+json",
            produces = "application/vnd.login.v1+json",
            path = "/user/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws IOException {
        return userService.authenticateUser(loginRequestDTO.userName(), dataEncoder.encode(loginRequestDTO.password()))
                .map(value -> new LoginResponseDTO(tokenManager.generateToken(value, Duration.ofMinutes(10))))
                .switchIfEmpty(Mono.error(() -> new ApplicationAuthenticationException("No user found [UserName]: " +
                        loginRequestDTO.userName())));
    }
}
