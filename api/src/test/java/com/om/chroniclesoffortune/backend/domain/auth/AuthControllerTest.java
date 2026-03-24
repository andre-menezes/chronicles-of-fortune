package com.om.chroniclesoffortune.backend.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om.chroniclesoffortune.backend.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest sobe o contexto completo da aplicação (segurança, filtros, banco)
// @AutoConfigureMockMvc injeta o MockMvc para simular requisições HTTP sem subir servidor real
// @ActiveProfiles("test") usa application-test.yml com H2 no lugar do PostgreSQL
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("POST /auth/register e POST /auth/login")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper criado diretamente — não depende do contexto Spring para serializar JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    // Limpa o banco antes de cada teste para garantir isolamento
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // Corpo de registro válido reutilizado nos testes
    private Map<String, String> validRegisterBody() {
        return Map.of(
                "name", "Andre Menezes",
                "username", "andre_menezes",
                "email", "andre@email.com",
                "password", "Senha@123",
                "confirmPassword", "Senha@123"
        );
    }

    // =========================================================================
    // POST /auth/register
    // =========================================================================

    @Nested
    @DisplayName("Registro de usuário")
    class Register {

        @Test
        @DisplayName("Deve retornar token ao registrar com dados válidos")
        void registerSuccess() throws Exception {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterBody())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("Deve rejeitar nome sem sobrenome")
        void registerNameWithoutSurname() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("name", "Andre");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("name")));
        }

        @Test
        @DisplayName("Deve rejeitar nome com números")
        void registerNameWithNumbers() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("name", "Andre 123");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("name")));
        }

        @Test
        @DisplayName("Deve rejeitar nome com menos de 3 caracteres")
        void registerNameTooShort() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("name", "AB");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("name")));
        }

        @Test
        @DisplayName("Deve rejeitar username que começa com número")
        void registerUsernameStartsWithNumber() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("username", "1andre");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("username")));
        }

        @Test
        @DisplayName("Deve rejeitar username com menos de 3 caracteres")
        void registerUsernameTooShort() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("username", "ab");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("username")));
        }

        @Test
        @DisplayName("Deve rejeitar username com espaços")
        void registerUsernameWithSpaces() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("username", "andre menezes");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("username")));
        }

        @Test
        @DisplayName("Deve rejeitar email inválido")
        void registerInvalidEmail() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("email", "nao-e-um-email");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("email")));
        }

        @Test
        @DisplayName("Deve rejeitar senha com menos de 8 caracteres")
        void registerPasswordTooShort() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("password", "S@1");
            body.put("confirmPassword", "S@1");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("password")));
        }

        @Test
        @DisplayName("Deve rejeitar senha sem caractere especial")
        void registerPasswordWithoutSpecialChar() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("password", "Senha123");
            body.put("confirmPassword", "Senha123");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("password")));
        }

        @Test
        @DisplayName("Deve rejeitar senha sem número")
        void registerPasswordWithoutNumber() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("password", "Senha@abc");
            body.put("confirmPassword", "Senha@abc");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("password")));
        }

        @Test
        @DisplayName("Deve rejeitar senha sem letra")
        void registerPasswordWithoutLetter() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("password", "12345678@");
            body.put("confirmPassword", "12345678@");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors[*].field", hasItem("password")));
        }

        @Test
        @DisplayName("Deve rejeitar quando confirmPassword não confere")
        void registerPasswordMismatch() throws Exception {
            var body = new java.util.HashMap<>(validRegisterBody());
            body.put("confirmPassword", "Senha@456");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                    .andExpect(jsonPath("$.message").value("Passwords do not match"));
        }

        @Test
        @DisplayName("Deve rejeitar campos obrigatórios ausentes")
        void registerMissingRequiredFields() throws Exception {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(4))));
        }
    }

    // =========================================================================
    // POST /auth/login
    // =========================================================================

    @Nested
    @DisplayName("Login de usuário")
    class Login {

        // Registra um usuário antes dos testes de login
        @BeforeEach
        void registerUser() throws Exception {
            mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRegisterBody())));
        }

        @Test
        @DisplayName("Deve retornar token ao fazer login com username")
        void loginWithUsername() throws Exception {
            var body = Map.of("identifier", "andre_menezes", "password", "Senha@123");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("Deve retornar token ao fazer login com email")
        void loginWithEmail() throws Exception {
            var body = Map.of("identifier", "andre@email.com", "password", "Senha@123");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("Deve retornar 401 com credenciais inválidas")
        void loginWrongPassword() throws Exception {
            var body = Map.of("identifier", "andre_menezes", "password", "SenhaErrada@1");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
        }

        @Test
        @DisplayName("Deve retornar 401 quando usuário não existe")
        void loginUserNotFound() throws Exception {
            var body = Map.of("identifier", "inexistente", "password", "Senha@123");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
        }
    }
}
