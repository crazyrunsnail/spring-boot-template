package com.github.crazyrunsnail.template.mappertests;

import com.github.crazyrunsnail.template.mapper.UserMapper;
import com.github.crazyrunsnail.template.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


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
}
