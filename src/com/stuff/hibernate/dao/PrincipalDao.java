package com.stuff.hibernate.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.stuff.bean.Principal;
import com.stuff.hibernate.BaseHibernateDAO;

public class PrincipalDao extends BaseHibernateDAO<Principal>
{

	Logger log = Logger.getLogger(PrincipalDao.class);
	
	private static PrincipalDao instance = new PrincipalDao();
	
	public static PrincipalDao getInstance()
	{
		return instance;
	}
	
	public Principal getByUsername(String username)
	{
		List<Principal> list = this.queryListByProperty("username", "=", username);
		if(list.size() > 1)
		{
			log.error("Should only get 1 result. username=" + username);
		}
		
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	protected Class<Principal> getEntityClazz() {
		return Principal.class;
	}
}
