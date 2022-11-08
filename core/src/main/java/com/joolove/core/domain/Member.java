package com.joolove.core.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "member")
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Embedded
    private Address address;

    @NotNull
    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Subscribe> subscribes = new ArrayList<>();


}
