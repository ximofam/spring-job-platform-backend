package com.htweb.api.repositories.impl;

import com.htweb.api.repositories.CandidateCvRepository;
import com.htweb.core.pojo.CandidateCv;
import com.htweb.core.repositories.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("apiCandidateCvRepository")
public class CandidateCvRepositoryImpl extends BaseRepositoryImpl<CandidateCv, Long> implements CandidateCvRepository {
    public CandidateCvRepositoryImpl() {
        super(CandidateCv.class);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<CandidateCv> findByUserId(Long userId) {
        String hql = "FROM CandidateCv cv WHERE cv.candidateProfile.id = :userId";

        return getCurrentSession().createQuery(hql, CandidateCv.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
