package com.example.mall.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
@Entity
@Data
public class UserToken {

    @Id
    private Long userId;

    private String token;

    private Date updateTime;

    private Date expireTime;
}
