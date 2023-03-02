package com.joolove.core.config;

import com.joolove.core.repository.RoleRepository;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.security.jwt.utils.AccessDeniedHandlerJwt;
import com.joolove.core.security.jwt.utils.AuthEntryPointJwt;
import com.joolove.core.security.jwt.utils.AuthTokenFilter;
import com.joolove.core.security.jwt.utils.OAuthTokenFilter;
import com.joolove.core.security.oauth2.OAuth2FailureHandler;
import com.joolove.core.security.oauth2.OAuth2SuccessHandler;
import com.joolove.core.security.service.UserDetailsServiceImpl;
import com.joolove.core.security.service.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AuthEntryPointJwt unauthorizedHandler;

    private final AccessDeniedHandlerJwt accessDeniedHandlerJwt;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public OAuth2UserServiceImpl oAuth2UserService() {
        return new OAuth2UserServiceImpl(userRepository, passwordEncoder(), roleRepository);
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
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
                .httpBasic().disable()  // don't need for using jwt
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // don't need for using rest api & jwt
                //STATELESS (X) because it's not working google oauth2 login info
                //NEVER (O) using temporary session for bring login info
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)  // 401 Unauthorized
                    .accessDeniedHandler(accessDeniedHandlerJwt)    // 403 Forbidden
                .and()
                .authorizeRequests()
                    .antMatchers("/api/v1/auth/**").permitAll()
                    .antMatchers("/user/**").authenticated()
                    .antMatchers("/manager/**").access("hasRole('MANAGER') or hasRole('ADMIN')")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().permitAll();
                //.anyRequest().authenticated().and()

        http.oauth2Login()
                .loginPage("/loginForm")
                .defaultSuccessUrl("/")
                .failureUrl("/loginForm")
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler())
                .userInfoEndpoint().userService(oAuth2UserService());

        //얘 빼볼까?
        http.authenticationProvider(authenticationProvider());  // Form login

        // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
        http.headers().frameOptions().sameOrigin();

        // before filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterBefore(new OAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // after filter

        return http.build();
    }

}