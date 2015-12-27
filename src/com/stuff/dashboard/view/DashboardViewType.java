package com.stuff.dashboard.view;

import com.stuff.dashboard.view.dashboard.DashboardView;
import com.stuff.dashboard.view.report.ReportView;
import com.stuff.util.PrivilegeConstant;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum DashboardViewType {
	DASHBOARD("Dashboard", DashboardView.class, FontAwesome.HOME, true, PrivilegeConstant.MENU_DASHBOARD), 
	REPORT("Report", ReportView.class, FontAwesome.FILE_TEXT, true, PrivilegeConstant.MENU_REPORTS),
	
	;
	
	private final String viewName;
	private final Class<? extends View> viewClass;
	private final Resource icon;
	private final boolean stateful;
    private final PrivilegeConstant authorize;
    
	private DashboardViewType(final String viewName, final Class<? extends View> viewClass, 
			final Resource icon, final boolean stateful, final PrivilegeConstant authorize) {
		this.viewName = viewName;
		this.viewClass = viewClass;
		this.icon = icon;
		this.stateful = stateful;
		this.authorize = authorize;
	}

	public boolean isStateful() {
		return stateful;
	}

	public String getViewName() {
		return viewName;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public Resource getIcon() {
		return icon;
	}

	public static DashboardViewType getByViewName(final String viewName) {
		DashboardViewType result = null;
		for (DashboardViewType viewType : values()) {
			if (viewType.getViewName().equals(viewName)) {
				result = viewType;
				break;
			}
		}
		return result;
	}

	public PrivilegeConstant getAuthorize() {
		return authorize;
	}

}
