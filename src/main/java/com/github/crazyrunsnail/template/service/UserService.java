package com.github.crazyrunsnail.template.service;

import com.github.crazyrunsnail.template.dto.user.UserDTO;
import com.github.crazyrunsnail.template.dto.user.UserSearchParam;
import com.github.crazyrunsnail.template.model.User;
import com.github.pagehelper.Page;

public interface UserService {

    String login(String username, String password);


    UserDTO getById(Long id);


    Page<User> getBySearchParam(UserSearchParam param);

}
