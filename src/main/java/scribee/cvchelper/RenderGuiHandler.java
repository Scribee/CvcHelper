package scribee.cvchelper;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderGuiHandler {
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post event) {
		String streak = CvcHelper.getStreakMessage();
		String nade = GrenadeCountdown.getNadeMessage();
		// To keep from messing up normal rendering, wait until after the xp bar is drawn
		if (event.type == ElementType.EXPERIENCE) {
			if (!streak.equals("")) {
				new CvcHelperGui(Minecraft.getMinecraft(), streak, CvcHelper.getCurrentHudPos());
			}
			
			if (!nade.equals("")) {
				new CvcHelperGui(Minecraft.getMinecraft(), nade, HudPosition.CROSSHAIR);
			}
		}
	}
}
