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

    /**
     * 运行时异常
     */
    @Test
    void insertThrowRuntimeExceptionTest() {
        assertThrows(IllegalArgumentException.class, userService::insert);
    }

    /**
     * 受检异常
     */
    @Test
    void insertThrowCheckedExceptionTest() {
        assertThrows(IOException.class, userService::insertThrowException);
    }

    /**
     * 在只读事务中执行insert操作
     */
    @Test
    void insertInReadOnlyTest() {
        assertThrows(TransientDataAccessResourceException.class, userService::getReadOnly);
    }

    /**
     * 与@Transactional同时存在
     */
    @Test
    void withTransactionalTest() {
        assertThrows(TransientDataAccessResourceException.class, userService::withTransactional);
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
