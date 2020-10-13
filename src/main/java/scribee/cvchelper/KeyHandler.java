package scribee.cvchelper;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import scribee.cvchelper.config.CvcHelperConfigGui;

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
		// Check whether the pressed key is set as a keybind for this mod
		if (CvcHelper.getGuiPosKeyBinding().isKeyDown()) {
			CvcHelper.nextGuiPosition(); // cycle to the next position
		}
		else if (CvcHelper.getOpenConfigKeyBinding().isKeyDown()) {
			Minecraft.getMinecraft().displayGuiScreen(new CvcHelperConfigGui(Minecraft.getMinecraft().currentScreen)); // open the config GUI
		}
	}
}
