package com.github.dreamroute.tx.manager.sample.domain;

import com.github.dreamroute.mybatis.pro.core.annotations.Id;
import com.github.dreamroute.mybatis.pro.core.annotations.Table;
import lombok.Data;

@Data
@Table("smart_addr")
public class Addr {
    @Id
    private Long id;
    private String name;
    private Long userId;
}
