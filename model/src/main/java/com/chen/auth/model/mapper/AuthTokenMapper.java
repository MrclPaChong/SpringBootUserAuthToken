package com.chen.auth.model.mapper;


import com.chen.auth.model.entity.AuthToken;
import org.apache.ibatis.annotations.Param;

public interface AuthTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthToken record);

    int insertSelective(AuthToken record);

    AuthToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthToken record);

    int updateByPrimaryKey(AuthToken record);

    void invalidateTokenByUser(@Param("userId") Integer userId);

    AuthToken selectByAccessToken(@Param("accessToken") String accessToken);

    int invalidateByToken(@Param("accessToken") String accessToken);

    int deleteUnactiveToken();
}