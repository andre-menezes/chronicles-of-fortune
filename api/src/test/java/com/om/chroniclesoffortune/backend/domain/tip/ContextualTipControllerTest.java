package com.om.chroniclesoffortune.backend.domain.tip;

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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ContextualTip endpoints")
class ContextualTipControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private ContextualTipRepository contextualTipRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String playerToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        contextualTipRepository.deleteAll();
        userRepository.deleteAll();

        User player = userRepository.save(User.builder()
                .name("Test Player")
                .username("test_player")
                .email("player@test.com")
                .password(passwordEncoder.encode("Senha@123"))
                .role(Role.PLAYER)
                .build());

        User admin = userRepository.save(User.builder()
                .name("Test Admin")
                .username("test_admin")
                .email("admin@test.com")
                .password(passwordEncoder.encode("Senha@123"))
                .role(Role.ADMIN)
                .build());

        playerToken = jwtService.generateToken(player);
        adminToken = jwtService.generateToken(admin);
    }

    private Map<String, Object> buildCreateRequest() {
        return Map.of(
                "text", "Reserva de emergência é a base de tudo.",
                "context", "DASHBOARD",
                "category", "EMERGENCY_RESERVE"
        );
    }

    private String createTip() throws Exception {
        var result = mockMvc.perform(post("/admin/contextual-tips")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildCreateRequest())))
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    // =========================================================================
    // POST /admin/contextual-tips
    // =========================================================================

    @Nested
    @DisplayName("POST /admin/contextual-tips")
    class AdminCreate {

        @Test
        @DisplayName("Deve criar dica com sucesso para role ADMIN")
        void createsSuccessfully() throws Exception {
            mockMvc.perform(post("/admin/contextual-tips")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildCreateRequest())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.text").value("Reserva de emergência é a base de tudo."))
                    .andExpect(jsonPath("$.context").value("DASHBOARD"))
                    .andExpect(jsonPath("$.category").value("EMERGENCY_RESERVE"))
                    .andExpect(jsonPath("$.active").value(true))
                    .andExpect(jsonPath("$.createdAt").isNotEmpty());
        }

        @Test
        @DisplayName("Deve retornar 403 para role PLAYER")
        void returns403ForPlayer() throws Exception {
            mockMvc.perform(post("/admin/contextual-tips")
                            .header("Authorization", "Bearer " + playerToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildCreateRequest())))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(post("/admin/contextual-tips")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildCreateRequest())))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Deve rejeitar text com menos de 10 caracteres")
        void rejectsShortText() throws Exception {
            var body = Map.of("text", "Curto", "context", "DASHBOARD", "category", "CASH_FLOW");

            mockMvc.perform(post("/admin/contextual-tips")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }
    }

    // =========================================================================
    // GET /admin/contextual-tips
    // =========================================================================

    @Nested
    @DisplayName("GET /admin/contextual-tips")
    class AdminFindAll {

        @Test
        @DisplayName("Deve listar todas as dicas para role ADMIN")
        void listsAllTips() throws Exception {
            createTip();

            mockMvc.perform(get("/admin/contextual-tips")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].context").value("DASHBOARD"));
        }

        @Test
        @DisplayName("Deve retornar 403 para role PLAYER")
        void returns403ForPlayer() throws Exception {
            mockMvc.perform(get("/admin/contextual-tips")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isForbidden());
        }
    }

    // =========================================================================
    // PUT /admin/contextual-tips/{id}
    // =========================================================================

    @Nested
    @DisplayName("PUT /admin/contextual-tips/{id}")
    class AdminUpdate {

        @Test
        @DisplayName("Deve atualizar dica com sucesso")
        void updatesSuccessfully() throws Exception {
            String id = createTip();
            var updateBody = Map.of("active", false);

            mockMvc.perform(put("/admin/contextual-tips/{id}", id)
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateBody)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.active").value(false));
        }

        @Test
        @DisplayName("Deve retornar 404 para id inexistente")
        void returns404ForNonExistentId() throws Exception {
            var updateBody = Map.of("active", false);

            mockMvc.perform(put("/admin/contextual-tips/{id}", UUID.randomUUID())
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateBody)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("CONTEXTUAL_TIP_NOT_FOUND"));
        }
    }

    // =========================================================================
    // DELETE /admin/contextual-tips/{id}
    // =========================================================================

    @Nested
    @DisplayName("DELETE /admin/contextual-tips/{id}")
    class AdminDelete {

        @Test
        @DisplayName("Deve remover dica e retornar 204")
        void deletesAndReturns204() throws Exception {
            String id = createTip();

            mockMvc.perform(delete("/admin/contextual-tips/{id}", id)
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/admin/contextual-tips")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Deve retornar 404 para id inexistente")
        void returns404ForNonExistentId() throws Exception {
            mockMvc.perform(delete("/admin/contextual-tips/{id}", UUID.randomUUID())
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("CONTEXTUAL_TIP_NOT_FOUND"));
        }
    }

    // =========================================================================
    // GET /contextual-tips?context=
    // =========================================================================

    @Nested
    @DisplayName("GET /contextual-tips")
    class PlayerFindByContext {

        @Test
        @DisplayName("Deve retornar dicas ativas do contexto solicitado")
        void returnsActiveTipsByContext() throws Exception {
            createTip();

            mockMvc.perform(get("/contextual-tips")
                            .param("context", "DASHBOARD")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].context").value("DASHBOARD"));
        }

        @Test
        @DisplayName("Deve retornar lista vazia para contexto sem dicas ativas")
        void returnsEmptyForContextWithNoActiveTips() throws Exception {
            mockMvc.perform(get("/contextual-tips")
                            .param("context", "DEBT")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Não deve retornar dicas inativas")
        void doesNotReturnInactiveTips() throws Exception {
            String id = createTip();
            var updateBody = Map.of("active", false);

            mockMvc.perform(put("/admin/contextual-tips/{id}", id)
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateBody)));

            mockMvc.perform(get("/contextual-tips")
                            .param("context", "DASHBOARD")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(get("/contextual-tips")
                            .param("context", "DASHBOARD"))
                    .andExpect(status().isForbidden());
        }
    }
}
