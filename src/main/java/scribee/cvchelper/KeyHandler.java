package scribee.cvchelper;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

/**
 * Checks for mod keybind presses.
 */
public class KeyHandler {
	/**
	 * Called when any key is pressed.
	 * 
	 * @param event - Key input event
	 */
	@SubscribeEvent
	public void onKeyPressed(KeyInputEvent event) {
		// Check if the pressed key was the one bound to this function
		if (CvcHelper.getGuiPosKeyBinding().isKeyDown()) {
			CvcHelper.nextGuiPosition(); // cycle to the next position
		}
	}
}
