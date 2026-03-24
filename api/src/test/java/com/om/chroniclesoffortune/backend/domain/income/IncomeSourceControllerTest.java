package com.om.chroniclesoffortune.backend.domain.income;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om.chroniclesoffortune.backend.domain.auth.JwtService;
import com.om.chroniclesoffortune.backend.domain.kingdom.Kingdom;
import com.om.chroniclesoffortune.backend.domain.kingdom.KingdomRepository;
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

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("IncomeSource endpoints")
class IncomeSourceControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private KingdomRepository kingdomRepository;
    @Autowired private IncomeSourceRepository incomeSourceRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String playerToken;
    private String otherToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        incomeSourceRepository.deleteAll();
        kingdomRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .name("Test Player")
                .username("test_player")
                .email("player@test.com")
                .password(passwordEncoder.encode("Senha@123"))
                .role(Role.PLAYER)
                .build());

        User otherUser = userRepository.save(User.builder()
                .name("Other Player")
                .username("other_player")
                .email("other@test.com")
                .password(passwordEncoder.encode("Senha@123"))
                .role(Role.PLAYER)
                .build());

        playerToken = jwtService.generateToken(testUser);
        otherToken = jwtService.generateToken(otherUser);

        kingdomRepository.saveAndFlush(Kingdom.builder()
                .user(testUser)
                .name("Test Kingdom")
                .build());

        kingdomRepository.saveAndFlush(Kingdom.builder()
                .user(otherUser)
                .name("Other Kingdom")
                .build());
    }

    private Map<String, Object> buildCreateRequest() {
        return Map.of(
                "name", "Salário Empresa",
                "type", "SALARY",
                "amount", 5000.00
        );
    }

    private String createIncomeSource() throws Exception {
        var result = mockMvc.perform(post("/income-sources")
                        .header("Authorization", "Bearer " + playerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildCreateRequest())))
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    // =========================================================================
    // POST /income-sources
    // =========================================================================

    @Nested
    @DisplayName("POST /income-sources")
    class CreateIncomeSource {

        @Test
        @DisplayName("Deve criar fonte de renda com sucesso")
        void createsSuccessfully() throws Exception {
            mockMvc.perform(post("/income-sources")
                            .header("Authorization", "Bearer " + playerToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildCreateRequest())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name").value("Salário Empresa"))
                    .andExpect(jsonPath("$.type").value("SALARY"))
                    .andExpect(jsonPath("$.amount").value(5000.00))
                    .andExpect(jsonPath("$.active").value(true))
                    .andExpect(jsonPath("$.createdAt").isNotEmpty());
        }

        @Test
        @DisplayName("Deve rejeitar name inválido (menos de 3 caracteres)")
        void rejectsShortName() throws Exception {
            var body = Map.of("name", "AB", "type", "SALARY", "amount", 5000.00);

            mockMvc.perform(post("/income-sources")
                            .header("Authorization", "Bearer " + playerToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("Deve rejeitar amount inválido (menor que 0.01)")
        void rejectsInvalidAmount() throws Exception {
            var body = Map.of("name", "Salário", "type", "SALARY", "amount", 0.00);

            mockMvc.perform(post("/income-sources")
                            .header("Authorization", "Bearer " + playerToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(post("/income-sources")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildCreateRequest())))
                    .andExpect(status().isForbidden());
        }
    }

    // =========================================================================
    // GET /income-sources
    // =========================================================================

    @Nested
    @DisplayName("GET /income-sources")
    class FindAll {

        @Test
        @DisplayName("Deve retornar lista vazia para usuário sem fontes de renda")
        void returnsEmptyListForNewUser() throws Exception {
            mockMvc.perform(get("/income-sources")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Deve retornar lista com itens criados")
        void returnsCreatedItems() throws Exception {
            createIncomeSource();

            mockMvc.perform(get("/income-sources")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].name").value("Salário Empresa"));
        }

        @Test
        @DisplayName("Não deve retornar fontes de renda de outro usuário")
        void doesNotReturnOtherUsersItems() throws Exception {
            createIncomeSource();

            mockMvc.perform(get("/income-sources")
                            .header("Authorization", "Bearer " + otherToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    // =========================================================================
    // GET /income-sources/{id}
    // =========================================================================

    @Nested
    @DisplayName("GET /income-sources/{id}")
    class FindById {

        @Test
        @DisplayName("Deve retornar fonte de renda correta")
        void returnsCorrectItem() throws Exception {
            String id = createIncomeSource();

            mockMvc.perform(get("/income-sources/{id}", id)
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.name").value("Salário Empresa"))
                    .andExpect(jsonPath("$.type").value("SALARY"));
        }

        @Test
        @DisplayName("Deve retornar 404 para id inexistente")
        void returns404ForNonExistentId() throws Exception {
            mockMvc.perform(get("/income-sources/{id}", UUID.randomUUID())
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("INCOME_SOURCE_NOT_FOUND"));
        }

        @Test
        @DisplayName("Deve retornar 404 para id de outro usuário")
        void returns404ForOtherUsersId() throws Exception {
            String id = createIncomeSource();

            mockMvc.perform(get("/income-sources/{id}", id)
                            .header("Authorization", "Bearer " + otherToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("INCOME_SOURCE_NOT_FOUND"));
        }
    }

    // =========================================================================
    // PUT /income-sources/{id}
    // =========================================================================

    @Nested
    @DisplayName("PUT /income-sources/{id}")
    class Update {

        @Test
        @DisplayName("Deve atualizar nome e amount com sucesso")
        void updatesSuccessfully() throws Exception {
            String id = createIncomeSource();
            var updateBody = Map.of("name", "Salário Novo", "amount", new BigDecimal("6000.00"));

            mockMvc.perform(put("/income-sources/{id}", id)
                            .header("Authorization", "Bearer " + playerToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateBody)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Salário Novo"))
                    .andExpect(jsonPath("$.amount").value(6000.00))
                    .andExpect(jsonPath("$.type").value("SALARY"));
        }

        @Test
        @DisplayName("Deve atualizar active para false")
        void deactivatesIncomeSource() throws Exception {
            String id = createIncomeSource();
            var updateBody = Map.of("active", false);

            mockMvc.perform(put("/income-sources/{id}", id)
                            .header("Authorization", "Bearer " + playerToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateBody)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.active").value(false));
        }

        @Test
        @DisplayName("Deve retornar 404 para id de outro usuário")
        void returns404ForOtherUsersId() throws Exception {
            String id = createIncomeSource();
            var updateBody = Map.of("name", "Tentativa");

            mockMvc.perform(put("/income-sources/{id}", id)
                            .header("Authorization", "Bearer " + otherToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateBody)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("INCOME_SOURCE_NOT_FOUND"));
        }
    }

    // =========================================================================
    // DELETE /income-sources/{id}
    // =========================================================================

    @Nested
    @DisplayName("DELETE /income-sources/{id}")
    class Delete {

        @Test
        @DisplayName("Deve remover fonte de renda e retornar 204")
        void deletesAndReturns204() throws Exception {
            String id = createIncomeSource();

            mockMvc.perform(delete("/income-sources/{id}", id)
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/income-sources/{id}", id)
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Deve retornar 404 para id de outro usuário")
        void returns404ForOtherUsersId() throws Exception {
            String id = createIncomeSource();

            mockMvc.perform(delete("/income-sources/{id}", id)
                            .header("Authorization", "Bearer " + otherToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("INCOME_SOURCE_NOT_FOUND"));
        }
    }
}
