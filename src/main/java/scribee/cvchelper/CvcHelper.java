package scribee.cvchelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
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
    // Total number of kills the player has gotten since their last death
	private static int totalStreak = 0;
	// Number of kills with each different weapon the player has gotten since their last death
	private static Map<String, Integer> weaponStreaks = new HashMap<String, Integer>();
	// Message stating the player's current streak with the most recent weapon used
	private static String currentStreak = "Streak display";
	// Key binding for changing the position that killstreak messages are displayed
	private static KeyBinding changeHudPos = new KeyBinding("keyBinding.hudPos", Keyboard.KEY_H, "category.cvchelper");
	// Current position to display killstreak messages
	private static HudPosition currentHudPos = HudPosition.HOTBAR_LEFT;
	
	/**
	 * Initialize mod.
	 */
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	MinecraftForge.EVENT_BUS.register(this);
    	
    	MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
    	
    	MinecraftForge.EVENT_BUS.register(new GrenadeCountdown());

    	ClientRegistry.registerKeyBinding(changeHudPos);
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
				totalStreak++;
				currentStreak = updateWeaponStreaks(matcher.group(2));
				if (currentHudPos == HudPosition.CHAT) {
					sendStreakMessage();
				}
			}
			// If it was the player's death (last name in the message is their username)
			else if (matcher.group(3).equals(name)) {
				if (totalStreak > 0) {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Reset streaks." + EnumChatFormatting.RESET));
				}
				resetWeaponStreaks();
			}
		}
		// If player is sent to a different server (or starts a new game)
		else if (message.startsWith(EnumChatFormatting.GREEN + "Sending you to ") && message.endsWith("!" + EnumChatFormatting.RESET)) {
			System.out.println("New game");
			resetWeaponStreaks();
		}
		// If player death message doesn't involve another player
		else if (message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.GRENADE + " ")
				|| message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.FIRE + " ")
				|| message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.C4 + " ")
				|| message.startsWith(EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + Reference.BONE + " ")
				&& message.endsWith(name + EnumChatFormatting.RESET)) {
			resetWeaponStreaks();
		}
		else if (message.equals(EnumChatFormatting.RESET + "" + EnumChatFormatting.GREEN + "You selected the " + EnumChatFormatting.RESET + "" + EnumChatFormatting.GOLD + "Frag Grenade" + EnumChatFormatting.RESET)) {
			GrenadeCountdown.startCountdown();
		}
	}
    
    /**
     * Increments the killstreak counter for the specified weapon. If a key matching this weapon doesn't exist
     * for the weaponStreaks map yet, it will be created with an initial value of 1.
     * 
     * @param weapon - Gun characters used to indicate what weapon was used
     * @return streak message to send to the player
     */
    private String updateWeaponStreaks(String weapon) {
    	int kills = 0;
    	
    	// If there was one, remove the headshot symbol to just get the characters that make up the gun
    	if (weapon.substring(weapon.length() - 1).equals(Reference.HEADSHOT)) {
    		weapon = weapon.substring(0, weapon.length() - 1);
    	}
    	
    	// If the weapon already exists as a key in the map, increment its value
    	if (weaponStreaks.containsKey(weapon)) {
    		kills = weaponStreaks.get(weapon) + 1;
    	}
    	// If the weapon doesn't exist in the map, the streak must be 1
    	else {
    		kills = 1;
    	}
    	
    	// Update the map
    	weaponStreaks.put(weapon, kills);
    	
    	// Return the streak message to be printed to the player
    	return weapon + EnumChatFormatting.DARK_GREEN + " streak: " + EnumChatFormatting.RESET + kills;
    } 
    
    /**
     * Resets the value of every key in the weaponStreaks map.
     */
    private void resetWeaponStreaks() {
    	totalStreak = 0;
    	currentStreak = "";
    	
    	weaponStreaks.forEach(new BiConsumer<String, Integer>() {
			@Override
			public void accept(String k, Integer v) {
				weaponStreaks.put(k, 0);
			}
		});
    }
    
    /**
     * Getter for currentStreak variable, which stores the message stating the current killstreak.
     * 
     * @return a formatted message stating the current weapon and streak.
     */
    public static String getStreakMessage() {
    	return currentStreak;
    }
    
    /**
     * Prints currentStreak in chat.
     */
    public static void sendStreakMessage() {
    	Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(getStreakMessage()));
    }
    
    /**
     * Getter for the changeHudPos key binding.
     * 
     * @return KeyBinding for changing the position of messages
     */
    public static KeyBinding getHudPosKeyBinding() {
    	return changeHudPos;
    }

    /**
     * Getter for the current position messages will be displayed.
     * 
     * @return HudPosition where killstreaks will be displayed
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
