package com.github.crazyrunsnail.template.mapper;

import com.github.crazyrunsnail.template.model.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;


@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    User selectByUsername(String username);

    Page<User> selectAllByCreatedAtPage(LocalDate gtLocalDate);
}