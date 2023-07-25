package com.joolove.core.config;

import com.joolove.core.domain.auth.Role;
import com.joolove.core.repository.UserRepository;
import com.joolove.core.utils.PasswordUtils;
import com.joolove.core.utils.filter.ValidationFilter;
import com.joolove.core.utils.handler.CustomAccessDeniedHandler;
import com.joolove.core.utils.handler.CustomAuthenticationEntryPoint;
import com.joolove.core.utils.filter.JwtAuthenticationFilter;
import com.joolove.core.utils.handler.CommonLogoutSuccessHandler;
import com.joolove.core.utils.handler.FormLoginSuccessHandler;
import com.joolove.core.utils.handler.OAuth2FailureHandler;
import com.joolove.core.utils.handler.OAuth2SuccessHandler;
import com.joolove.core.service.OAuth2UserServiceImpl;
import com.joolove.core.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserDetailsServiceImpl userDetailsService;
    private final OAuth2UserServiceImpl oAuth2UserService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final FormLoginSuccessHandler formLoginSuccessHandler;
    private final CommonLogoutSuccessHandler commonLogoutSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ValidationFilter validationFilter;

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 정적리소스 관련 설정 (경로 설정 및 캐시)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(7)));

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(7)));

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCacheControl(CacheControl.noCache().cachePrivate());
    }

    // 정적리소스 관련 필터 체인 (인증 및 접근권한과 같은 불필요한 필터 제외)
    @Bean
    @Order(0)
    public SecurityFilterChain securityFilterChainToIgnoringResources(HttpSecurity http) throws Exception {
        http.requestMatchers((matchers) ->
                        matchers.mvcMatchers("/css/**")
                                .mvcMatchers("/images/**")
                                .mvcMatchers("/js/**")
                )
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().permitAll())
                .securityContext().disable()
                .sessionManagement().disable()
                .logout().disable()
                .anonymous().disable()
                .csrf().disable()
                .exceptionHandling().disable()
                .requestCache()
                .requestCache(new NullRequestCache()).disable();

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers()
                .frameOptions().sameOrigin();

        http.cors()
                .and()
                .csrf().disable()  // don't need for using rest api
                .httpBasic().disable();  // don't need for using jwt

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // don't need for using jwt

        http.authorizeHttpRequests()
                .mvcMatchers(HttpMethod.GET, "/environment/*").hasRole(Role.ERole.ROLE_ADMIN.name())
                .anyRequest().permitAll();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(validationFilter, JwtAuthenticationFilter.class);

        http.exceptionHandling()    // 아래 번호 순서대로 필터링
                    .authenticationEntryPoint(customAuthenticationEntryPoint)  // (1) 401 Unauthorized 인증이 안된 경우
                    .accessDeniedHandler(customAccessDeniedHandler);   // (2) 403 Forbidden 접근권한이 없는 경우

        http.rememberMe()
                .key("uniqueAndSecret") // remember-me cookie key
                .rememberMeParameter("remember-me")     // checkbox name
                .rememberMeCookieName("remember-me")    // real cookie name
                .tokenValiditySeconds(60 * 60 * 24 * 365)  // 365 days
                .alwaysRemember(false)
                .userDetailsService(userDetailsService);

        http.formLogin()
                .loginPage("/login/password")
                .loginProcessingUrl("/login/success")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .successHandler(formLoginSuccessHandler);

        http.oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .userInfoEndpoint().userService(oAuth2UserService);

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .addLogoutHandler(commonLogoutSuccessHandler)   // refreshToken 삭제, logoutToken 생성 (블랙리스트)
                .deleteCookies("remember-me", "jooloveJwt", "jooloveJwtRefresh");

        return http.build();
    }

}