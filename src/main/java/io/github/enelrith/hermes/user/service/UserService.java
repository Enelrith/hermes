package io.github.enelrith.hermes.user.service;

import io.github.enelrith.hermes.common.enums.RoleName;
import io.github.enelrith.hermes.user.dto.RegisterUserRequest;
import io.github.enelrith.hermes.user.entity.Customer;
import io.github.enelrith.hermes.user.exception.RoleNotFoundException;
import io.github.enelrith.hermes.user.repository.CustomerRepository;
import io.github.enelrith.hermes.user.exception.EmailAlreadyInUseException;
import io.github.enelrith.hermes.user.exception.PasswordsDoNotMatchException;
import io.github.enelrith.hermes.user.mapper.UserMapper;
import io.github.enelrith.hermes.user.repository.RoleRepository;
import io.github.enelrith.hermes.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void registerUser(RegisterUserRequest request) {
        if (!request.password().matches(request.rePassword())) throw new PasswordsDoNotMatchException();
        if (userRepository.existsByEmail(request.email())) throw new EmailAlreadyInUseException();

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        var userRole = roleRepository.findByName(RoleName.USER).orElseThrow(RoleNotFoundException::new);
        user.addRoleToUser(userRole);

        userRepository.save(user);

        var customer = new Customer();
        customer.setUser(user);

        customerRepository.save(customer);
    }
}
