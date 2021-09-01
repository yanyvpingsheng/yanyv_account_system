package com.yanyv.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password_sha256")
    private String passwordSha256;

    @Column(name = "name")
    private String name;

    // 注册日期
    @Column(name = "date")
    private Date date;
}
