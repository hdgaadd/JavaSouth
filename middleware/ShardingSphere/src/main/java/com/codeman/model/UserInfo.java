package com.codeman.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private Long userId;

    private String userName;

    private String account;

    private String password;
}