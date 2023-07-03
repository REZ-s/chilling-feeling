package com.joolove.core.config;

import com.joolove.core.repository.RoleRepository;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.security.jwt.utils.AccessDeniedHandlerJwt;
import com.joolove.core.security.jwt.utils.AuthEntryPointJwt;
import com.joolove.core.security.jwt.utils.JwtAuthFilter;
import com.joolove.core.security.oauth2.CommonLogoutSuccessHandler;
import com.joolove.core.security.oauth2.FormLoginSuccessHandler;
import com.joolove.core.security.oauth2.OAuth2FailureHandler;
import com.joolove.core.security.oauth2.OAuth2SuccessHandler;
import com.joolove.core.security.service.LogoutTokenService;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserDetailsServiceImpl;
import com.joolove.core.security.service.OAuth2UserServiceImpl;
import com.joolove.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleRepository roleRepository;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final AccessDeniedHandlerJwt accessDeniedHandlerJwt;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final FormLoginSuccessHandler formLoginSuccessHandler;
    private final CommonLogoutSuccessHandler commonLogoutSuccessHandler;

    @Bean
    public OAuth2UserServiceImpl oAuth2UserService() {
        return new OAuth2UserServiceImpl(userRepository, passwordEncoder(), roleRepository);
    }

    @Bean
    public JwtAuthFilter jwtAuthenticationFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public OAuth2FailureHandler oAuth2FailureHandler() {
        return new OAuth2FailureHandler();
    }

    /**
     * hashing (password + salt)
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers()
                .frameOptions().sameOrigin();

        http.cors()
                .and()
                .csrf().disable()  // don't need for using rest api
                .httpBasic().disable();  // don't need for using jwt

        http.authorizeRequests()
                .anyRequest().permitAll();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // don't need for using jwt

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()    // 아래 번호 순서대로 필터링
                    .authenticationEntryPoint(authEntryPointJwt)  // (1) 401 Unauthorized 인증이 안된 경우
                    .accessDeniedHandler(accessDeniedHandlerJwt);   // (2) 403 Forbidden 접근권한이 없는 경우

        http.formLogin()
                .loginPage("/cf_login2")
                .loginProcessingUrl("/cf_login/complete")
                .defaultSuccessUrl("/cf_main")
                .failureUrl("/cf_login?error=true")
                .successHandler(formLoginSuccessHandler);

        http.oauth2Login()
                .loginPage("/cf_login")
                .defaultSuccessUrl("/cf_main")
                .failureUrl("/cf_login?error=true")
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler())
                .userInfoEndpoint().userService(oAuth2UserService());

        http.logout()
                .logoutUrl("/cf_logout")
                .logoutSuccessUrl("/cf_main")
                .addLogoutHandler(commonLogoutSuccessHandler)   // refreshToken 삭제, logoutToken 생성 (블랙리스트)
                .deleteCookies("JSESSIONID", "jooloveJwt", "jooloveJwtRefresh");

        http.rememberMe()
                .key("uniqueAndSecret") // remember-me cookie key
                .rememberMeParameter("remember-me")     // checkbox name
                .rememberMeCookieName("remember-me")    // real cookie name
                .tokenValiditySeconds(60 * 60 * 24 * 30)  // 30 days
                .alwaysRemember(false)
                .userDetailsService(userDetailsService);

        return http.build();
    }

}