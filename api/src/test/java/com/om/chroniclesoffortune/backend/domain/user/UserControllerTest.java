package com.om.chroniclesoffortune.backend.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om.chroniclesoffortune.backend.domain.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("GET /users/me")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // Helper: registra um usuário e retorna o token JWT
    private String registerAndGetToken() throws Exception {
        var body = Map.of(
                "name", "Andre Menezes",
                "username", "andre_menezes",
                "email", "andre@email.com",
                "password", "Senha@123",
                "confirmPassword", "Senha@123"
        );

        var result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // Extrai o token do JSON de resposta
        var responseJson = objectMapper.readTree(result.getResponse().getContentAsString());
        return responseJson.get("token").asText();
    }

    @Test
    @DisplayName("Deve retornar dados do usuário autenticado")
    void getMeSuccess() throws Exception {
        var token = registerAndGetToken();

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Andre Menezes"))
                .andExpect(jsonPath("$.username").value("andre_menezes"))
                .andExpect(jsonPath("$.email").value("andre@email.com"))
                .andExpect(jsonPath("$.role").value("PLAYER"));
    }

    @Test
    @DisplayName("Deve retornar 403 sem token de autenticação")
    void getMeWithoutToken() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 403 com token malformado")
    void getMeWithInvalidToken() throws Exception {
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer token.invalido.aqui"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 403 com header Authorization incorreto")
    void getMeWithWrongAuthHeader() throws Exception {
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Basic dXNlcjpwYXNz")) // Basic auth em vez de Bearer
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 403 com token expirado")
    void getMeWithExpiredToken() throws Exception {
        // Gera um token expirado diretamente pelo JwtService com tempo negativo
        var user = userRepository.findByEmailOrUsername("andre@email.com", "andre_menezes");

        // Registra o usuário primeiro
        registerAndGetToken();

        var expiredToken = jwtService.generateExpiredTokenForTest(
                userRepository.findByEmailOrUsername("andre@email.com", "andre_menezes").orElseThrow()
        );

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isForbidden());
    }
}
