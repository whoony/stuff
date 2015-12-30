package com.stuff.dashboard.view.dashboard;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.stuff.dashboard.event.DashboardEventBus;
import com.stuff.dashboard.view.report.HistoryReportUI;
import com.stuff.dashboard.view.report.MyReportUI;
import com.stuff.util.PrivilegeConstant;
import com.stuff.util.SecurityDataUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class DashboardView extends Panel implements View{

    private final VerticalLayout root;
	private VerticalLayout detailPanel = new VerticalLayout();
	private boolean initialized = false;
	private Button myReport;
	private Button historyReport;
    
    public DashboardView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        buildTopButtonBar();
		root.addComponent(new Label("<hr>", ContentMode.HTML)); //$NON-NLS-1$
		root.addComponent(detailPanel);
		root.setExpandRatio(detailPanel, 1);
    }
    
    
	
	private void buildTopButtonBar()
	{
		GridLayout buttons = new GridLayout(6, 1);
		root.addComponent(buttons);
		buttons.setSpacing(true);
		//buttons.setWidth("100%");
		
		myReport = new Button("今日日报");
		historyReport = new Button("历史日报");

		if(SecurityDataUtil.isAuthorize(PrivilegeConstant.MENU_DASHBOARD))
			buttons.addComponent(myReport);
		if(SecurityDataUtil.isAuthorize(PrivilegeConstant.MENU_REPORTS))
			buttons.addComponent(historyReport);
		
		myReport.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event)
			{
				detailPanel.removeAllComponents();
				detailPanel.addComponent(new MyReportUI());
			}
		});
		
		historyReport.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event)
			{
				detailPanel.removeAllComponents();
				detailPanel.addComponent(new HistoryReportUI());
			}
		});
			
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd EEEE");
		String dateString = dateFormatter.format(new Date());
		Label today = new Label("<h3>"+dateString+"</h3>", ContentMode.HTML);
		buttons.addComponent(today);
		buttons.setComponentAlignment(today, Alignment.MIDDLE_LEFT);
	}
	
    

    @Override
    public void enter(final ViewChangeEvent event) {
		if(!initialized)
		{
			initialized = true;
			myReport.click();
		}
    }


}
