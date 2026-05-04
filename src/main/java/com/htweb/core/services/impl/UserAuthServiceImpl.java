package com.htweb.core.services.impl;

import com.htweb.core.helpers.security.CustomUserDetails;
import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import com.htweb.core.services.AuthorityService;
import com.htweb.core.services.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private final UserAuthRepository userAuthRepository;
    private final AuthorityService authorityService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userAuthRepository.findUserByUsername(usernameOrEmail)
                .or(() -> userAuthRepository.findUserByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException(usernameOrEmail));

        Set<GrantedAuthority> authorities = authorityService.getAuthorities(user.getRoles());

        return new CustomUserDetails(user, authorities);
    }
}
