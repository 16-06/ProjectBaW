package com.example.projectbaw.payload;

import com.example.projectbaw.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.temporal.ChronoUnit;

@Data
@RequiredArgsConstructor
public class AdminDto {

    @Getter
    @Setter
    @Data
    public static class ChangeRoleDto {

        private Long userId;
        private Role newRole;

    }

    @Getter
    @Setter
    @Data
    public static class BanUserDto  {

        private Long userId;
        private long durationAmount;
        private ChronoUnit durationUnit;

    }
}
