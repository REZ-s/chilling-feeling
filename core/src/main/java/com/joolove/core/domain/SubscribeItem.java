package com.joolove.core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "subscribe_item")
public class SubscribeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscribe_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribe_id")
    private Subscribe subscribe;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<Item> items = new ArrayList<>();

    private int price;


}