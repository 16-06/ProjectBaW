package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class UserProfileDto {

    public static class RequestDto{

        private String firstName;
        private String lastName;
        private String bio;
        private String interests;
        private byte[] avatarImage;

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }

        public String getInterests() { return interests; }
        public void setInterests(String interests) { this.interests = interests; }

        public byte[] getAvatarImage() { return avatarImage; }
        public void setAvatarImage(byte[] avatarImage) { this.avatarImage = avatarImage; }


    }

    public static class ResponseDto{
        private Long id;
        private String firstName;
        private String lastName;
        private String bio;
        private String interests;
        private byte[] avatarImage;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }


        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }

        public String getInterests() { return interests; }
        public void setInterests(String interests) { this.interests = interests; }

        public byte[] getAvatarImage() { return avatarImage; }
        public void setAvatarImage(byte[] avatarImage) { this.avatarImage = avatarImage;
    }


}
}
