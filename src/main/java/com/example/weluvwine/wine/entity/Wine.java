package com.example.weluvwine.wine.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String grape;
    @Column(nullable = false)
    private Long vintage;
    @Column(nullable = false)
    private String winery;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String information;
    @Column(nullable = false)
    private long recommendCount;

    public void setRecommendCount(long recommendCount){
        this.recommendCount = recommendCount;
    }
}
