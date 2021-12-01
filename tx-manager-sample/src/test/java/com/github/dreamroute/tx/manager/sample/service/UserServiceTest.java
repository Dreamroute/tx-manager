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

    /**
     * 内外均开启事务，内部抛出异常，并且被catch，外部依然回滚，
     * 如果希望外层事务不回滚，那么内层事务使用NESTED
     * 全局配置和单个配置优先级测试：要让@Transactional优先于全局配置，需要开启@EnableTransactionManagement
     */
    @Test
    void catchInnerExceptionTest() {
        userService.insertCatchInnerException();
    }

    /**
     * 参考：
     * 1. https://yunlongn.github.io/2019/05/06/%E8%AE%B0%E4%B8%80%E6%AC%A1%E4%BA%8B%E5%8A%A1%E7%9A%84%E5%9D%91Transaction-rolled-back-because-it-has-been-marked-as-rollback-only/
     * 2. https://segmentfault.com/a/1190000013341344
     */

}
