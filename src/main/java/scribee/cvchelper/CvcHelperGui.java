package scribee.cvchelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class CvcHelperGui extends Gui {
	final int TEXT_WIDTH = 67; // maximum width in pixels for killstreak messages (assuming 3 digits isn't possible)
	final int TEXT_HEIGHT = 8; // height of a line of text

	/**
	 * Constructor for the CvcHelperGui, which just displays the killstreak message as text in different positions on the screen.
	 * @param mc - Minecraft instance
	 */
	public CvcHelperGui(Minecraft mc) {
		ScaledResolution res = new ScaledResolution(mc);

		// Position to draw the killstreak message at
		int x = 0;
		int y = 0;

		// Check where the messages should be printed and calculate the x and y coordinates of that area
		switch (CvcHelper.getCurrentHudPos()) {
		case HOTBAR_LEFT:
			x = (res.getScaledWidth() / 2) - 91 - TEXT_WIDTH - 2; // middle of the screen - half the width of the hotbar - width of the string - buffer
			y = res.getScaledHeight() - TEXT_HEIGHT - 7; // to center vertically next to the 22 pixel hotbar means 7 pixels below
			break;
		case HOTBAR_ABOVE:
			x = (res.getScaledWidth() / 2) + 19; // positioned where the food bar would be in other games
			y = res.getScaledHeight() - TEXT_HEIGHT - 30; // vertically aligned with health bar
			break;
		case HOTBAR_RIGHT:
			x = (res.getScaledWidth() / 2) + 91 + 2;
			y = res.getScaledHeight() - TEXT_HEIGHT - 7;
			break;
		case BOTTOM_RIGHT:
			x = res.getScaledWidth() - TEXT_WIDTH - 2;
			y = res.getScaledHeight() - TEXT_HEIGHT - 2;
			break;
		case TOP_RIGHT:
			x = res.getScaledWidth() - TEXT_WIDTH - 2;
			y = 2;
			break;
		case TOP_MIDDLE:
			x = (res.getScaledWidth() / 2) - (TEXT_WIDTH / 2);
			y = 19; // buffer to put it below the bossbar which is sometimes visible in cvc
			break;
		case TOP_LEFT:
			x = 2;
			y = 2;
			break;
		case SIDE_LEFT:
			x = 2;
			y = res.getScaledHeight() / 2;
			break;
		case BOTTOM_LEFT:
			x = 2;
			y = res.getScaledHeight() - TEXT_HEIGHT - 2;
			break;
		case CHAT:
			return; // return without drawing the string at all
		case NONE:
			return;
		default:
			System.out.println("Unknown position");
			break;
		}

		// Draw the message at the calculated coordinates
		drawString(mc.fontRendererObj, CvcHelper.getStreakMessage(), x, y, Integer.parseInt("FFFFFF", 16));
	}
}
