package com.stuff.controller;

import java.util.List;

import com.stuff.bean.Principal;
import com.stuff.hibernate.dao.PrincipalDao;
import com.stuff.util.MD5Util;

public class PrincipalUtil {
	
	private static PrincipalDao pDao = PrincipalDao.getInstance();
	
	public static boolean login(String username, String password, List<String> error)
	{
		Principal p = pDao.getByUsername(username);
		if(p == null)
		{
			error.add("用户名错误");
			return false;
		}
		String pass = encodePassword(username, password);
		if(!pass.equals(p.getPassword()))
		{
			error.add("密码错误");
			return false;
		}
		return true;
	}
	
	public static Principal getPrincipal(String username)
	{
		return pDao.getByUsername(username);
	}
	
	public static String encodePassword(String username, String password)
	{
		String s = "hello" + username + password + "$$world";
		return MD5Util.md5(s);
	}
}
