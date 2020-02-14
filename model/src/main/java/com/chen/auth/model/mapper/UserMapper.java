package com.chen.auth.model.mapper;

import com.chen.auth.model.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUserName(@Param("userName") String userName);

    User selectByUserId(@Param("userId") Integer userId);

    int updatePassword(@Param("userName") String userName, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);
}