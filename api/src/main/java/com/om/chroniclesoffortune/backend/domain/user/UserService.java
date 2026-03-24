package com.om.chroniclesoffortune.backend.domain.user;

import com.om.chroniclesoffortune.backend.domain.user.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@NullMarked
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        if (request.name() != null) user.setName(request.name());

        if (request.email() != null) user.setEmail(request.email());

        if (request.newPassword() != null) {
            if (request.currentPassword() == null ||
                    !passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("INVALID_CURRENT_PASSWORD");
            }
            user.setPassword(passwordEncoder.encode(request.newPassword()));
        }

        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public void delete(User user) {
        userRepository.deleteById(user.getId());
    }

    // ─── private helpers ──────────────────────────────────────────────────────

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getDisplayName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
