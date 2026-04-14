package com.htweb.api.repositories;

import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.repositories.BaseRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends BaseRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    boolean revokeTokenByTokenHash(String tokenHash);
}
