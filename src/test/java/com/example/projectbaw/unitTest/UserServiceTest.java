package com.example.projectbaw.unitTest;

import com.example.projectbaw.config.JwtUtil;
import com.example.projectbaw.enums.Role;
import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.UserSecurityRepository;
import com.example.projectbaw.service.EmailConfirmationService;
import com.example.projectbaw.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EmailConfirmationService emailConfirmationService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserSecurityRepository userSecurityRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        // given
        UserDto.RegisterDto registerDto = new UserDto.RegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("strongpassword");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        User mappedUser = new User();
        mappedUser.setUsername("testuser");
        mappedUser.setPassword("strongpassword");
        mappedUser.setEmail("test@example.com");

        when(userMapper.toRegister(registerDto)).thenReturn(mappedUser);
        when(bCryptPasswordEncoder.encode("strongpassword")).thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // when
        userService.registerUser(registerDto);

        // then
        verify(userRepository).save(userCaptor.capture());
        verify(emailConfirmationService).sendConfirmationEmail(eq("test@example.com"), anyString());

        User savedUser = userCaptor.getValue();

        assertEquals("testuser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("test@example.com", savedUser.getEmail());
        assertFalse(savedUser.isEnabledAccount());
        assertFalse(savedUser.isTwoFactorEnabled());
        assertEquals(Role.USER, savedUser.getRole());
        assertNotNull(savedUser.getProfile());
        assertNotNull(savedUser.getSecurityData().getActivationToken());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // given
        UserDto.RegisterDto registerDto = new UserDto.RegisterDto();
        registerDto.setUsername("existingUser");
        registerDto.setEmail("existing@example.com");
        registerDto.setPassword("password123");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registerDto));
    }

    @Test
    void shouldThrowExceptionWhenPasswordTooShort() {

        // given
        UserDto.RegisterDto registerDto = new UserDto.RegisterDto();
        registerDto.setUsername("newuser");
        registerDto.setEmail("new@example.com");
        registerDto.setPassword("short");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registerDto));
    }
}
