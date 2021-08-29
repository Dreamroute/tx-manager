package com.github.dreamroute.tx.manager.sample.mapper;

import com.github.dreamroute.mybatis.pro.service.mapper.BaseMapper;
import com.github.dreamroute.tx.manager.sample.domain.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User, Long> {

    @Select("select * from smart_user where id = #{id}")
    User getById(Long id);
}
