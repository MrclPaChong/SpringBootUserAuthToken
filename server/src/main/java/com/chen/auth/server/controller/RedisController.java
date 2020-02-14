package com.chen.auth.server.controller;/**
 * Created by Administrator on 2019/9/9.
 */

import com.chen.auth.api.enums.StatusCode;
import com.chen.auth.api.response.BaseResponse;
import com.chen.auth.model.entity.User;
import com.chen.auth.server.dto.UpdatePsdDto;
import com.chen.auth.server.service.DataBaseService;
import com.chen.auth.server.service.RedisService;
import com.chen.auth.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * token+redis 认证模式
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/9 17:20
 **/
@RestController
@RequestMapping("redis")
public class RedisController extends AbstractController{

    @Autowired
    private RedisService redisService;


    //用户登录
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public BaseResponse login(@RequestParam String userName,@RequestParam String password){
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
            return new BaseResponse(StatusCode.UserNamePasswordNotBlank);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            response.setData(redisService.authAndCreateToken(userName,password));

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //访问需要被授权的资源
    @RequestMapping(value = "token/auth",method = RequestMethod.GET)
    public BaseResponse tokenAuth(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            String info="redis+token~成功访问需要被拦截的链接/资源";
            response.setData(info);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //访问不需要被授权的资源
    @RequestMapping(value = "token/unauth",method = RequestMethod.GET)
    public BaseResponse tokenUnauth(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            String info="redis+token~成功访问不需要被拦截的链接/资源";
            response.setData(info);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //修改密码
    @RequestMapping(value = "token/password/update",method = RequestMethod.POST)
    public BaseResponse updatePassword(@RequestHeader String accessToken, @RequestBody @Validated UpdatePsdDto dto, BindingResult bindingResult){
        log.info("--token+redis~修改密码--");

        if (StringUtils.isBlank(accessToken)){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        String res=ValidatorUtil.checkResult(bindingResult);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            redisService.updatePassword(accessToken,dto);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //退出注销登录~前端需要清除token并重新进行登录
    @RequestMapping(value = "token/logout",method = RequestMethod.GET)
    public BaseResponse logout(@RequestHeader String accessToken){
        if (StringUtils.isBlank(accessToken)){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            redisService.invalidateByAccessToken(accessToken);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


}














































