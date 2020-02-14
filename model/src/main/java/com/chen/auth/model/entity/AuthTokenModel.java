package com.chen.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenModel implements Serializable{

    //token
    private String accessToken;
    //失效时间
    private Long accessExpire;

}