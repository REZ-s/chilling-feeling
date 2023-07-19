package com.joolove.core.security.service;

import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.member.User;
import com.joolove.core.security.oauth2.GoogleUserInfo;
import com.joolove.core.security.oauth2.KakaoUserInfo;
import com.joolove.core.security.oauth2.NaverUserInfo;
import com.joolove.core.security.oauth2.OAuth2UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPrincipal implements UserDetails, OAuth2User {
    private String username;
    private OAuth2UserInfo oAuth2UserInfo;  // attributes == oAuth2UserInfo.getAttributes();
    private Collection<? extends GrantedAuthority> authorities;

    @Builder(builderClassName = "UserDetailsBuilder", builderMethodName = "userDetailsBuilder")
    public UserPrincipal(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    @Builder(builderClassName = "OAuth2UserBuilder", builderMethodName = "oAuth2UserBuilder")
    public UserPrincipal(String username, OAuth2UserInfo oAuth2UserInfo) {
        this.username = username;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static UserPrincipal buildUserDetails(String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(Role.ERole.ROLE_USER.name()));

        return UserPrincipal.userDetailsBuilder()
                .username(username)
                .authorities(authorities)
                .build();
    }

    public static UserPrincipal buildOAuth2User(String username, OAuth2UserInfo oAuth2UserInfo) {
        return UserPrincipal.oAuth2UserBuilder()
                .username(username)
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
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
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
    public String getUsername() {
        return username;
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

}