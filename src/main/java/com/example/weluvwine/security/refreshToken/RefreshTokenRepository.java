package com.example.weluvwine.security.refreshToken;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(String memberId);

    void deleteByMemberId(String memberId);

}