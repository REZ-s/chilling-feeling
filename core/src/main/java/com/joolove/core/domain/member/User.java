package com.joolove.core.domain.member;

import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.auth.*;
import com.joolove.core.domain.billing.Orders;
import com.joolove.core.domain.log.LoginLog;
import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.domain.recommend.UserRecommendationDaily;
import com.joolove.core.domain.recommend.UserRecommendationBase;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @Column(unique = true, length = 64)
    private String username;    // email

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
    private UserRecommendationBase userRecommendationBase;

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
    private List<UserRecommendationDaily> recommendationDailies = new ArrayList<>();

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

    public void setUserRecommendationBase(UserRecommendationBase userRecommendationBase) {
        this.userRecommendationBase = userRecommendationBase;
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

    public void setRecommendationDailies(List<UserRecommendationDaily> recommendationDailies) {
        this.recommendationDailies = recommendationDailies;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

}
