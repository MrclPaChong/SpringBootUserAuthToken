package com.chen.auth.server.utils;/**
 * Created by Administrator on 2019/9/10.
 */

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.function.Consumer;

/**
 * 统一的验参工具
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/10 9:36
 **/
public class ValidatorUtil {

    //统一处理加注解后校验的结果 package com.chen.auth.server.dto; UpdatePsdDto类中 字段  @NotBlank(message = "旧密码不能为空！")
    public static String checkResult(BindingResult result){
        //StringBuffer值(123)通过append追加值(123456),执行效率高线程不安全 https://blog.csdn.net/csxypr/article/details/92378336
        StringBuilder sb=new StringBuilder("");

        if (result!=null && result.hasErrors()){
            List<ObjectError> errors=result.getAllErrors();
            /*for (ObjectError error:errors){
                sb.append(error.getDefaultMessage()).append("\n");
            }*/

            //java8 stream的写法
            errors.forEach(error -> sb.append(error.getDefaultMessage()).append("\n"));

        }
        return sb.toString();
    }


}