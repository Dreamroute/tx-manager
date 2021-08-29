package com.github.dreamroute.tx.manager.sample.service;

import com.github.dreamroute.tx.manager.sample.domain.User;
import com.github.dreamroute.tx.manager.sample.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;

    @Test
    void insertTest() {
        userService.insert();
    }

    @Test
    void insertThrowExceptionTest() throws Exception {
        userService.insertThrowException();
    }

    /**
     * 在只读事务中执行insert操作
     */
    @Test
    void insertInReadOnlyTest() {
        userService.getReadOnly();
    }

    /**
     * 读取数据
     */
    @Test
    void readTest() {
        User user = userService.selectById(1L);
        System.err.println(user);
    }

    @Test
    void selectByIdOfMapper() {
        User u = userMapper.selectById(1L);
        System.err.println(u);
    }

    @Test
    void selectByIdOfService() {
        User user = userService.selectById(1L);
        System.err.println(user);
    }

}
