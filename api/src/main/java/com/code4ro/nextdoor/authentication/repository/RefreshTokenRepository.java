package com.code4ro.nextdoor.authentication.repository;


import com.code4ro.nextdoor.authentication.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    List<RefreshToken> findAllByUserId(String userId);
}
