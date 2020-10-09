package scribee.cvchelper.gui;

/**
 * Enum defining all on screen positions that CvcHelper messages can be printed.
 */
public enum GuiPosition {
	HOTBAR_LEFT,
	HOTBAR_ABOVE,
	HOTBAR_RIGHT,
	BOTTOM_RIGHT,
	TOP_RIGHT,
	TOP_MIDDLE,
	TOP_LEFT,
	SIDE_LEFT,
	BOTTOM_LEFT,
	CHAT,
	NONE,
	CROSSHAIR_RIGHT, // only to be used by item countdown notifications \/
	CROSSHAIR_LEFT,
	CROSSHAIR_ABOVE,
	CROSSHAIR_BELOW;
	
	/**
	 * Returns the gui position that follows the current one.
	 * 
	 * @return GuiPosition - next position on screen to render text overlays
	 */
	public GuiPosition nextTextPosition() {
		// Return the next position, but always skip from the second to last value to the beginning
		return values()[(ordinal() + 1) % (GuiPosition.NONE.ordinal() + 1)];
	}
}
