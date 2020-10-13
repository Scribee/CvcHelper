package scribee.cvchelper.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scribee.cvchelper.CvcHelper;
import scribee.cvchelper.CvcHelperModule;

/**
 * Class in charge of rendering all GUI elements for the mod.
 */
public class RenderGuiHandler {	
	public RenderGuiHandler() {
		
	}
	
	/**
	 * Called after the game renders GUI elements on the screen. Used to draw custom strings
	 * to show the player killstreak or grenade countdown messages.
	 * 
	 * @param event - Render GUI element on screen event
	 */
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post event) {
		// To keep from messing up normal rendering, wait until after the xp bar is drawn
		if (event.type == ElementType.EXPERIENCE) {
			for (CvcHelperModule module : CvcHelper.modules) {
				String message = module.getMessage();
				if (!message.equals("")) {
					new CvcHelperGui(Minecraft.getMinecraft(), message, module.getGuiPosition()); // render wherever the modules messages are displayed
				}
			}
		}
	}
}
