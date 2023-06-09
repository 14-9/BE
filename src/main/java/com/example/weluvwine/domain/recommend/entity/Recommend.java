package com.example.weluvwine.domain.recommend.entity;

import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.wine.entity.Wine;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    private Wine wine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public Recommend(Wine wine, Member member){
        this.wine = wine;
        this.member = member;
    }

}
