package com.stuff.hibernate.dao;

import com.stuff.bean.Role;
import com.stuff.hibernate.BaseHibernateDAO;

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
}
