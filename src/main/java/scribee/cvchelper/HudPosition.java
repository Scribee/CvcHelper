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
	NONE;
	
	/**
	 * Returns the hud position that follows the current one.
	 * 
	 * @return next HudPosition
	 */
	public HudPosition next() {
		return values()[(ordinal() + 1) % values().length];
	}
}
