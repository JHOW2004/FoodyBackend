package com.foody.delivery.security;

import com.foody.delivery.domain.model.User;
import com.foody.delivery.domain.model.UserRole;
import com.foody.delivery.domain.repository.UserRepository;
import com.foody.delivery.security.dto.AuthResponse;
import com.foody.delivery.security.dto.LoginRequest;
import com.foody.delivery.security.dto.RegisterRequest;
import com.foody.delivery.security.jwt.JwtService;
import com.foody.delivery.security.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .name("Desenvolvedor Foody")
                .email("test@foody.com")
                .password("senha123")
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@foody.com")
                .password("senha123")
                .build();

        mockUser = User.builder()
                .id(1L)
                .name("Desenvolvedor Foody")
                .email("test@foody.com")
                .password("encoded_password")
                .role(UserRole.USER)
                .build();
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso e retornar token JWT")
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail("test@foody.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("mock_jwt_token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mock_jwt_token", response.getToken());
        assertEquals("test@foody.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar e-mail já existente")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("test@foody.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(registerRequest)
        );

        assertEquals("E-mail já cadastrado no sistema!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar token JWT")
    void shouldLoginSuccessfully() {
        when(userRepository.findByEmail("test@foody.com")).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("mock_jwt_token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mock_jwt_token", response.getToken());
        assertEquals("test@foody.com", response.getEmail());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
