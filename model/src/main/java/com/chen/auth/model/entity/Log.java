package com.chen.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private Integer id;

    private Integer userId;

    private String userName;

    private Date createTime;

    private String memo;

}