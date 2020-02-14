package com.chen.auth.server.controller;/**
 * Created by Administrator on 2019/9/9.
 */


import com.chen.auth.api.enums.StatusCode;
import com.chen.auth.api.response.BaseResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/9 14:32
 **/
@RestController
@RequestMapping("base")
public class BaseController extends AbstractController{

    @RequestMapping("info")
    public BaseResponse info(){
        log.info("--请求响应数据列表--");

        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            String strOne="程序员实战基地~SpringBoot前后端分离开发之用户身份认证实战(后端实现)";
            String strTwo="www.fightjava.com";
            String strThree="程序员实战基地!!";

            List<String> list= Lists.newLinkedList();
            list.add(strOne);
            list.add(strTwo);
            list.add(strThree);

            response.setData(list);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("redis/info")
    public BaseResponse redisInfo(){
        log.info("--请求响应redis数据列表--");

        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            String key="SpringBootUserAuthKey";
            String value="java程序员实战基地";
            stringRedisTemplate.opsForValue().set(key,value);


            response.setData(stringRedisTemplate.opsForValue().get(key));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

}