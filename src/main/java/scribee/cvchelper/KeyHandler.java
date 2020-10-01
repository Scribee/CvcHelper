package scribee.cvchelper;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyHandler {
	@SubscribeEvent
	public void onKeyPressed(KeyInputEvent event) {
		// Check if the pressed key was the one bound to this function
		if (CvcHelper.getHudPosKeyBinding().isKeyDown()) {
			CvcHelper.nextHudPosition(); // cycle to the next position
		}
	}
}
