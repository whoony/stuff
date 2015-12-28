package com.stuff.hibernate.dao;

import java.util.List;

import com.stuff.bean.Role;
import com.stuff.hibernate.BaseHibernateDAO;
import com.stuff.hibernate.QueryBuilder;

public class RoleDao extends BaseHibernateDAO<Role>{

	private static RoleDao instance = new RoleDao();
	
	public static RoleDao getInstance()
	{
		return instance;
	}

	@Override
	protected Class<Role> getEntityClazz() {
		return Role.class;
	}

	public List<Role> getAll() {
		QueryBuilder qb = getQueryBuilder();
		return qb.getList();
	}
}
