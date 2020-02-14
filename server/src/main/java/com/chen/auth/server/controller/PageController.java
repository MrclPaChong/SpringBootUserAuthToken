package com.chen.auth.server.controller;/**
 * Created by Administrator on 2019/9/9.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/9 14:33
 **/
@Controller
@RequestMapping("page")
public class PageController extends AbstractController{

    @RequestMapping("info")
    public String info(ModelMap modelMap){
        log.info("--请求响应跳转页面--");

        modelMap.put("code","http://www.fightjava.com");
        return "page";
    }

}