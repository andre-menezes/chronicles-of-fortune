package com.om.chroniclesoffortune.backend.domain.behaviorlog;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("BehaviorLog admin endpoints")
class BehaviorLogAdminControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private KingdomRepository kingdomRepository;
    @Autowired private BehaviorLogRepository behaviorLogRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    private String playerToken;
    private String adminToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        behaviorLogRepository.deleteAll();
        kingdomRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
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

        playerToken = jwtService.generateToken(testUser);
        adminToken = jwtService.generateToken(admin);

        kingdomRepository.saveAndFlush(Kingdom.builder()
                .user(testUser)
                .name("Test Kingdom")
                .build());
    }

    private void saveLogs() {
        behaviorLogRepository.save(BehaviorLog.builder()
                .user(testUser)
                .action(UserAction.INCOME_SOURCE_CREATED)
                .metadata("{\"type\":\"SALARY\"}")
                .build());

        behaviorLogRepository.save(BehaviorLog.builder()
                .user(testUser)
                .action(UserAction.TIPS_VIEWED)
                .metadata("{\"context\":\"DASHBOARD\"}")
                .build());
    }

    // =========================================================================
    // GET /admin/behavior-logs
    // =========================================================================

    @Nested
    @DisplayName("GET /admin/behavior-logs")
    class FindAll {

        @Test
        @DisplayName("Deve listar todos os logs para ADMIN")
        void listsAllLogs() throws Exception {
            saveLogs();

            mockMvc.perform(get("/admin/behavior-logs")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        @DisplayName("Deve filtrar por userId")
        void filtersByUserId() throws Exception {
            saveLogs();

            mockMvc.perform(get("/admin/behavior-logs")
                            .param("userId", testUser.getId().toString())
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].userId").value(testUser.getId().toString()));
        }

        @Test
        @DisplayName("Deve filtrar por action")
        void filtersByAction() throws Exception {
            saveLogs();

            mockMvc.perform(get("/admin/behavior-logs")
                            .param("action", "INCOME_SOURCE_CREATED")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].action").value("INCOME_SOURCE_CREATED"))
                    .andExpect(jsonPath("$[0].metadata").value("{\"type\":\"SALARY\"}"));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há logs")
        void returnsEmptyList() throws Exception {
            mockMvc.perform(get("/admin/behavior-logs")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Deve retornar 403 para role PLAYER")
        void returns403ForPlayer() throws Exception {
            mockMvc.perform(get("/admin/behavior-logs")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(get("/admin/behavior-logs"))
                    .andExpect(status().isForbidden());
        }
    }
}
