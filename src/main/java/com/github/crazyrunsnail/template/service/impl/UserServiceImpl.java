package com.github.crazyrunsnail.template.service.impl;

import com.github.crazyrunsnail.template.mapper.UserMapper;
import com.github.crazyrunsnail.template.model.User;
import com.github.crazyrunsnail.template.service.UserService;
import com.github.crazyrunsnail.template.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(passwordEncoder.matches(password, user.getPassword()),
                "密码不正确");

        User updateUser = User.builder().id(user.getId()).loggedInAt(LocalDateTime.now())
                .build();
        userMapper.updateByPrimaryKeySelective(updateUser);

        return jwtUtils.generateToken(user.getUsername());
    }
}
