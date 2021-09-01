package com.yanyv.account.service;

import com.yanyv.account.entity.Account;
import org.springframework.stereotype.Service;

public interface AccountService extends MyService<Account> {
    /**
     * 用户登录方法
     * @param email
     * @param password
     * @return
     */
    Account login(String email, String password);

    /**
     * 根据邮箱查询用户
     * @param email
     * @return
     */
    Account findByEmail(String email);

    /**
     * 用户注册方法
     * @param email
     * @param password
     * @return
     */
    Long register(String email, String password, String name);
}
