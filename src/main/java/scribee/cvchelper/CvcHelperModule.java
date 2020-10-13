package scribee.cvchelper;

import scribee.cvchelper.gui.GuiPosition;

public abstract class CvcHelperModule {
	
	private boolean enabled = true;
	
	/**
	 * Modules should expect this method to be called every time the player dies or a game ends.
	 */
	public abstract void reset();
	
	/**
	 * Returns text to be printed in the GuiPosition specified by getGuiPosition().
	 * 
	 * @return String - text to be printed on screen
	 */
	public abstract String getMessage();
	
	/**
	 * Whether or not this module has event listeners and needs to be registered on the forge event bus.
	 * 
	 * @return boolean - whether or not this modules uses event listeners
	 */
	public abstract boolean hasEventHandler();
	
	/**
	 * Position on screen to display messages from this module.
	 * 
	 * @return GuiPosition - where to display module messages
	 */
	public abstract GuiPosition getGuiPosition();
	
	public void disable() {
		enabled = false;
	}
	
	public void enable() {
		enabled = true;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
