package io.github.enelrith.hermes.user.service;

import io.github.enelrith.hermes.common.enums.RoleName;
import io.github.enelrith.hermes.user.dto.RegisterUserRequest;
import io.github.enelrith.hermes.user.entity.Role;
import io.github.enelrith.hermes.user.entity.User;
import io.github.enelrith.hermes.user.exception.EmailAlreadyInUseException;
import io.github.enelrith.hermes.user.exception.PasswordsDoNotMatchException;
import io.github.enelrith.hermes.user.exception.RoleNotFoundException;
import io.github.enelrith.hermes.user.mapper.UserMapper;
import io.github.enelrith.hermes.user.repository.CustomerRepository;
import io.github.enelrith.hermes.user.repository.RoleRepository;
import io.github.enelrith.hermes.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private CustomerRepository customerRepository;
    @Mock private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should register user successfully when request is valid")
    void registerUser_Success() {
        var request = new RegisterUserRequest("test@email.com", "pass123", "pass123");
        var user = new User();
        Set<User> users = new HashSet<>();
        users.add(user);
        var role = new Role((byte) 1, RoleName.USER, users);

        given(userRepository.existsByEmail(request.email())).willReturn(false);
        given(userMapper.toEntity(request)).willReturn(user);
        given(passwordEncoder.encode(request.password())).willReturn("encodedPass");
        given(roleRepository.findByName(RoleName.USER)).willReturn(Optional.of(role));

        userService.registerUser(request);

        verify(userRepository).save(user);
        verify(customerRepository).save(any());
        verify(passwordEncoder).encode("pass123");
    }

    @Test
    @DisplayName("Should throw PasswordsDoNotMatchException when passwords differ")
    void registerUser_PasswordsMismatch() {
        var request = new RegisterUserRequest("test@email.com", "pass123", "wrongPass");

        assertThrows(PasswordsDoNotMatchException.class, () -> userService.registerUser(request));

        verifyNoInteractions(userRepository, customerRepository);
    }

    @Test
    @DisplayName("Should throw EmailAlreadyInUseException when email exists")
    void registerUser_EmailExists() {
        var request = new RegisterUserRequest("exists@email.com", "pass123", "pass123");
        given(userRepository.existsByEmail(request.email())).willReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> userService.registerUser(request));
    }

    @Test
    @DisplayName("Should throw RoleNotFoundException when USER role is missing in DB")
    void registerUser_RoleNotFound() {
        var request = new RegisterUserRequest("test@email.com", "pass123", "pass123");
        given(userRepository.existsByEmail(request.email())).willReturn(false);
        given(userMapper.toEntity(request)).willReturn(new User());
        given(roleRepository.findByName(RoleName.USER)).willReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.registerUser(request));
    }
}
