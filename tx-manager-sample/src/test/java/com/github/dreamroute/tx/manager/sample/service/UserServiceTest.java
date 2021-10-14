package com.github.dreamroute.tx.manager.sample.service;

import com.github.dreamroute.tx.manager.sample.domain.User;
import com.github.dreamroute.tx.manager.sample.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.TransientDataAccessResourceException;

import javax.annotation.Resource;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;

    @Test
    void insertTest() {
        assertThrows(IllegalArgumentException.class, userService::insert);
    }

    @Test
    void insertThrowExceptionTest() {
        assertThrows(IOException.class, userService::insertThrowException);
    }

    /**
     * 在只读事务中执行insert操作
     */
    @Test
    void insertInReadOnlyTest() {
        assertThrows(TransientDataAccessResourceException.class, userService::getReadOnly);
    }

    @Test
    void selectByIdOfMapper() {
        User user = userMapper.selectById(1L, "id", "name");
        assertEquals("w.dehai", user.getName());
    }

    @Test
    void selectByIdOfService() {
        User user = userService.selectById(1L);
        assertEquals("w.dehai", user.getName());
    }

}
