package com.github.crazyrunsnail.template.filter;

import com.github.crazyrunsnail.template.dto.ApiResponse;
import com.github.crazyrunsnail.template.dto.user.UserDetailsDTO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final static String TOKEN_HEADER = "Authorization";
    private final static String TOKEN_HEAD = "Bearer ";
    private final static String LOGIN_PATH = "/api/user/login";

    private final static String X_TOKEN_HEADER = "x-token";


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

        String authHeader = Optional.ofNullable(request.getHeader(TOKEN_HEADER)).orElse(request.getHeader(X_TOKEN_HEADER));
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String authToken = authHeader.replace(TOKEN_HEAD, ""); // The part after "Bearer "

        String username = jwtUtils.getUserNameFromToken(authToken);
        if (!jwtUtils.validateToken(authToken, username)) {
            SecurityContextHolder.clearContext();
            writeToResponse(response, 10002, "Token无效或已过期");
            return;
        }

        User user;
        try {
            user = this.userMapper.selectByUsername(username);
        } catch (Exception e) {
            log.error("userMapper.selectByUsername error: {}", e.getMessage(), e);
            writeToResponse(response, 500, "服务器异常：数据库查询失败");
            return;
        }

        if (Objects.isNull(user)) {
            SecurityContextHolder.clearContext();
            writeToResponse(response, 10002, "Token已过时失效");
            return;
        }

        UserDetailsDTO userDetails = toUserDetails(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }

    private void writeToResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.getOutputStream().write(JsonUtils.toString(ApiResponse.fail(code, message))
                .getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }


    private UserDetailsDTO toUserDetails(User user) {
        UserDetailsDTO userDetails = new UserDetailsDTO();
        BeanUtils.copyProperties(user, userDetails);
        return userDetails;
    }


}
