package scribee.cvchelper;

public enum HudPosition {
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
	CROSSHAIR; // only to be used by the grenade countdown
	
	/**
	 * Returns the hud position that follows the current one.
	 * 
	 * @return next HudPosition
	 */
	public HudPosition next() {
		// Return the next position, but always skip from the second to last value to the beginning
		return values()[(ordinal() + 1) % (values().length - 1)];
	}
}
