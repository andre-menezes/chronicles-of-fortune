package com.om.chroniclesoffortune.backend.domain.narrative;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om.chroniclesoffortune.backend.domain.auth.JwtService;
import com.om.chroniclesoffortune.backend.domain.kingdom.*;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("NarrativeEvent endpoints")
class NarrativeEventControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private KingdomRepository kingdomRepository;
    @Autowired private KingdomStateRepository kingdomStateRepository;
    @Autowired private PlayerProgressRepository playerProgressRepository;
    @Autowired private NarrativeEventRepository narrativeEventRepository;
    @Autowired private ResolvedEventRepository resolvedEventRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String playerToken;
    private String adminToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        resolvedEventRepository.deleteAll();
        playerProgressRepository.deleteAll();
        kingdomStateRepository.deleteAll();
        kingdomRepository.deleteAll();
        narrativeEventRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .name("Test Player")
                .username("test_player")
                .email("player@test.com")
                .password(passwordEncoder.encode("Senha@123"))
                .role(Role.PLAYER)
                .build());

        User adminUser = userRepository.save(User.builder()
                .name("Test Admin")
                .username("test_admin")
                .email("admin@test.com")
                .password(passwordEncoder.encode("Senha@123"))
                .role(Role.ADMIN)
                .build());

        playerToken = jwtService.generateToken(testUser);
        adminToken = jwtService.generateToken(adminUser);

        Kingdom kingdom = kingdomRepository.saveAndFlush(Kingdom.builder()
                .user(testUser)
                .name("Test Kingdom")
                .build());

        kingdomStateRepository.save(KingdomState.builder()
                .kingdom(kingdom)
                .gold(BigDecimal.ZERO)
                .mana(BigDecimal.ZERO)
                .resilience(BigDecimal.ZERO)
                .stability(new BigDecimal("100.00"))
                .build());

        playerProgressRepository.save(PlayerProgress.builder()
                .user(testUser)
                .kingdom(kingdom)
                .level(1)
                .experiencePoints(0)
                .build());
    }

    private NarrativeEvent createAlwaysFireEvent() {
        NarrativeEvent event = NarrativeEvent.builder()
                .title("Test Event")
                .description("A test narrative event.")
                .requiredLevel(1)
                .priority(10)
                .build();

        Choice choice = Choice.builder()
                .narrativeEvent(event)
                .text("Test Choice")
                .displayOrder(1)
                .build();

        choice.getEffects().add(Effect.builder()
                .choice(choice)
                .effectType(EffectType.GOLD_CHANGE)
                .value(new BigDecimal("10"))
                .build());

        event.getChoices().add(choice);

        event.getTriggerRules().add(TriggerRule.builder()
                .narrativeEvent(event)
                .conditionType(ConditionType.RANDOM)
                .probability(new BigDecimal("1.000"))
                .build());

        return narrativeEventRepository.saveAndFlush(event);
    }

    // =========================================================================
    // GET /narrative-events/next
    // =========================================================================

    @Nested
    @DisplayName("GET /narrative-events/next")
    class GetNextEvent {

        @Test
        @DisplayName("Deve retornar evento quando há evento aplicável")
        void returnsEventWhenApplicable() throws Exception {
            createAlwaysFireEvent();

            mockMvc.perform(get("/narrative-events/next")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.title").value("Test Event"))
                    .andExpect(jsonPath("$.description").isNotEmpty());
        }

        @Test
        @DisplayName("Deve retornar 204 quando nenhum evento se aplica")
        void returns204WhenNoEventApplies() throws Exception {
            NarrativeEvent event = NarrativeEvent.builder()
                    .title("High Level Event")
                    .description("Requires high level")
                    .requiredLevel(99)
                    .priority(10)
                    .build();
            narrativeEventRepository.save(event);

            mockMvc.perform(get("/narrative-events/next")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Deve retornar 204 quando todos os eventos estão resolvidos")
        void returns204WhenAllResolved() throws Exception {
            NarrativeEvent event = createAlwaysFireEvent();
            UUID choiceId = event.getChoices().get(0).getId();

            mockMvc.perform(post("/narrative-events/{id}/choices/{choiceId}/resolve",
                            event.getId(), choiceId)
                    .header("Authorization", "Bearer " + playerToken));

            mockMvc.perform(get("/narrative-events/next")
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(get("/narrative-events/next"))
                    .andExpect(status().isForbidden());
        }
    }

    // =========================================================================
    // GET /narrative-events/{id}
    // =========================================================================

    @Nested
    @DisplayName("GET /narrative-events/{id}")
    class GetEvent {

        @Test
        @DisplayName("Deve retornar evento com choices sem expor effects")
        void returnsEventWithChoices() throws Exception {
            NarrativeEvent event = createAlwaysFireEvent();

            mockMvc.perform(get("/narrative-events/{id}", event.getId())
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(event.getId().toString()))
                    .andExpect(jsonPath("$.title").value("Test Event"))
                    .andExpect(jsonPath("$.choices").isArray())
                    .andExpect(jsonPath("$.choices[0].id").isNotEmpty())
                    .andExpect(jsonPath("$.choices[0].text").value("Test Choice"))
                    .andExpect(jsonPath("$.choices[0].displayOrder").value(1))
                    .andExpect(jsonPath("$.choices[0].effects").doesNotExist());
        }

        @Test
        @DisplayName("Deve retornar 404 para evento inexistente")
        void returns404ForNonExistentEvent() throws Exception {
            mockMvc.perform(get("/narrative-events/{id}", UUID.randomUUID())
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("EVENT_NOT_FOUND"));
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(get("/narrative-events/{id}", UUID.randomUUID()))
                    .andExpect(status().isForbidden());
        }
    }

    // =========================================================================
    // POST /narrative-events/{id}/choices/{choiceId}/resolve
    // =========================================================================

    @Nested
    @DisplayName("POST /narrative-events/{id}/choices/{choiceId}/resolve")
    class ResolveChoice {

        private NarrativeEvent event;
        private UUID choiceId;

        @BeforeEach
        void createEvent() {
            event = createAlwaysFireEvent();
            choiceId = event.getChoices().get(0).getId();
        }

        @Test
        @DisplayName("Deve retornar KingdomState e PlayerProgress atualizados")
        void returnsUpdatedStateAndProgress() throws Exception {
            mockMvc.perform(post("/narrative-events/{id}/choices/{choiceId}/resolve",
                            event.getId(), choiceId)
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.state.gold").value(10))
                    .andExpect(jsonPath("$.state.stability").isNotEmpty())
                    .andExpect(jsonPath("$.progress.level").value(1))
                    .andExpect(jsonPath("$.progress.experiencePoints").value(0));
        }

        @Test
        @DisplayName("Deve retornar 409 ao resolver evento já resolvido")
        void returns409WhenAlreadyResolved() throws Exception {
            mockMvc.perform(post("/narrative-events/{id}/choices/{choiceId}/resolve",
                            event.getId(), choiceId)
                    .header("Authorization", "Bearer " + playerToken));

            mockMvc.perform(post("/narrative-events/{id}/choices/{choiceId}/resolve",
                                    event.getId(), choiceId)
                            .header("Authorization", "Bearer " + playerToken))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value("EVENT_ALREADY_RESOLVED"));
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(post("/narrative-events/{id}/choices/{choiceId}/resolve",
                            event.getId(), choiceId))
                    .andExpect(status().isForbidden());
        }
    }

    // =========================================================================
    // POST /admin/narrative-events
    // =========================================================================

    @Nested
    @DisplayName("POST /admin/narrative-events")
    class AdminCreateEvent {

        @Test
        @DisplayName("Deve retornar 403 para role PLAYER")
        void returns403ForPlayerRole() throws Exception {
            mockMvc.perform(post("/admin/narrative-events")
                            .header("Authorization", "Bearer " + playerToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildCreateRequest())))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Deve criar evento com sucesso para role ADMIN")
        void createsEventForAdminRole() throws Exception {
            mockMvc.perform(post("/admin/narrative-events")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildCreateRequest())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.title").value("Admin Event"))
                    .andExpect(jsonPath("$.choices").isArray())
                    .andExpect(jsonPath("$.choices[0].text").value("Admin Choice"));
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void returns403WithoutToken() throws Exception {
            mockMvc.perform(post("/admin/narrative-events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildCreateRequest())))
                    .andExpect(status().isForbidden());
        }

        private Map<String, Object> buildCreateRequest() {
            return Map.of(
                    "title", "Admin Event",
                    "description", "Created via admin endpoint",
                    "requiredLevel", 1,
                    "priority", 5,
                    "choices", List.of(Map.of(
                            "text", "Admin Choice",
                            "displayOrder", 1,
                            "effects", List.of(Map.of(
                                    "effectType", "GOLD_CHANGE",
                                    "value", 5
                            ))
                    )),
                    "triggerRules", List.of(Map.of(
                            "conditionType", "RANDOM",
                            "probability", 1.0
                    ))
            );
        }
    }
}
