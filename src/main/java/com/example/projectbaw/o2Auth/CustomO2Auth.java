package com.example.projectbaw.o2Auth;

import com.example.projectbaw.model.User;
import com.example.projectbaw.model.UserProfile;
import com.example.projectbaw.model.UserSecurity;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomO2Auth implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {

                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setEmail(email);
                    newUser.setEnabledAccount(true);
                    newUser.setTwoFactorEnabled(false);
                    newUser.setRole(Role.USER);
                    newUser.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));

                    UserProfile userProfile = new UserProfile();
                    userProfile.setUser(newUser);

                    UserSecurity userSecurity = new UserSecurity();
                    userSecurity.setUser(newUser);

                    newUser.setProfile(userProfile);
                    newUser.setSecurityData(userSecurity);

                    return userRepository.save(newUser);
                });

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())),
                oAuth2User.getAttributes(),
                "email"
        );
    }

}
