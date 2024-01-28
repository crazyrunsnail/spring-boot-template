package com.github.crazyrunsnail.template.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.crazyrunsnail.template.dto.ApiResponse;
import com.github.crazyrunsnail.template.mapper.UserMapper;
import com.github.crazyrunsnail.template.model.User;
import com.github.crazyrunsnail.template.util.JsonUtils;
import com.github.crazyrunsnail.template.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final static String TOKEN_HEADER = "Authorization";
    private final static String TOKEN_HEAD = "Bearer ";
    private final static String LOGIN_PATH = "/api/user/login";

    private final UserMapper userMapper;

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (LOGIN_PATH.equals(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(TOKEN_HEADER);
        if (authHeader == null || !authHeader.startsWith(TOKEN_HEAD)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authToken = authHeader.substring(TOKEN_HEAD.length()); // The part after "Bearer "


        String username = jwtUtils.getUserNameFromToken(authToken);

        if (!jwtUtils.validateToken(authToken, username)) {
            SecurityContextHolder.clearContext();
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            response.getOutputStream().write(JsonUtils.toString(ApiResponse.fail(10002, "Token无效或已过期"))
                    .getBytes(StandardCharsets.UTF_8));
            response.getOutputStream().flush();
            return;
        }

        User user = this.userMapper.selectByUsername(username);
        UserDetails userDetails = toUserDetails(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }


    private UserDetails toUserDetails(User user) {
        List<String> roles = JsonUtils.parse(user.getRolesArrayJson(), new TypeReference<List<String>>() {
        });
        List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


}
