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
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import scribee.cvchelper.gui.GuiPosition;
import scribee.cvchelper.gui.RenderGuiHandler;
import scribee.cvchelper.util.Reference;

/**
 * Main mod class in charge of reading incoming chat messages and counting killstreaks.
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class CvcHelper {

    // Player's username
	private static String name = "";
	// Regex that matches kill and death messages
	private static Pattern killfeedPattern = Pattern.compile(EnumChatFormatting.RESET + Reference.S + "[34](\\w{1,16}) " + EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + "(\\W\\W?\\W?) " + EnumChatFormatting.RESET + Reference.S + "[34](\\w{1,16})");
	// Key binding for changing the position that killstreak messages are displayed
	private static KeyBinding keyChangeGuiPos = new KeyBinding("keyBinding.guiPos", Keyboard.KEY_H, "category.cvchelper");
	// Current position to display killstreak messages
	private static GuiPosition currentGuiPos = GuiPosition.HOTBAR_LEFT;
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	// Modules
	private static StreakDisplay streakCounter = new StreakDisplay();
	private static ItemCountdown nadeCounter = new ItemCountdown(Reference.SYMBOL_GRENADE, GuiPosition.CROSSHAIR_LEFT);
	private static ItemCountdown firebombCounter = new ItemCountdown(Reference.SYMBOL_FIREBOMB, GuiPosition.CROSSHAIR_RIGHT);
	public static CvcHelperModule[] modules = {streakCounter, nadeCounter, firebombCounter};
	
	public static RenderGuiHandler guiHandler = new RenderGuiHandler();
	
	/**
	 * Initialize mod.
	 */
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	MinecraftForge.EVENT_BUS.register(this);
    	
    	MinecraftForge.EVENT_BUS.register(guiHandler);
    	
    	for (CvcHelperModule module : modules) {
    		if (module.hasEventHandler()) {
    			MinecraftForge.EVENT_BUS.register(module);
    		}
    	}

    	ClientRegistry.registerKeyBinding(keyChangeGuiPos);
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
			name = mc.thePlayer.getDisplayNameString();
		}

		Matcher matcher = killfeedPattern.matcher(message);
		// If the chat message matches the pattern of a kill message
		if (matcher.find()) {
			// If it was the player's kill (first name in the message is their username)
			if (matcher.group(1).equals(name)) {
				streakCounter.updateWeaponStreaks(matcher.group(2));
				if (currentGuiPos == GuiPosition.CHAT) {
					sendStreakMessage();
				}
			}
			// If it was the player's death (last name in the message is their username)
			else if (matcher.group(3).equals(name)) {
				if (streakCounter.getTotalStreak() > 0) {
					mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Reset streaks." + EnumChatFormatting.RESET));
				}
				streakCounter.reset();
			}
		}
		// If player death message doesn't involve another player
		else if ((message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.GRENADE + " ")
				|| message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.FIRE + " ")
				|| message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.C4 + " ")
				|| message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.BONE + " "))
				&& message.endsWith(name + EnumChatFormatting.RESET)) {
			streakCounter.reset();
		}
		// If player selects grenade in TDM
		else if (message.equals(EnumChatFormatting.RESET + "" + EnumChatFormatting.GREEN + "You selected the " + EnumChatFormatting.RESET + "" + EnumChatFormatting.GOLD + "Frag Grenade" + EnumChatFormatting.RESET)) {
			nadeCounter.startCountdown();
		}
		// If player selects firebomb in TDM
		else if (message.equals(EnumChatFormatting.RESET + "" + EnumChatFormatting.GREEN + "You selected the " + EnumChatFormatting.RESET + "" + EnumChatFormatting.GOLD + "Firebomb" + EnumChatFormatting.RESET)) {
			firebombCounter.startCountdown();
		}
		// Detect when games end to stop displaying anything
		else if (message.equals(EnumChatFormatting.RESET + "                          " + EnumChatFormatting.RESET + "" + EnumChatFormatting.DARK_AQUA + "" + EnumChatFormatting.BOLD + "Cops won the game!" + EnumChatFormatting.RESET) || message.equals(EnumChatFormatting.RESET + "                       " + EnumChatFormatting.RESET + "" + EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + "Criminals won the game!" + EnumChatFormatting.RESET)) {
			System.out.println("Game end");
			for (CvcHelperModule module : modules) {
				module.reset();
			}
		}
		// If player is sent to a different server (or starts a new game)
		else if (message.startsWith(EnumChatFormatting.GREEN + "Sending you to ") && message.endsWith("!" + EnumChatFormatting.RESET)) {
			System.out.println("New game");
			for (CvcHelperModule module : modules) {
				module.reset();
			}
		}
    }
    
    /**
     * Called when player disconnects from a sever. Used to reset all mod GUIs.
     * 
     * @param event - disconnect from server event
     */
    @SubscribeEvent
    public void onPlayerLeaveEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
    	for (CvcHelperModule module : modules) {
			module.reset();
		}
    }

    /**
     * Prints the current streak in chat.
     */
    public static void sendStreakMessage() {
    	mc.thePlayer.addChatMessage(new ChatComponentText(streakCounter.getMessage()));
    }
    
    /**
     * Getter for the changeGuiPos key binding.
     * 
     * @return KeyBinding - key used to change the position of messages
     */
    public static KeyBinding getGuiPosKeyBinding() {
    	return keyChangeGuiPos;
    }

    /**
     * Getter for the current position messages will be displayed.
     * 
     * @return GuiPosition - area of screen where killstreaks will be displayed
     */
	public static GuiPosition getCurrentGuiPos() {
		return currentGuiPos;
	}

	/**
	 * Cycles currentGuiPos to the next GuiPosition.
	 */
	public static void nextGuiPosition() {
		currentGuiPos = currentGuiPos.nextTextPosition();
		if (currentGuiPos == GuiPosition.CHAT) {
			mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Killstreaks will now be printed in chat.\n" + EnumChatFormatting.DARK_GREEN + "Press " + EnumChatFormatting.RESET + Keyboard.getKeyName(getGuiPosKeyBinding().getKeyCode()) + EnumChatFormatting.DARK_GREEN + " again to disable messages entirely." + EnumChatFormatting.RESET));
		}
	}
}
