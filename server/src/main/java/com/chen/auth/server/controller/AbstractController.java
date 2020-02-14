package com.chen.auth.server.controller;/**
 * Created by Administrator on 2019/9/9.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * 封装日志
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/9 14:43
 **/
@Controller
public class AbstractController {

    protected final Logger log= LoggerFactory.getLogger(getClass());

}