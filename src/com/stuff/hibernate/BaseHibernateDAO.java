package com.stuff.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.stuff.hibernate.QueryBuilder.PropertyClause;


/**
 * Data access object (DAO) for domain model
 * 
 * @author MyEclipse Persistence Tools
 */
public abstract class BaseHibernateDAO<T>{

	Logger log = Logger.getLogger(BaseHibernateDAO.class);

	public BaseHibernateDAO() {
	}

	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}

	public void rollback() {
		try {
			getSession().getTransaction().rollback();
		} catch (Throwable ex) {
			log.error("Rollback error!");
			log.error(ex.toString());
		}
	}

	public T saveEntity(T entity) {
		log.debug("saving " + entity.getClass().getSimpleName() + " instance");
		try {
			Session session = getSession();
			session.beginTransaction();
			session.save(entity);
			session.getTransaction().commit();
			log.debug("save successful");
			return entity;
		} catch (Throwable re) {
			log.error("save failed", re);
			rollback();
			return null;
		}

	}

	public boolean deleteEntity(T entity) {
		if(entity == null)
			return false;
		log.debug("deleting " + entity.getClass().getSimpleName() + " instance");
		try {
			Session session = getSession();
			session.beginTransaction();
			session.delete(entity);
			session.getTransaction().commit();
			log.debug("delete successful");
			return true;
		} catch (Throwable re) {
			log.error("delete failed", re);
			rollback();
			return false;
		}

	}

	public T getEntityByID(Class<T> clazz, Long id) {
		return (T) getSession().get(clazz, id);
	}

	public T mergeEntity(T detachedInstance) {
		log.debug("merging " + detachedInstance.getClass().getSimpleName()
				+ " instance");
		try {
			Session session = getSession();
			session.beginTransaction();
			session.update(detachedInstance);
			session.getTransaction().commit();
			log.debug("merge successful");
			return detachedInstance;
		} catch (Throwable re) {
			log.error("merge failed", re);
			rollback();
			return null;
		}
	}

	public List<T> queryListByProperty(String prop,	String operator, Object value) {
		QueryBuilder qb = getQueryBuilder();
		qb.addPropertyClause(PropertyClause.valueOf(prop, operator, value));
		return qb.getList();
	}

	protected abstract Class<T> getEntityClazz();
	
	public QueryBuilder getQueryBuilder()
	{
		return new QueryBuilder(getEntityClazz());
	}
}