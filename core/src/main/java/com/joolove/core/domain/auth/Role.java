package com.joolove.core.domain.auth;

import com.joolove.core.domain.ERole;
import com.joolove.core.domain.member.UserRole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.sql.OracleJoinFragment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(schema = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Role {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "role_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ERole name;

    @Builder
    public Role(ERole name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name=" + name +
                ", roles=" + roles +
                '}';
    }

    /**
     * mappedBy
     */
    @OneToMany(mappedBy = "role")
    private List<UserRole> roles = new ArrayList<>();

    public void setUserRoles(List<UserRole> roles) {
        this.roles = roles;
    }

}