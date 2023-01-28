package com.joolove.core.domain.member;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.auth.*;
import com.joolove.core.domain.billing.Orders;
import com.joolove.core.domain.log.LoginLog;
import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.domain.recommend.RecommendationDaily;
import com.joolove.core.domain.recommend.RecommendationFirst;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Column(unique = true, length = 30)
    private String username;

    @NotNull
    private Short accountType;

    @Builder
    public User(UUID id, String username, Short accountType) {
        this.id = id;
        this.username = username;
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", accountType=" + accountType +
                '}';
    }

    /**
     * mappedBy
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private SocialLogin socialLogin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Password password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private CIDI cidi;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Profile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Favorite favorite;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Authentication authentication;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private RecommendationFirst recommendationFirst;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<LoginLog> loginLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<UserActivityLog> userActivityLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Device> devices = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<RecommendationDaily> recommendationDailies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<UserRole> roles = new ArrayList<>();

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public void setSocialLogin(SocialLogin socialLogin) {
        this.socialLogin = socialLogin;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setCidi(CIDI cidi) {
        this.cidi = cidi;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public void setRecommendationFirst(RecommendationFirst recommendationFirst) {
        this.recommendationFirst = recommendationFirst;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public void setLoginLogs(List<LoginLog> loginLogs) {
        this.loginLogs = loginLogs;
    }

    public void setUserActivityLogs(List<UserActivityLog> userActivityLogs) {
        this.userActivityLogs = userActivityLogs;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public void setRecommendationDailies(List<RecommendationDaily> recommendationDailies) {
        this.recommendationDailies = recommendationDailies;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * DTO
     */
    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SigninRequest {
        @NotBlank
        @Size(min = 4, max = 20)
        private String username;

        @NotBlank
        @Size(min = 6, max = 40)
        private String password;

        @Builder
        public SigninRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SigninResponse {
        private UUID id;
        private String username;

        private String email;
        private List<String> roles;

        @Builder
        public SigninResponse(UUID id, String username, String email, List<String> roles) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.roles = roles;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignupRequest {
        @NotBlank
        @Size(min = 4, max = 20)
        private String username;

        @NotBlank
        @Size(max = 50)
        @Email
        private String email;

        private List<String> roles;

        @NotBlank
        @Size(min = 6, max = 40)
        private String password;

        @Builder
        public SignupRequest(String username, String email, List<String> roles, String password) {
            this.username = username;
            this.email = email;
            this.roles = roles;
            this.password = password;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OAuth2Response {

        private String email;
        private String name;
        private String picture;

        @Builder
        public OAuth2Response(String email, String name, String picture) {
            this.email = email;
            this.name = name;
            this.picture = picture;
        }

        public static OAuth2Response build(OAuth2User oAuth2User) {
            var attributes = oAuth2User.getAttributes();
            return OAuth2Response.builder()
                    .email((String)attributes.get("email"))
                    .name((String)attributes.get("name"))
                    .picture((String)attributes.get("picture"))
                    .build();
        }
    }

}
