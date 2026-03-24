package com.om.chroniclesoffortune.backend.domain.user;

import com.om.chroniclesoffortune.backend.domain.user.dto.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@NullMarked
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponse(
                user.getDisplayName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        ));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> update(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(user, request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user) {
        userService.delete(user);
        return ResponseEntity.noContent().build();
    }
}
