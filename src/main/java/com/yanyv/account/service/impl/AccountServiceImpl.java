package com.yanyv.account.service.impl;

import com.yanyv.account.entity.Account;
import com.yanyv.account.repository.AccountRepository;
import com.yanyv.account.service.AccountService;
import com.yanyv.account.util.AssertUtil;
import com.yanyv.account.util.Sha256Util;
import com.yanyv.account.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository repository;
    @Override
    public Account login(String email, String password) {
        AssertUtil.isTrue(StringUtil.isEmpty(email), "邮箱不能为空");
        AssertUtil.isTrue(StringUtil.isNotEmail(email), "邮箱格式错误");
        AssertUtil.isTrue(StringUtil.isEmpty(password), "密码不能为空");
        Account account = findByEmail(email);
        AssertUtil.isTrue(null == account, "用户不存在");
        AssertUtil.isTrue(!Sha256Util.get(email + password).equals(account.getPasswordSha256()), "密码错误");
        return account;
    }

    @Override
    public Account findByEmail(String email) {
        return repository.queryByEmail(email);
    }

    @Override
    public Long register(String email, String password, String name) {
        Account account = new Account();
        account.setEmail(email);
        account.setPasswordSha256(Sha256Util.get(email + password));
        account.setName(name);
        account.setDate(new Date());

        return repository.save(account);
    }
}
