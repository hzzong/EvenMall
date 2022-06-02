package com.example.mall.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String nickName;

    private String loginName;

    private String password ;

    private String introduceSign;

    private Byte isDeleted;

    private Byte lockedFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
