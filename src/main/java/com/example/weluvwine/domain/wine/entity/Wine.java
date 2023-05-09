package com.example.weluvwine.domain.wine.entity;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String grape;
    @Column(nullable = false)
    private String vintage;
    @Column(nullable = false)
    private String winery;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WineType type;
    @Column(nullable = false,columnDefinition = "text")
    private String information;
    @Column(nullable = false)
    @ColumnDefault("0")
    private long recommendCount;

    public void setRecommendCount(long recommendCount){
        this.recommendCount = recommendCount;
    }
}
