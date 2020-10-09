package scribee.cvchelper;

import scribee.cvchelper.gui.GuiPosition;

public abstract class CvcHelperModule {
	public abstract void reset();
	
	public abstract String getMessage();
	
	public abstract boolean hasEventHandler();
	
	public abstract GuiPosition getGuiPosition();
}
