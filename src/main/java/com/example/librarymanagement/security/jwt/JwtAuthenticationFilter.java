package com.example.librarymanagement.security.jwt;

import com.example.librarymanagement.service.CustomUserDetailService;
import com.example.librarymanagement.service.JwtBlacklistService;
import com.example.librarymanagement.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    CustomUserDetailService customUserDetailService;

    JwtTokenProvider tokenProvider;

    JwtBlacklistService tokenService;

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = JwtUtil.extractTokenFromRequest(request);
            if (accessToken == null ||
                !tokenProvider.validateToken(accessToken) ||
                !tokenProvider.isAccessToken(accessToken) ||
                    tokenService.isTokenBlocked(accessToken)) {
                filterChain.doFilter(request, response);

                return;
            }

            String userId = tokenProvider.extractSubjectFromJwt(accessToken);
            String cardNumber = (userId == null) ? tokenProvider.extractClaimCardNumber(accessToken) : null;

            if (userId != null) {
                UserDetails userDetails = customUserDetailService.loadUserByUserId(userId);
                setAuthentication(userDetails, request);
            } else if (cardNumber != null) {
                UserDetails userDetails = customUserDetailService.loadUserByCartNumber(cardNumber);
                setAuthentication(userDetails, request);
            }
        } catch (UsernameNotFoundException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found: " + e.getMessage());
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token: " + e.getMessage());
            return;
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
