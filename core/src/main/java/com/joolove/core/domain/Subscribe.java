package com.joolove.core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
@Table(name = "subscribe")
public class Subscribe {

    @Id @GeneratedValue
    @Column(name = "subscribe_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscribe")
    private List<SubscribeItem> subscribeItems = new ArrayList<>();

    private LocalDateTime subscribeDate;

    @Enumerated(EnumType.STRING)
    private SubscribeStatus subscribeStatus;


}