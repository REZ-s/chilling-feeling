package com.joolove.core.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.joolove.core.domain.BaseTimeStamp;
import com.joolove.core.domain.auth.CIDI;
import com.joolove.core.domain.auth.Password;
import com.joolove.core.domain.auth.Role;
import com.joolove.core.domain.auth.SocialLogin;
import com.joolove.core.domain.billing.Cart;
import com.joolove.core.domain.billing.Orders;
import com.joolove.core.domain.log.LoginLog;
import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.domain.recommendation.UserRecommendationBase;
import com.joolove.core.domain.recommendation.UserRecommendationDaily;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(catalog = "member")
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
    @Email
    private String username;    // email

    @NotNull
    private Short accountType;  // 계정 유형 (1 : 소셜 로그인, 2 : 폼 로그인 - 회원가입, 3 : 특수 계정)

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

    public void updateAll(User user) {
        this.accountType = user.getAccountType();
        setPassword(user.getPassword());
        setRole(user.getRole());
        setSocialLogin(user.getSocialLogin());
        setCidi(user.getCidi());
        setAddresses(user.getAddresses());
        setFavorite(user.getFavorite());
        setProfile(user.getProfile());
        setOrders(user.getOrdersList());
        setLoginLogs(user.getLoginLogs());
        setUserPersonal(user.getUserPersonal());
        setUserRecommendationBase(user.getUserRecommendationBase());
        setUserRecommendationDaily(user.getUserRecommendationDaily());
    }

    /**
     * mappedBy
     */
    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Password password;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Role role;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private SocialLogin socialLogin;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private CIDI cidi;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Profile profile;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Favorite favorite;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Cart cart;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private UserPersonal userPersonal;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private UserRecommendationBase userRecommendationBase;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private UserRecommendationDaily userRecommendationDaily;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Orders> ordersList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<LoginLog> loginLogs = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Address> addresses = new ArrayList<>();

    public void setRole(Role role) {
        this.role = role;
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

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setUserPersonal(UserPersonal userPersonal) {
        this.userPersonal = userPersonal;
    }

    public void setUserRecommendationBase(UserRecommendationBase userRecommendationBase) {
        this.userRecommendationBase = userRecommendationBase;
    }

    public void setUserRecommendationDaily(UserRecommendationDaily userRecommendationDaily) {
        this.userRecommendationDaily = userRecommendationDaily;
    }

    public void setOrders(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public void setLoginLogs(List<LoginLog> loginLogs) {
        this.loginLogs = loginLogs;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

}
