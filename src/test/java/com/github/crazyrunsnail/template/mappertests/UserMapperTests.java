package com.github.crazyrunsnail.template.mappertests;

import com.github.crazyrunsnail.template.mapper.UserMapper;
import com.github.crazyrunsnail.template.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@SpringBootTest
@Transactional
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectNotFoundRecord() {
        User user = userMapper.selectByPrimaryKey(0L);
        Assertions.assertNull(user);
    }


    @Test
    public void testSelectPage() {
        PageHelper.startPage(1, 10);
        Page<User> userPage = userMapper.selectAllByCreatedAtPage(LocalDate.of(2024, 1, 28));
        Assertions.assertNotNull(userPage);
        Assertions.assertTrue(userPage.getTotal()>=0);
    }
}
