package com.om.chroniclesoffortune.backend.domain.auth;

import com.om.chroniclesoffortune.backend.domain.auth.dto.AuthResponse;
import com.om.chroniclesoffortune.backend.domain.auth.dto.LoginRequest;
import com.om.chroniclesoffortune.backend.domain.auth.dto.RegisterRequest;
import com.om.chroniclesoffortune.backend.domain.user.Role;
import com.om.chroniclesoffortune.backend.domain.user.User;
import com.om.chroniclesoffortune.backend.domain.user.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@NullMarked
@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return userRepository.findByEmailOrUsername(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + identifier));
    }

    public AuthResponse register(RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        var user = User.builder()
                .name(request.name())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.PLAYER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.identifier(), request.password())
        );

        User user = userRepository.findByEmailOrUsername(request.identifier(), request.identifier())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.identifier()));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}