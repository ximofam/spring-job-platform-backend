package com.htweb.api.filters;

import com.htweb.api.exceptions.http.UnauthorizedException;
import com.htweb.api.services.TokenService;
import com.htweb.api.utils.FilterUtils;
import com.htweb.api.utils.Utils;
import com.htweb.core.services.AuthorityService;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Qualifier("apiTokenService")
    private final TokenService tokenService;
    private final AuthorityService authorityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            FilterUtils.handleException(response, new UnauthorizedException("Missing or invalid Authorization header."));
            return;
        }

        try {
            String token = authHeader.substring(7);
            JWTClaimsSet claims = tokenService.verifyAndParseAccessToken(token);

            String username = claims.getSubject();
            List<String> roles = Utils.castToStringList(claims.getClaim("roles"));
            Set<GrantedAuthority> authorities = authorityService.getAuthorities(roles);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(username, null, authorities)
            );
        } catch (UnauthorizedException ex) {
            FilterUtils.handleException(response, ex);
            return;
        } catch (Exception ex) {
            FilterUtils.handleException(response, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith("/api/secure/");
    }
}
