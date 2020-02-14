package com.chen.auth.server.service;/**
 * Created by Administrator on 2019/9/9.
 */

import com.chen.auth.api.enums.StatusCode;
import com.chen.auth.api.response.BaseResponse;
import com.chen.auth.model.entity.AuthToken;
import com.chen.auth.model.entity.AuthTokenModel;
import com.chen.auth.model.entity.Log;
import com.chen.auth.model.entity.User;
import com.chen.auth.model.mapper.AuthTokenMapper;
import com.chen.auth.model.mapper.UserMapper;
import com.chen.auth.server.dto.AccessTokenDto;
import com.chen.auth.server.dto.UpdatePsdDto;
import com.chen.auth.server.enums.Constant;
import com.chen.auth.server.utils.EncryptUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * token+数据库~service
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/9 17:23
 **/
@Service
public class DataBaseService {

    private static final Logger log= LoggerFactory.getLogger(DataBaseService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthTokenMapper authTokenMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonService commonService;



    //登录认证并创建token
    @Transactional(rollbackFor = Exception.class)
    public AuthTokenModel authAndCreateToken(String userName, String password) throws Exception{
        User user=userService.authUser(userName,password);
        if (user!=null){
            //失效以前那些仍然在使用的access token
            authTokenMapper.invalidateTokenByUser(user.getId());

            //创建token
            Long timestamp=System.currentTimeMillis();
            AccessTokenDto tokenDto=new AccessTokenDto(user.getId(),userName,timestamp,Constant.snowFlake.nextId().toString(),Constant.ACCESS_TOKEN_EXPIRE);
            String jsonStr=objectMapper.writeValueAsString(tokenDto);
            log.info("----json格式的access token字符串：{}",jsonStr);

            String accessToken=EncryptUtil.aesEncrypt(jsonStr,Constant.TOKEN_AUTH_KEY);

            AuthToken authToken=new AuthToken();
            authToken.setUserId(user.getId());
            authToken.setAccessToken(accessToken);
            authToken.setAccessExpire(Constant.ACCESS_TOKEN_EXPIRE);
            authToken.setTokenTimestamp(timestamp);
            authToken.setCreateTime(DateTime.now().toDate());
            authTokenMapper.insertSelective(authToken);

            log.info("--token+数据库用户认证成功，成功生成accessToken--");
            AuthTokenModel tokenModel=new AuthTokenModel(accessToken,Constant.ACCESS_TOKEN_EXPIRE);
            return tokenModel;
        }
        return null;
    }

    //验证-解析token
    public BaseResponse validateToken(final String accessToken){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            if (StringUtils.isBlank(accessToken)){
                return new BaseResponse(StatusCode.AccessTokenNotBlank);
            }

            //验证这个token是否存在
            AuthToken authToken=authTokenMapper.selectByAccessToken(accessToken);
            if (authToken==null){
                return new BaseResponse(StatusCode.AccessTokenNotExist);
            }

            AccessTokenDto dto;
            //为了防止token的伪造，会主动额外做异步操作-解析token
            try {
                //解析失败直接抛异常不做多余判断
                dto=parseAccessToken(accessToken);
            }catch (Exception e){
                return new BaseResponse(StatusCode.AccessTokenInvalidate);
            }

            if (dto!=null){
                //验证token是否过期  用entity对象字段，防止修改数据库字段值
                if (System.currentTimeMillis() - dto.getTimestamp() > dto.getExpire()){
                    this.invalidateByAccessToken(accessToken);
                    return new BaseResponse(StatusCode.TokenValidateExpireToken);
                }
            }


        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //失效access token
    public void invalidateByAccessToken(final String accessToken){
        if (StringUtils.isNotBlank(accessToken)){
            //修改数据库isActive为0(失效)
            authTokenMapper.invalidateByToken(accessToken);
        }
    }

    //解密accessToken
    public AccessTokenDto parseAccessToken(final String accessToken) throws Exception{
        String jsonStr=EncryptUtil.aesDecrypt(accessToken,Constant.TOKEN_AUTH_KEY);
        //json转对象
        return objectMapper.readValue(jsonStr,AccessTokenDto.class);
    }

    //修改密码
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(final String accessToken, final UpdatePsdDto dto)throws Exception{
        if (StringUtils.isNotBlank(accessToken)){
            //解析access token，获取用户信息
            AccessTokenDto tokenDto=parseAccessToken(accessToken);

            //修改密码
            int res=userMapper.updatePassword(tokenDto.getUserName(),dto.getOldPassword(),dto.getNewPassword());
            if (res<=0){
                throw new RuntimeException("修改密码失败~旧密码不正确。。。！");
            }

            //失效掉以前有效的token
            authTokenMapper.invalidateTokenByUser(tokenDto.getUserId());
        }
    }

    //实际业务模块操作-新增用户
    public void saveUser(final String accessToken,User user) throws Exception{
        //解析token 获取用户信息
        AccessTokenDto tokenDto=this.parseAccessToken(accessToken);

        //新增用户
        user.setCreateTime(DateTime.now().toDate());
        userMapper.insertSelective(user);

        //记录日志
        Log entity=new Log(null,tokenDto.getUserId(),tokenDto.getUserName(),new Date(),"新增用户");
        commonService.insertLog(entity);
    }

}
































