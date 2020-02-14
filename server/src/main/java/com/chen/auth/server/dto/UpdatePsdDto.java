package com.chen.auth.server.dto;/**
 * Created by Administrator on 2019/9/10.
 */

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/10 9:33
 **/
@Data
public class UpdatePsdDto implements Serializable{

    @NotBlank(message = "旧密码不能为空！")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空！")
    private String newPassword;

}