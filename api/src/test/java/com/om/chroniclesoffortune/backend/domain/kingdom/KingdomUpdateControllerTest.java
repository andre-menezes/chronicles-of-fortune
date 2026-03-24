package com.om.chroniclesoffortune.backend.domain.kingdom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om.chroniclesoffortune.backend.domain.auth.JwtService;
import com.om.chroniclesoffortune.backend.domain.user.Role;
import com.om.chroniclesoffortune.backend.domain.user.User;
import com.om.chroniclesoffortune.backend.domain.user.UserRepository;
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
@DisplayName("PUT /kingdoms/me")
class KingdomUpdateControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private KingdomRepository kingdomRepository;
    @Autowired private KingdomStateRepository kingdomStateRepository;
    @Autowired private PlayerProgressRepository playerProgressRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        playerProgressRepository.deleteAll();
        kingdomStateRepository.deleteAll();
        kingdomRepository.deleteAll();
        userRepository.deleteAll();

        User testUser = userRepository.save(User.builder()
                .name("Test Player")
                .username("test_player")
                .email("player@test.com")
                .password(passwordEncoder.encode("Senha@123"))
                .role(Role.PLAYER)
                .build());

        token = jwtService.generateToken(testUser);

        mockMvc.perform(post("/kingdoms")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Reino Original"))));
    }

    // =========================================================================
    // PUT /kingdoms/me
    // =========================================================================

    @Nested
    @DisplayName("PUT /kingdoms/me")
    class UpdateKingdom {

        @Test
        @DisplayName("Deve atualizar nome do reino com sucesso")
        void updatesKingdomName() throws Exception {
            var body = Map.of("name", "Reino Renomeado");

            mockMvc.perform(put("/kingdoms/me")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.kingdom.name").value("Reino Renomeado"));
        }

        @Test
        @DisplayName("Deve retornar 400 com nome inválido (menos de 3 caracteres)")
        void rejectsShortName() throws Exception {
            var body = Map.of("name", "AB");

            mockMvc.perform(put("/kingdoms/me")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando usuário não tem reino")
        void returns404WhenNoKingdom() throws Exception {
            kingdomStateRepository.deleteAll();
            kingdomRepository.deleteAll();

            var body = Map.of("name", "Novo Nome");

            mockMvc.perform(put("/kingdoms/me")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("KINGDOM_NOT_FOUND"));
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(put("/kingdoms/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Map.of("name", "Teste"))))
                    .andExpect(status().isForbidden());
        }
    }
}
