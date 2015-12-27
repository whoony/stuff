package com.stuff.util;

public enum PrivilegeConstant
{
	ALL("all", "Super_user", "SuperUser权限"),
	
	MENU_DASHBOARD("menu_dashboard", "Dashboard菜单", "Dashboard菜单项权限"),
	MENU_REPORTS("menu_reports", "日报管理", "日报管理菜单"),
	
	TAB_TODAY_REPORT("tab_today_report", "tab今日日报", "dashboard, 今天的日报"),
	TAB_HISTORY_REPORTS("tab_history_report", "tab历史日报", "dashboard, 历史日报"),
	
	;

	private String shortName;
	private String displayName;
	private String description;
	
	private PrivilegeConstant(String shortName, String displayName, String desc)
	{
		this.shortName = shortName;
		this.description = desc;
		this.displayName = displayName;
	}

	public String getShortName()
	{
		return shortName;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getDescription()
	{
		return description;
	}
}
