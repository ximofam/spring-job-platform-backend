package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.CandidateProfileRepository;
import com.htweb.core.pojo.CandidateProfile;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository("apiCandidateProfileRepository")
public class CandidateProfileRepositoryImpl
        extends BaseRepositoryImpl<CandidateProfile, Long>
        implements CandidateProfileRepository {
    public CandidateProfileRepositoryImpl() {
        super(CandidateProfile.class);
    }

    @Override
    public Optional<CandidateProfile> findById(Long userId) {
        Session session = this.getCurrentSession();
        session.createQuery("""
                        FROM CandidateProfile p
                        LEFT JOIN FETCH p.educations
                        WHERE p.userId = :userId
                        """, CandidateProfile.class)
                .setParameter("userId", userId)
                .uniqueResult();

        CandidateProfile profile = session.createQuery("""
                        FROM CandidateProfile p
                        LEFT JOIN FETCH p.experiences
                        WHERE p.userId = :userId
                        """, CandidateProfile.class)
                .setParameter("userId", userId)
                .uniqueResult();

        return Optional.ofNullable(profile);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<CandidateProfile> findByUserId(Long userId) {
        String hql = "FROM CandidateProfile p WHERE p.userId = :userId";
        
        return getCurrentSession().createQuery(hql, CandidateProfile.class)
                .setParameter("userId", userId)
                .uniqueResultOptional();
    }
}
