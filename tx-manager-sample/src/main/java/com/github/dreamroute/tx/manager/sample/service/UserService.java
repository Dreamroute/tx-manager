package com.github.dreamroute.tx.manager.sample.service;

import com.github.dreamroute.tx.manager.sample.domain.User;

public interface UserService {
    void insert();

    void insertThrowException() throws Exception;

    void getReadOnly();

    User selectById(Long id);

    void withTransactional();

    void insertCatchInnerException();

}
