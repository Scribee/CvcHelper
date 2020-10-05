package scribee.cvchelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scribee.cvchelper.gui.HudPosition;
import scribee.cvchelper.gui.RenderGuiHandler;
import scribee.cvchelper.util.Reference;

/**
 * Main mod class in charge of reading incoming chat messages and counting killstreaks.
 */
@Mod(modid = Reference.MOD_ID, version = Reference.VERSION)
public class CvcHelper {

    // Player's username
	private static String name = "";
	// Regex that matches kill and death messages
	private static Pattern killfeedPattern = Pattern.compile(EnumChatFormatting.RESET + Reference.S + "[34](\\w{1,16}) " + EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + "(\\W\\W?\\W?) " + EnumChatFormatting.RESET + Reference.S + "[34](\\w{1,16})");
	// Key binding for changing the position that killstreak messages are displayed
	private static KeyBinding keyChangeHudPos = new KeyBinding("keyBinding.hudPos", Keyboard.KEY_H, "category.cvchelper");
	// Current position to display killstreak messages
	private static HudPosition currentHudPos = HudPosition.HOTBAR_LEFT;
	
	public static StreakDisplay streakCounter = new StreakDisplay();
	public static GrenadeCountdown nadeCounter = new GrenadeCountdown();
	
	/**
	 * Initialize mod.
	 */
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	MinecraftForge.EVENT_BUS.register(this);
    	
    	MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
    	
    	MinecraftForge.EVENT_BUS.register(nadeCounter);

    	ClientRegistry.registerKeyBinding(keyChangeHudPos);
    	MinecraftForge.EVENT_BUS.register(new KeyHandler());
	}
    
    /**
     * Called whenever the player receives a chat message.
     * 
     * @param event - Chat message received event
     */
    @SubscribeEvent
	public void onChatEvent(ClientChatReceivedEvent event) {
		String message = event.message.getFormattedText();

		// Get the player's name if it isn't saved already
		if (name.equals("")) {
			name = Minecraft.getMinecraft().thePlayer.getDisplayNameString();
		}

		Matcher matcher = killfeedPattern.matcher(message);
		// If the chat message matches the pattern of a kill message
		if (matcher.find()) {
			// If it was the player's kill (first name in the message is their username)
			if (matcher.group(1).equals(name)) {
				streakCounter.updateWeaponStreaks(matcher.group(2));
				if (currentHudPos == HudPosition.CHAT) {
					sendStreakMessage();
				}
			}
			// If it was the player's death (last name in the message is their username)
			else if (matcher.group(3).equals(name)) {
				if (streakCounter.getTotalStreak() > 0) {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Reset streaks." + EnumChatFormatting.RESET));
				}
				streakCounter.resetStreaks();
			}
		}
		// If player is sent to a different server (or starts a new game)
		else if (message.startsWith(EnumChatFormatting.GREEN + "Sending you to ") && message.endsWith("!" + EnumChatFormatting.RESET)) {
			System.out.println("New game");
			streakCounter.resetStreaks();
		}
		// If player death message doesn't involve another player
		else if (message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.GRENADE + " ")
				|| message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.FIRE + " ")
				|| message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.C4 + " ")
				|| message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.BONE + " ")
				&& message.endsWith(name + EnumChatFormatting.RESET)) {
			streakCounter.resetStreaks();
		}
		else if (message.equals(EnumChatFormatting.RESET + "" + EnumChatFormatting.GREEN + "You selected the " + EnumChatFormatting.RESET + "" + EnumChatFormatting.GOLD + "Frag Grenade" + EnumChatFormatting.RESET)) {
			nadeCounter.startCountdown();
		}
	}
    
    /**
     * Prints the current streak in chat.
     */
    public static void sendStreakMessage() {
    	Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(streakCounter.getStreakMessage()));
    }
    
    /**
     * Getter for the changeHudPos key binding.
     * 
     * @return KeyBinding - key used to change the position of messages
     */
    public static KeyBinding getHudPosKeyBinding() {
    	return keyChangeHudPos;
    }

    /**
     * Getter for the current position messages will be displayed.
     * 
     * @return HudPosition - area of screen where killstreaks will be displayed
     */
	public static HudPosition getCurrentHudPos() {
		return currentHudPos;
	}

	/**
	 * Cycles currentHudPos to the next HudPosition.
	 */
	public static void nextHudPosition() {
		currentHudPos = currentHudPos.next();
		if (currentHudPos == HudPosition.CHAT) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Killstreaks will now be printed in chat.\n" + EnumChatFormatting.DARK_GREEN + "Press " + EnumChatFormatting.RESET + Keyboard.getKeyName(getHudPosKeyBinding().getKeyCode()) + EnumChatFormatting.DARK_GREEN + " again to disable messages entirely." + EnumChatFormatting.RESET));
		}
	}
}
