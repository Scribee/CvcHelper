package scribee.cvchelper;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderGuiHandler {
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post event) {
		// To keep from messing up normal rendering, wait until after the xp bar
		if (event.type == ElementType.EXPERIENCE) {
			new CvcHelperGui(Minecraft.getMinecraft());
		}
	}
}
