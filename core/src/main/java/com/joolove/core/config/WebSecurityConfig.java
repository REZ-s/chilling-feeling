package com.joolove.core.config;

import com.joolove.core.repository.RoleRepository;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.security.jwt.utils.AccessDeniedHandlerJwt;
import com.joolove.core.security.jwt.utils.AuthEntryPointJwt;
import com.joolove.core.security.jwt.utils.FormAuthFilter;
import com.joolove.core.security.jwt.utils.JwtAuthFilter;
import com.joolove.core.security.oauth2.FormLoginSuccessHandler;
import com.joolove.core.security.oauth2.OAuth2FailureHandler;
import com.joolove.core.security.oauth2.OAuth2SuccessHandler;
import com.joolove.core.security.service.UserDetailsServiceImpl;
import com.joolove.core.security.service.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AuthEntryPointJwt authEntryPointJwt;

    private final AccessDeniedHandlerJwt accessDeniedHandlerJwt;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final FormLoginSuccessHandler formLoginSuccessHandler;


    @Bean
    public OAuth2UserServiceImpl oAuth2UserService() {
        return new OAuth2UserServiceImpl(userRepository, passwordEncoder(), roleRepository);
    }

    @Bean
    public JwtAuthFilter jwtAuthenticationFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    public FormAuthFilter formAuthenticationFilter() {
        FormAuthFilter formAuthFilter = new FormAuthFilter(authenticationManager());
        formAuthFilter.setFilterProcessesUrl("/sign_in");
        formAuthFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler);
        formAuthFilter.afterPropertiesSet();
        return formAuthFilter;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
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
        http.cors()
                .and()
                .csrf().disable()  // don't need for using rest api
                //.httpBasic().disable()  // don't need for using jwt
                .authorizeRequests().anyRequest().permitAll()   // all access about using jwt
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // don't need for using jwt
                .and()
                .exceptionHandling()    // 아래 번호 순서대로 필터링됨
                    .authenticationEntryPoint(authEntryPointJwt)  // (1) 401 Unauthorized 인증이 안된 경우
                    .accessDeniedHandler(accessDeniedHandlerJwt);   // (2) 403 Forbidden 접근권한이 없는 경우

        http.formLogin()
                .loginPage("/sign_in")
                .successForwardUrl("/")
                .failureForwardUrl("/");

        http.oauth2Login()
                .loginPage("/sign_in")
                .defaultSuccessUrl("/")
                .failureUrl("/")
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler())
                .userInfoEndpoint().userService(oAuth2UserService());

        http.logout()
                .logoutUrl("/logout").permitAll();

        http.authenticationProvider(authenticationProvider());

        // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
        http.headers()
                .frameOptions()
                .sameOrigin();

        // common before filter
        http.addFilterBefore(formAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), OncePerRequestFilter.class);

        // common after filter

        return http.build();
    }

}