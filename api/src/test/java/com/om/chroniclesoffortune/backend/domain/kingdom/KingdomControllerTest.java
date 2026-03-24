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
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("POST /kingdoms e GET /kingdoms/me")
class KingdomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KingdomRepository kingdomRepository;

    @Autowired
    private KingdomStateRepository kingdomStateRepository;

    @Autowired
    private PlayerProgressRepository playerProgressRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;
    private User testUser;

    @BeforeEach
    void setUp() {
        playerProgressRepository.deleteAll();
        kingdomStateRepository.deleteAll();
        kingdomRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(
                User.builder()
                        .name("Andre Menezes")
                        .username("andre_menezes")
                        .email("andre@email.com")
                        .password(passwordEncoder.encode("Senha@123"))
                        .role(Role.PLAYER)
                        .build()
        );

        token = jwtService.generateToken(testUser);
    }

    // =========================================================================
    // POST /kingdoms
    // =========================================================================

    @Nested
    @DisplayName("Criação de reino")
    class CreateKingdom {

        @Test
        @DisplayName("Deve criar reino e retornar summary com estado inicial zerado")
        void createKingdomSuccess() throws Exception {
            var body = Map.of("name", "Reino das Finanças");

            mockMvc.perform(post("/kingdoms")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.kingdom.id").isNotEmpty())
                    .andExpect(jsonPath("$.kingdom.name").value("Reino das Finanças"))
                    .andExpect(jsonPath("$.kingdom.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.state.gold").value(0))
                    .andExpect(jsonPath("$.state.mana").value(0))
                    .andExpect(jsonPath("$.state.resilience").value(0))
                    .andExpect(jsonPath("$.state.stability").value(100));
        }

        @Test
        @DisplayName("Deve retornar 409 ao tentar criar segundo reino")
        void createKingdomAlreadyExists() throws Exception {
            var body = Map.of("name", "Primeiro Reino");

            mockMvc.perform(post("/kingdoms")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body)));

            mockMvc.perform(post("/kingdoms")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Map.of("name", "Segundo Reino"))))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value("KINGDOM_ALREADY_EXISTS"));
        }

        @Test
        @DisplayName("Deve rejeitar nome com menos de 3 caracteres")
        void createKingdomNameTooShort() throws Exception {
            var body = Map.of("name", "AB");

            mockMvc.perform(post("/kingdoms")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("name")));
        }

        @Test
        @DisplayName("Deve rejeitar nome em branco")
        void createKingdomNameBlank() throws Exception {
            var body = Map.of("name", "");

            mockMvc.perform(post("/kingdoms")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void createKingdomWithoutToken() throws Exception {
            var body = Map.of("name", "Reino Sem Auth");

            mockMvc.perform(post("/kingdoms")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isForbidden());
        }
    }

    // =========================================================================
    // GET /kingdoms/me
    // =========================================================================

    @Nested
    @DisplayName("Consulta de reino")
    class GetMyKingdom {

        @BeforeEach
        void createKingdom() throws Exception {
            mockMvc.perform(post("/kingdoms")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of("name", "Reino das Finanças"))));
        }

        @Test
        @DisplayName("Deve retornar summary do reino do usuário autenticado")
        void getMyKingdomSuccess() throws Exception {
            mockMvc.perform(get("/kingdoms/me")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.kingdom.name").value("Reino das Finanças"))
                    .andExpect(jsonPath("$.state.stability").value(100));
        }

        @Test
        @DisplayName("Deve retornar 403 sem token")
        void getMyKingdomWithoutToken() throws Exception {
            mockMvc.perform(get("/kingdoms/me"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Consulta sem reino criado")
    class GetMyKingdomNotFound {

        @Test
        @DisplayName("Deve retornar 404 quando usuário não tem reino")
        void getMyKingdomNotFound() throws Exception {
            mockMvc.perform(get("/kingdoms/me")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("KINGDOM_NOT_FOUND"));
        }
    }
}
