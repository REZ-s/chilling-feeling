package com.joolove.core.security.jwt.controller;

import com.joolove.core.dto.request.SignInRequest;
import com.joolove.core.dto.response.SignInResponse;
import com.joolove.core.security.jwt.utils.JwtUtils;
import com.joolove.core.security.service.AuthService;
import com.joolove.core.security.service.RefreshTokenService;
import com.joolove.core.security.service.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @PostMapping("/oauth2/signin")
    public ResponseEntity<?> oauth2LoginUser(@Valid @RequestBody SignInRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = jwtUtils.generateJwtCookie(userPrincipal.getUsername()).toString();

        String refreshToken = refreshTokenService.getRefreshTokenCookie(userPrincipal.getUsername()).toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessToken)
                .header(HttpHeaders.SET_COOKIE, refreshToken)
                .body(Collections.emptyList());
    }

/*    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Valid @RequestBody SignInRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        SignInResponse response = SignInResponse.builder()
                .id(userPrincipal.getUsername())
                .username(userPrincipal.getUsername())
                .roles(roles)
                .build();

        String jwtCookie = jwtUtils.generateJwtCookie(userPrincipal.getUsername()).toString();
        String jwtRefreshCookie = refreshTokenService.getRefreshTokenCookie(userPrincipal.getUsername()).toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie)
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie)
                .body(response);
    }*/

/*
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User.SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("Error: username is already taken!");
        }

        if (authenticationRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Error: Email is already in use!");
        }

        List<String> strRoles = request.getRoles();
        List<Role> roles = new ArrayList<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
        else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "manager" -> {
                        Role mgrRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(mgrRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        // Create new user's account
        User user = User.builder()
                .username(request.getUsername())
                .accountType((short)1)
                .build();

        Password password = Password.builder()
                .user(user)
                .pw(passwordEncoder.encode(request.getPassword()))
                .build();
        user.setPassword(password);

        List<UserRole> userRoles = new ArrayList<>();
        for (Role r : roles) {
            UserRole userRole = UserRole.builder()
                    .user(user)
                    .role(r)
                    .build();
            userRoles.add(userRole);
        }
        user.setRoles(userRoles);

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }
    */

/*    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser() {
        return authService.logout(true);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        if (jwtUtils.getJwtFromCookies(request) == null) {  // access token 이 다른 경우
            return authService.logout(false);
        }

        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body("Token is refreshed successfully!");
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }

        return ResponseEntity.badRequest()
                .body("Refresh Token is empty!");
    }*/

}