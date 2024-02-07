package com.github.crazyrunsnail.template.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.crazyrunsnail.template.dto.user.UserDTO;
import com.github.crazyrunsnail.template.dto.user.UserSearchParam;
import com.github.crazyrunsnail.template.mapper.UserMapper;
import com.github.crazyrunsnail.template.model.User;
import com.github.crazyrunsnail.template.service.UserService;
import com.github.crazyrunsnail.template.util.JsonUtils;
import com.github.crazyrunsnail.template.util.JwtUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public UserDTO getById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return UserDTO.builder().id(user.getId()).name(user.getName()).username(user.getUsername())
                .roles(JsonUtils.parse(Optional.ofNullable(user.getRolesArrayJson()).orElse("[]"),
                        new TypeReference<List<String>>() {
                        })).build();
    }

    @Override
    public Page<User> getBySearchParam(UserSearchParam param) {
        PageHelper.startPage(param.getPage(), param.getPer());
        return userMapper.selectBySearchParam(param);
    }
}
