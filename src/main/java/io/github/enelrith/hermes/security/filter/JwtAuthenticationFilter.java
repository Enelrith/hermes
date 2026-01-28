package io.github.enelrith.hermes.security.filter;

import io.github.enelrith.hermes.security.service.CustomUserDetailsService;
import io.github.enelrith.hermes.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Extract JWT from Authorization header
        String token = getJWTFromRequest(request);

        // 2. If token exists and is valid
        if (token != null) {
            try {
                // 3. Extract username from token
                String username = jwtService.getUsernameFromToken(token);

                // 4. Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Validate token matches the user
                if (jwtService.validateAccessToken(token, userDetails)) {
                    // 6. Create authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 7. Set authentication in Spring Security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token is invalid, do nothing (user remains unauthenticated)
                logger.error("Could not set user authentication", e);
            }
        }

        // 8. Continue filter chain
        filterChain.doFilter(request, response);
    }

    // Extract token from "Authorization: Bearer <token>" header
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Remove "Bearer " prefix
        }

        return null;
    }
}
