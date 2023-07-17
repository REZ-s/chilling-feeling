package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(catalog = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category extends BaseTimeStamp {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "category_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @NotBlank
    @Column(unique = true)
    private String categoryName;

    @Builder
    public Category(UUID id, Category parent, String categoryName) {
        this.id = id;
        this.parent = parent;
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", parent=" + parent +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

    /**
     * mappedBy
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
    private List<Category> children = new ArrayList<>();

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public enum ECategory {
        WINE,               // 와인
        WHISKY,             // 위스키
        COCKTAIL,           // 칵테일
        TRADITIONAL_LIQUOR, // 전통주
        NON_ALCOHOL,        // 논알콜
        MEAT,               // 육류
        SEAFOOD,            // 해산물
        BRAND_NEW,          // 신상품
        BEST_SELLER,        // 인기상품
        ALL
    }
}
