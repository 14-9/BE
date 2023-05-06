package com.example.weluvwine.security.refreshToken;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String refreshToken;

    @NotBlank
    private String memberId;

    public RefreshToken(String tokenDto,  String memberId) {
        this.refreshToken = tokenDto;
        this.memberId = memberId;
    }

    public RefreshToken updateToken(String tokenDto) {
        this.refreshToken = tokenDto;
        return this;
    }

}