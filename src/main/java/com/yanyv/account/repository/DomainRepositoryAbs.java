package com.yanyv.account.repository;

import com.yanyv.account.entity.Account;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

public abstract class DomainRepositoryAbs<T, PK extends Serializable> implements DomainRepository<T,PK> {
    @Autowired
    private SessionFactory sessionFactory;
    protected String className = "";

    protected Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public void persist(T entity) {
        Session session = getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.persist(entity);
        tx.commit();
        session.close();
    }

    @Override
    public PK save(T entity) {
        Session session = getCurrentSession();
        Transaction tx = session.beginTransaction();
        PK pk = (PK) session.save(entity);
        tx.commit();
        session.close();
        return pk;
    }

    @Override
    public void saveOrUpdate(T entity) {
        Session session = getCurrentSession();

        entity = (T) session.merge(entity);
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(entity);
        tx.commit();
        session.close();
    }

    @Override
    public void delete(T t) {
        Session session = getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.delete(t);
        tx.commit();
        session.close();
    }

    @Override
    public void flush() {
        getCurrentSession().flush();
    }

}
