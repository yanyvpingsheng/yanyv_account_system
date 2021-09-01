package com.yanyv.account.repository;


import com.yanyv.account.entity.Account;

public abstract class AccountRepository extends DomainRepositoryAbs<Account, Long> {
    public abstract Account queryByEmail(String email);
}
