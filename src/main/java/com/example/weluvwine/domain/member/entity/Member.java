package com.example.weluvwine.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String memberId;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;
//    @Column(nullable = false)
//    private String email;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "email_code_id")
//    private EmailCode emailCode;
//
//    private boolean emailVerified = false;



    public Member(String memberId, String password, String nickname) {
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;

    }



}
