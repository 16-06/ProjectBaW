package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
public class UserProfileDto {

    @Getter
    @Setter
    public static class RequestDto{

        private String firstName;
        private String lastName;
        private String bio;
        private String interests;
        private byte[] avatarImage;

    }

    @Getter
    @Setter
    public static class ResponseDto{
        private Long id;
        private String firstName;
        private String lastName;
        private String bio;
        private String interests;
        private byte[] avatarImage;

    }


}

