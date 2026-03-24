package com.om.chroniclesoffortune.backend.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om.chroniclesoffortune.backend.domain.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("PUT /users/me e DELETE /users/me")
class UserUpdateDeleteControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .name("Andre Menezes")
                .username("andre_menezes")
                .email("andre@email.com")
                .password(passwordEncoder.encode("Senha@123"))
                .role(Role.PLAYER)
                .build());

        token = jwtService.generateToken(testUser);
    }

    // =========================================================================
    // PUT /users/me
    // =========================================================================

    @Nested
    @DisplayName("PUT /users/me")
    class UpdateUser {

        @Test
        @DisplayName("Deve atualizar nome com sucesso")
        void updatesName() throws Exception {
            var body = Map.of("name", "Andre Atualizado");

            mockMvc.perform(put("/users/me")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Andre Atualizado"))
                    .andExpect(jsonPath("$.username").value("andre_menezes"));
        }

        @Test
        @DisplayName("Deve atualizar email com sucesso")
        void updatesEmail() throws Exception {
            var body = Map.of("email", "novo@email.com");

            mockMvc.perform(put("/users/me")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("novo@email.com"));
        }

        @Test
        @DisplayName("Deve atualizar senha com currentPassword correto")
        void updatesPassword() throws Exception {
            var body = Map.of(
                    "currentPassword", "Senha@123",
                    "newPassword", "NovaSenha@456"
            );

            mockMvc.perform(put("/users/me")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Deve retornar 400 com currentPassword incorreto")
        void rejectsWrongCurrentPassword() throws Exception {
            var body = Map.of(
                    "currentPassword", "SenhaErrada",
                    "newPassword", "NovaSenha@456"
            );

            mockMvc.perform(put("/users/me")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                    .andExpect(jsonPath("$.message").value("INVALID_CURRENT_PASSWORD"));
        }

        @Test
        @DisplayName("Deve retornar 400 com newPassword sem currentPassword")
        void rejectsNewPasswordWithoutCurrentPassword() throws Exception {
            var body = Map.of("newPassword", "NovaSenha@456");

            mockMvc.perform(put("/users/me")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
        }

        @Test
        @DisplayName("Deve retornar 400 com email inválido")
        void rejectsInvalidEmail() throws Exception {
            var body = Map.of("email", "nao-e-um-email");

            mockMvc.perform(put("/users/me")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(put("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Map.of("name", "Teste"))))
                    .andExpect(status().isForbidden());
        }
    }

    // =========================================================================
    // DELETE /users/me
    // =========================================================================

    @Nested
    @DisplayName("DELETE /users/me")
    class DeleteUser {

        @Test
        @DisplayName("Deve excluir conta e retornar 204")
        void deletesAccount() throws Exception {
            mockMvc.perform(delete("/users/me")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/users/me")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(delete("/users/me"))
                    .andExpect(status().isForbidden());
        }
    }
}
