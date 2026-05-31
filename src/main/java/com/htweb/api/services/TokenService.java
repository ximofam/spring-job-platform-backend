package com.htweb.api.services;

import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.pojo.User;

public interface TokenService {

    String generateRefreshToken(User user);

    RefreshToken verifyAndGetRefreshToken(String rawToken);

    void revokeRefreshToken(Long tokenId);
}
