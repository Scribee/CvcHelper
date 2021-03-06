package scribee.cvchelper.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class CvcHelperGui extends Gui {
	final int TEXT_WIDTH = 72; // maximum width in pixels for killstreak messages (assuming 3 digits isn't possible)
	final int TEXT_HEIGHT = 8; // height of a line of text

	/**
	 * Constructor for the CvcHelperGui, which just draws mod messages as text in different positions on the screen.
	 * 
	 * @param mc - Minecraft instance
	 * @param message - message to display on screen
	 * @param position - area of screen to display the provided message
	 */
	public CvcHelperGui(Minecraft mc, String message, GuiPosition position) {
		ScaledResolution res = new ScaledResolution(mc);

		// Position to draw the message at
		int x = 0;
		int y = 0;

		// Check where the messages should be printed and calculate the x and y coordinates of that area
		switch (position) {
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
			y = 2; // in 1.8 the bossbar doesn't show during cvc games, so this position shouldn't ever be covered
			break;
		case TOP_LEFT:
			x = 2;
			y = 2;
			break;
		case SIDE_LEFT:
			x = 2;
			y = (res.getScaledHeight() / 2) - 4;
			break;
		case BOTTOM_LEFT:
			x = 2;
			y = res.getScaledHeight() - TEXT_HEIGHT - 2;
			break;
		case CHAT:
			return; // return without drawing the string at all
		case NONE:
			return;
		case CROSSHAIR_RIGHT:
			x = (res.getScaledWidth() / 2) + 6;
			y = (res.getScaledHeight() / 2) - 4;
			break;
		case CROSSHAIR_LEFT:
			x = (res.getScaledWidth() / 2) - 4 - 2 - 7; // center - crosshair width/2 - buffer - grenade symbol width
			y = (res.getScaledHeight() / 2) - 4;
			break;
		case CROSSHAIR_ABOVE:
			x = (res.getScaledWidth() / 2) - 3; // center - symbol width / 2
			y = (res.getScaledHeight() / 2) - 4 - 2 - TEXT_HEIGHT; // center - crosshair height / 2 - buffer - symbol height
			break;
		case CROSSHAIR_BELOW:
			x = (res.getScaledWidth() / 2) - 3;
			y = (res.getScaledHeight() / 2) + 4 + 2;
			break;
		default:
			System.out.println("Unknown position");
			break;
		}

		// Draw the message at the calculated coordinates
		drawString(mc.fontRendererObj, message, x, y, Integer.parseInt("FFFFFF", 16));
	}
}
