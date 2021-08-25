package com.github.dreamroute.tx.manager.sample.domain;

import com.github.dreamroute.mybatis.pro.core.annotations.Id;
import com.github.dreamroute.mybatis.pro.core.annotations.Table;
import com.github.dreamroute.mybatis.pro.core.annotations.Transient;
import lombok.Data;

@Data
@Table("test")
public class User {
    @Id
    private Long id;
    private String name;
    private String password;
    private Long version;
    private String phoneNo;
    @Transient
    private String addrInfo;

}
