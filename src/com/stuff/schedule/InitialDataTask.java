package com.stuff.schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stuff.bean.Principal;
import com.stuff.bean.Role;
import com.stuff.controller.PrincipalUtil;
import com.stuff.hibernate.dao.PrincipalDao;
import com.stuff.hibernate.dao.RoleDao;
import com.stuff.util.PrivilegeConstant;

public class InitialDataTask implements Runnable{

	private static Map<String, List<PrivilegeConstant>> rolesMap;
	private static Map<String, String> nameMap;
	
	static
	{
		rolesMap = new HashMap<String, List<PrivilegeConstant>>();
		rolesMap.put("system_admin", Arrays.asList(PrivilegeConstant.ALL));
		rolesMap.put("stuff", 
				Arrays.asList(PrivilegeConstant.MENU_DASHBOARD, PrivilegeConstant.TAB_TODAY_REPORT, PrivilegeConstant.TAB_HISTORY_REPORTS));
		
		nameMap = new HashMap<String, String>();
		nameMap.put("jiashengjun", "贾胜军");
		nameMap.put("likaige", "李凯歌");
		nameMap.put("lvweining", "吕伟宁");
		nameMap.put("wanghongyu", "王洪宇");
		nameMap.put("system_admin", "System Admin");
	}
	
	@Override
	public void run() {
		
		
		RoleDao roleDao = RoleDao.getInstance();
		List<Role> roles = roleDao.getAll();
		Map<String, Role> nameRoleMap = new HashMap<String, Role>();
		if(roles == null || roles.isEmpty())
		{
			roles = new ArrayList<Role>();
			Iterator<String> iterator = rolesMap.keySet().iterator();
			while(iterator.hasNext())
			{
				String role = iterator.next();
				Role roleEntity = new Role();
				roleEntity.setName(role);
				
				List<PrivilegeConstant> pList = rolesMap.get(role);
				Set<String> funcs = new HashSet<String>();
				for(PrivilegeConstant p : pList)
				{
					funcs.add(p.getShortName());
				}
				roleEntity.setFuncs(funcs);
				roleDao.saveEntity(roleEntity);
				roles.add(roleEntity);
			}
		}
		
		for(Role r : roles)
		{
			nameRoleMap.put(r.getName(), r);
		}
		
		
		
		PrincipalDao pDao = PrincipalDao.getInstance();
		Principal p = pDao.getByUsername("system_admin");
		if(p == null)
		{
			Iterator<String> iterator = nameMap.keySet().iterator();
			while(iterator.hasNext())
			{
				String username = iterator.next();
				String name = nameMap.get(username);
				
				Principal principal = new Principal();
				principal.setName(name);
				principal.setUsername(username);
				principal.setPassword(PrincipalUtil.encodePassword(username, "123"));
				if("system_admin".equals(username))
				{
					principal.getRoles().add(nameRoleMap.get("system_admin"));
				}
				else
				{
					principal.getRoles().add(nameRoleMap.get("stuff"));
				}
				
				pDao.saveEntity(principal);
			}
			
		}
		
	}

}
