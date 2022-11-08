package com.joolove.core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
@Table(name = "items")
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "items")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    //추후 구현 다대다 관계를 풀어서 작성
    private List<Category> categories = new ArrayList<>();
}
