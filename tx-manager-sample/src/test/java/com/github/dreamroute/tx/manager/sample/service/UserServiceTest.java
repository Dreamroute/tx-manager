package com.github.dreamroute.tx.manager.sample.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

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

}
