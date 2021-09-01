package com.yanyv.account.repository.impl;

import com.yanyv.account.entity.Account;
import com.yanyv.account.repository.AccountRepository;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountRepositoryImpl extends AccountRepository {
    @Override
    public Account queryByEmail(String email) {
        Session session = getCurrentSession();
        Query query = session.createQuery("from Account where email='" + email + "'");
        List<Account> list = query.list();
        if (list.size() != 0)
            return list.get(0);
        else
            return null;
    }

    @Override
    public Account load(Long id) {
        return (Account) getCurrentSession().load(Account.class, id);
    }

    @Override
    public Account get(Long id) {
        return (Account) getCurrentSession().get(Account.class, id);
    }
}
