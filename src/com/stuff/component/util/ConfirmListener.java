package com.stuff.component.util;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.MessageBoxListener;

public abstract class ConfirmListener implements MessageBoxListener
{

	public abstract void accept(boolean flag);
	
	@Override
	public void buttonClicked(ButtonId buttonId)
	{
		accept(buttonId == ButtonId.YES);
	}

}
