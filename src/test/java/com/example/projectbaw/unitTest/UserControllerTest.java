package com.example.projectbaw.unitTest;

import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.config.JwtAuthFilter;
import com.example.projectbaw.config.JwtUtil;
import com.example.projectbaw.controller.UserController;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @InjectMocks
    private UserController userController;

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChangePassword_Success() {

        // Arrange
        UserDto.ChangePassDto changePassDto = new UserDto.ChangePassDto();
        changePassDto.setPassword("old123");
        changePassDto.setNewPassword("new123");

        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(userService.changePassword(changePassDto, userDetails)).thenReturn(true);

        // Act
        ResponseEntity<?> response = userController.changePassword(changePassDto, userDetails);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Successful password change", response.getBody());
    }

    @Test
    public void testChangePassword_Failure() {

        // Arrange
        UserDto.ChangePassDto changePassDto = new UserDto.ChangePassDto();
        changePassDto.setPassword("wrongOld");
        changePassDto.setNewPassword("new123");

        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(userService.changePassword(changePassDto, userDetails)).thenReturn(false);

        // Act
        ResponseEntity<?> response = userController.changePassword(changePassDto, userDetails);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Incorrect old password or new password ", response.getBody());
    }



}
