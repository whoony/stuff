package com.stuff.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.stuff.bean.Role;
import com.stuff.dashboard.DashboardUI;

public class SecurityDataUtil
{
	final static String ADDRESS = "WEIXIN_APPLICATION_ADDRESS"; //$NON-NLS-1$
	final static String APPLICATION_TYPE = "APPLICATION_TYPE"; //$NON-NLS-1$
	final static String WEIXIN_APPLICATION = "WEIXIN"; //$NON-NLS-1$

	final static String ATTR_DISPLAY_NAME = "displayName"; //$NON-NLS-1$
	final static String ATTR_CREATION_TIME = "creationTime"; //$NON-NLS-1$
	
	
	public static boolean isAuthorize(PrivilegeConstant privilege)
	{
		return isAuthorize(privilege.getShortName());
	}
	
	public static boolean isAuthorize(PrivilegeConstant ... list)
	{
		List<String> needFuncs = new ArrayList<String>();
		for(PrivilegeConstant privilege : list)
		{
			needFuncs.add(privilege.getShortName());
		}
		return isAuthorize(needFuncs);
	}
	
	public static boolean isAuthorize(String needFuncs) 
	{
		return isAuthorize(Arrays.asList(needFuncs));
	}
	
	public static boolean isAuthorize(List<String> needFuncs)
	{
		Set<String> functions =  getCurrentFunction();
		if(functions.contains(PrivilegeConstant.ALL.getShortName())) {
			return true;
		}
		else {
			return functions.containsAll(needFuncs);
		}
	}
	
	public static Set<String> getCurrentFunction()
	{
		Set<Role> roles = DashboardUI.getCurrentUser().getRoles();
		Set<String> set = new HashSet<String>();
		for(Role r : roles)
		{
			set.addAll(r.getFuncs());
		}
		return set;
	}
}
