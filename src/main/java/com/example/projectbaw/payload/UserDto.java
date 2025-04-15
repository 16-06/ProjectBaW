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

       public Long getId() { return id; }
       public void setId(Long id) { this.id = id; }
       public String getUsername() { return username; }
       public void setUsername(String username) { this.username = username; }
    }
}
