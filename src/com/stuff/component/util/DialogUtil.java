package com.stuff.component.util;

import java.util.List;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;

public class DialogUtil
{

	public static void showErrorOnScreen(String message)
	{
		Notification.show(message,  Type.ERROR_MESSAGE);
	}
	
	public static void showErrors(List<String> errors)
	{
		StringBuffer sb = new StringBuffer();
		for(String s : errors)
		{
			sb.append("\n").append(s); //$NON-NLS-1$
		}
		MessageBox.showPlain(Icon.ERROR, "错误", sb.toString(), ButtonId.OK);
	}
	
	public static void infoMessage(List<String> message)
	{
		StringBuffer sb = new StringBuffer();
		for(String s : message)
		{
			sb.append("\n").append(s); //$NON-NLS-1$
		}
		MessageBox.showPlain(Icon.INFO, "提示", sb.toString(), ButtonId.OK); //$NON-NLS-1$
	}
	
	public static void successMessage(String message)
	{
		Notification success = new Notification(message);
		success.setDelayMsec(2000);
		success.setStyleName("bar success small"); //$NON-NLS-1$
		success.setPosition(Position.BOTTOM_CENTER);
		success.show(Page.getCurrent());
	}
	
	public static Window popDialog(String caption, Component com, UI ui, String width, String height)
	{
		Window dialog = new Window();
		dialog.setContent(com);
		dialog.setCaption(caption);
		dialog.setWindowMode(WindowMode.NORMAL);
		dialog.setModal(true);
		dialog.setWidth(width);
		dialog.setHeight(height);
		ui.addWindow(dialog);
		return dialog;
	}
	
	public static void confirmDialog(String title, String message, ConfirmListener listener)
	{
		MessageBox.showPlain(Icon.WARN, title, message, listener, ButtonId.YES, ButtonId.NO);
	}
	
	public static void deleteConfirm(ConfirmListener listener)
	{
		confirmDialog("警告", "确定删除吗？", listener);
	}
}
