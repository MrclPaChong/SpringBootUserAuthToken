package com.chen.auth.server.dto;/**
 * Created by Administrator on 2019/9/9.
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * accessToken的内部字段信息
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/9 20:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenDto implements Serializable{

    private Integer userId;

    private String userName;

    private Long timestamp;

    private String randomStr;

    private Long expire;

}




















