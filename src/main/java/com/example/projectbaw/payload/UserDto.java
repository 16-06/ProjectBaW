package com.example.projectbaw.payload;

import com.example.projectbaw.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
public class UserDto {

    @Getter
    @Setter
    public static class RequestDto {

        private String username;
        private String password;

    }

    @Getter
    @Setter
    public static class ResponseDto {

        private Long id;
        private String username;
    }

    @Getter
    @Setter
    public static class ChangePassDto {

        private String password;
        private String newPassword;

    }

    @Getter
    @Setter
    public static class RegisterDto {

        private String username;
        private String email;
        private String password;

    }

    @Getter
    @Setter
    public static class ResetPasswordDto {

        private String resetPasswordToken;
        private String newPassword;

    }

    @Getter
    @Setter
    public static class passwordResetRequestDto {
        private String email;

    }


    @Getter
    @Setter
    public static class TwoFactorDto {
        private String username;
        private String code;

    }

    @Getter
    @Setter
    public static class LoginDto {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class FullUserDto {
        private Long id;
        private String username;
        private String email;
        private boolean enabledAccount;
        private boolean twoFactorEnabled;
        private boolean bannedAccount;
        private Role role;
    }

    @Getter
    @Setter
    public static class AuthenticatedUserDto {
        private Long id;
        private String username;
        private String token;
    }
}
