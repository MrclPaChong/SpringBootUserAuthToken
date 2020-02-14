package com.chen.auth.server.controller;/**
 * Created by Administrator on 2019/8/30.
 */

import com.chen.auth.api.enums.StatusCode;
import com.chen.auth.api.response.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:chen (SteadyJack)
 * @Date: 2019/8/30 9:57
 **/
@Controller
@RequestMapping("error")
public class ErrorController {

    @RequestMapping(value = "500",method = RequestMethod.GET)
    public String error500(){
        return "500";
    }

    @RequestMapping(value = "404",method = RequestMethod.GET)
    public String error404(){
        return "404";
    }

    @RequestMapping("unauth")
    @ResponseBody
    public BaseResponse unauth(){
        return new BaseResponse(StatusCode.AccessSessionNotExist);
    }
}