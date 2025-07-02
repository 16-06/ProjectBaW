package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class UserDto {


    public static class RequestDto {
        private String username;
        private String password;


        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

    }

    public static class ResponseDto {
        private Long id;
        private String username;
        private String token;

       public Long getId() { return id; }
       public void setId(Long id) { this.id = id; }
       public String getUsername() { return username; }
       public void setUsername(String username) { this.username = username; }
    }

    public static class ChangePassDto {

        private String password;
        private String newPassword;

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class RegisterDto {
        private String username;
        private String email;
        private String password;


        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

    }
    public static class ResetPasswordDto {
        private String resetPasswordToken;
        private String newPassword;

        public String getResetPasswordToken() { return resetPasswordToken; }
        public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class passwordResetRequestDto {
        private String email;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class getProfileDto {
        private String username;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

    }
}
