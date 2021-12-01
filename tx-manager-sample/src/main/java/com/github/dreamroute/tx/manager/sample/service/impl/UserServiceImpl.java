package com.github.dreamroute.tx.manager.sample.service.impl;

import com.github.dreamroute.tx.manager.sample.domain.Addr;
import com.github.dreamroute.tx.manager.sample.domain.User;
import com.github.dreamroute.tx.manager.sample.mapper.UserMapper;
import com.github.dreamroute.tx.manager.sample.service.AddrService;
import com.github.dreamroute.tx.manager.sample.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final AddrService addrService;

    @Override
    public void insert() {
        User user = new User();
        user.setName("yzw");
        user.setPassword("123");
        userMapper.insert(user);
        Addr addr = new Addr();
        addr.setName("天府三街");
        addr.setUserId(user.getId());
        addrService.insert(addr);
        throw new IllegalArgumentException();
    }

    @Override
    public void insertThrowException() throws Exception{
        User user = new User();
        user.setName("yzw");
        user.setPassword("123");
        userMapper.insert(user);
        throw new IOException("");
    }

    @Override
    public void getReadOnly() {
        User user = new User();
        user.setName("yzw");
        user.setPassword("123");
        userMapper.insert(user);
    }

    @Override
    @Transactional(readOnly = true)
    public void withTransactional() {
        User user = new User();
        user.setName("yzw");
        user.setPassword("123");
        userMapper.insert(user);
    }

    @Override
    public User selectById(Long id) {
        return userMapper.getById(id);
    }

    @Override
    public void insertCatchInnerException() {
        User user = new User();
        user.setName("yzw");
        user.setPassword("123");
        userMapper.insert(user);

        Addr addr = new Addr();
        addr.setName("天府三街");
        addr.setUserId(user.getId());
        try {
            addrService.insertThrowException(addr);
        } catch (Exception e) {
            System.err.println("insert addr抛出异常，并且被catch了");
        }
    }
}
