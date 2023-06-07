package com.joolove.core.domain.product;

import com.joolove.core.domain.BaseTimeStamp;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(schema = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class GoodsDetails extends BaseTimeStamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "goods_details_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(mappedBy = "goodsDetails")
    private Goods goods;

    @NotBlank
    private String name;

    @NotBlank
    private String engName;

    @NotBlank
    private String type;        // 주류 종류 (예: 와인, 위스키 등)

    private Short priceLevel;   // 가격대

    @Size(max = 1000)
    private String imageUrl;
    private String degree;      // 도수
    private String country;     // 생산국
    private String company;     // 제조사
    private String supplier;    // 공급사
    @Size(max = 1000)
    private String description;
    @Size(max = 1000)
    private String summary;
    private String color;

    private String opt1Name;    // aroma : 향 (예: "nuts,cocoa")
    private String opt1Value;
    private String opt2Name;    // balance : 알코올과 다른 성분의 균형. 높으면 알코올향이 쎄다. (1, 2, 3, 4, 5)
    private String opt2Value;
    private String opt3Name;    // body : 목 넘김. 높으면 중후해진다. (1, 2, 3, 4, 5)
    private String opt3Value;
    private String opt4Name;    // tannin : 떫기. 높으면 떫다. (1, 2, 3, 4, 5)
    private String opt4Value;
    private String opt5Name;    // sweetness : 당도. 높으면 단맛이 강하다. (1, 2, 3, 4, 5)
    private String opt5Value;
    private String opt6Name;    // acidity : 산도. 높으면 산미가 강하다. (1, 2, 3, 4, 5)
    private String opt6Value;
    private String opt7Name;    // soda : 탄산. 높으면 탄산이 강하다. (1, 2, 3, 4, 5)
    private String opt7Value;
    private String opt8Name;
    private String opt8Value;

    @Builder
    public GoodsDetails(UUID id, Goods goods, String name, String engName, String type, Short priceLevel, String imageUrl, String degree, String country, String company, String supplier, String description, String summary, String color, String opt1Name, String opt1Value, String opt2Name, String opt2Value, String opt3Name, String opt3Value, String opt4Name, String opt4Value, String opt5Name, String opt5Value, String opt6Name, String opt6Value, String opt7Name, String opt7Value, String opt8Name, String opt8Value) {
        this.id = id;
        this.goods = goods;
        this.name = name;
        this.engName = engName;
        this.type = type;
        this.priceLevel = priceLevel;
        this.imageUrl = imageUrl;
        this.degree = degree;
        this.country = country;
        this.company = company;
        this.supplier = supplier;
        this.description = description;
        this.summary = summary;
        this.color = color;
        this.opt1Name = opt1Name;
        this.opt1Value = opt1Value;
        this.opt2Name = opt2Name;
        this.opt2Value = opt2Value;
        this.opt3Name = opt3Name;
        this.opt3Value = opt3Value;
        this.opt4Name = opt4Name;
        this.opt4Value = opt4Value;
        this.opt5Name = opt5Name;
        this.opt5Value = opt5Value;
        this.opt6Name = opt6Name;
        this.opt6Value = opt6Value;
        this.opt7Name = opt7Name;
        this.opt7Value = opt7Value;
        this.opt8Name = opt8Name;
        this.opt8Value = opt8Value;
    }

    @Builder(builderClassName = "AlcoholBuilder", builderMethodName = "alcoholBuilder")
    public GoodsDetails(UUID id, Goods goods, String name, String engName, String type, String imageUrl,
                        String degree, String country, String company, String supplier,
                        String description, String summary, String color,
                        String opt1Value, String opt2Value, String opt3Value, String opt4Value,
                        String opt5Value, String opt6Value, String opt7Value) {
        this.id = id;
        this.goods = goods;
        this.name = name;
        this.engName = engName;
        this.type = type;
        this.imageUrl = imageUrl;
        this.degree = degree;
        this.country = country;
        this.company = company;
        this.supplier = supplier;
        this.description = description;
        this.summary = summary;
        this.color = color;
        this.opt1Name = "aroma";
        this.opt1Value = opt1Value;
        this.opt2Name = "balance";
        this.opt2Value = opt2Value;
        this.opt3Name = "body";
        this.opt3Value = opt3Value;
        this.opt4Name = "tannin";
        this.opt4Value = opt4Value;
        this.opt5Name = "sweetness";
        this.opt5Value = opt5Value;
        this.opt6Name = "acidity";
        this.opt6Value = opt6Value;
        this.opt7Name = "soda";
        this.opt7Value = opt7Value;
        this.opt8Name = null;
        this.opt8Value = null;
    }

}
