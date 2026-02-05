package com.console.game.config;

import com.console.game.enums.Provider;
import com.console.game.enums.Role;
import com.console.game.model.User;
import com.console.game.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public OAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String avatar = oauthUser.getAttribute("picture");
        String googleId = oauthUser.getAttribute("sub");

        User user = userRepository.findByEmail(email).orElse(null);

        // Nếu chưa có user → tạo mới
        if (user == null) {
            user = User.builder()
                    .email(email)
                    .fullName(name)
                    .avatarUrl(avatar)
                    .provider(Provider.GOOGLE)
                    .providerId(googleId)
                    .role(Role.CUSTOMER)
                    .isActive(true)
                    .build();

            userRepository.save(user);
        }
        else {
            // Nếu email tồn tại nhưng không phải Google → chặn
            if (user.getProvider() != Provider.GOOGLE) {
                response.sendRedirect("/auth/login?error=account_exists");
                return;
            }
        }

        response.sendRedirect("/");
    }
}