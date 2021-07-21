package com.github.dreamroute.tx.manager.sample.service;

public interface UserService {
    void insert();

    void insertThrowException() throws Exception;

    void getReadOnly();
}
