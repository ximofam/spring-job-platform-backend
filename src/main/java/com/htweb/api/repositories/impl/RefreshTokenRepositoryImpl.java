package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.RefreshTokenRepository;
import com.htweb.core.pojo.RefreshToken;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("apiRefreshTokenRepository")
public class RefreshTokenRepositoryImpl extends BaseRepositoryImpl<RefreshToken, Long> implements RefreshTokenRepository {
    public RefreshTokenRepositoryImpl() {
        super(RefreshToken.class);
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        Session session = this.getCurrentSession();
        RefreshToken refreshToken = session.createQuery(
                        "FROM RefreshToken rt JOIN FETCH rt.user WHERE rt.tokenHash = :tokenHash",
                        RefreshToken.class)
                .setParameter("tokenHash", tokenHash)
                .getSingleResultOrNull();

        return Optional.ofNullable(refreshToken);
    }

    @Override
    public boolean revokeTokenByTokenHash(String tokenHash) {
        Session session = this.getCurrentSession();

        int rowAffect = session.createMutationQuery("UPDATE RefreshToken rt SET rt.isActive = false WHERE rt.tokenHash = :tokenHash")
                .setParameter("tokenHash", tokenHash)
                .executeUpdate();
        
        return rowAffect > 0;
    }
}
