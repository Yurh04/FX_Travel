package org.fxtravel.fxspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import org.fxtravel.fxspringboot.pojo.entities.User;

public interface UserMapper  extends BaseMapper<User> {
    //插入用户
    @Insert("INSERT INTO user (email, password, verified, role) " +
            "VALUES (#{email}, #{password}, false, 'REGULAR')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer insertUser(User user);


    //根据邮箱查询用户
    @Select("select * from user where email = #{email}")
    public User getUserByEmail(String email);

    //查看用户邮箱是否被注册
    default boolean existsByEmail(String email) {
        return getUserByEmail(email) != null;
    }
}
