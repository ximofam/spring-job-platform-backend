package com.htweb.core.services.impl;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userAuthRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Not found user: %s", username)));
        
        Set<GrantedAuthority> authorities = authorityService.getAuthorities(user.getRoles());

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPasswordHash(), authorities);
    }
}
