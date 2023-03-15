package com.joolove.core.security.service;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

import com.joolove.core.domain.member.User;
import com.joolove.core.security.oauth2.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPrincipal implements UserDetails, OAuth2User {

    @Serial
    private static final long serialVersionUID = 1L;

    private User user;

    private OAuth2UserInfo oAuth2UserInfo;  // attributes == oAuth2UserInfo.getAttributes();

    private Collection<? extends GrantedAuthority> authorities;

    @Builder(builderClassName = "UserDetailsBuilder", builderMethodName = "userDetailsBuilder")
    public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Builder(builderClassName = "OAuth2UserBuilder", builderMethodName = "oAuth2UserBuilder")
    public UserPrincipal(User user, OAuth2UserInfo oAuth2UserInfo) {
        this.user = user;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static UserPrincipal buildUserDetails(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getName().name()))
                .collect(Collectors.toList());

        return UserPrincipal.userDetailsBuilder()
                .user(user)
                .authorities(authorities)
                .build();
    }

    public static UserPrincipal buildOAuth2User(User user, OAuth2UserInfo oAuth2UserInfo) {
        return UserPrincipal.oAuth2UserBuilder()
                .user(user)
                .oAuth2UserInfo(oAuth2UserInfo)
                .build();
    }

    public static OAuth2UserInfo buildOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> new GoogleUserInfo(attributes);
            case "naver" -> new NaverUserInfo(attributes);
            case "kakao" -> new KakaoUserInfo(attributes);
            default -> throw new RuntimeException();
        };
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null || authorities.isEmpty()) {
            authorities = user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRole().getName().name()))
                    .collect(Collectors.toList());
        }

        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfo.getAttributes();
    }


    @Override
    public String getName() {
        return oAuth2UserInfo.getProviderId();
    }

    @Override
    public String getPassword() {
        return user.getPassword().getPw();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정 만료 여부 (true : 만료되지 않음)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부 (true : 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 비밀번호 만료 여부 (true : 만료되지 않음)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 (true : 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        UserPrincipal userPrincipal = (UserPrincipal) o;
        return Objects.equals(user.getId(), userPrincipal.getUser().getId());
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "user=" + user +
                ", authorities=" + authorities +
                '}';
    }

}