package scribee.cvchelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = CvcHelper.MODID, version = CvcHelper.VERSION)
public class CvcHelper {
    public static final String MODID = "cvchelper";
    public static final String VERSION = "0.1";
    
    // Section symbol used by minecraft for text formatting
	private static final String S = String.valueOf('\u00a7');
    // Player's username
	private static String name = "";
	// Regex that matches kill and death messages
	private static Pattern killfeedPattern = Pattern.compile(S + "r" + S + "[34](\\w{1,16}) " + S + "r" + S + "f(\\W\\W?\\W?) " + S + "r" + S + "[34](\\w{1,16})");
    // Total number of kills the player has gotten since their last death
	private static int totalStreak = 0;
	
	private static Map<String, Integer> weaponStreaks = new HashMap<String, Integer>();
	
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	MinecraftForge.EVENT_BUS.register(this);
	}
    
    @SubscribeEvent
	public void onChatEvent(ClientChatReceivedEvent event) {
		String message = event.message.getFormattedText();
		
		if (name.equals("")) {
			name = Minecraft.getMinecraft().thePlayer.getDisplayNameString();
			System.out.println(name);
		}

		Matcher matcher = killfeedPattern.matcher(message);
		// If the chat message matches the pattern of a kill message
		if (matcher.find()) {
			// If it was the player's kill (first name in the message is their username)
			if (matcher.group(1).equals(name)) {
				totalStreak++;
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Current streak: " + EnumChatFormatting.RESET + totalStreak));
				Minecraft.getMinecraft().thePlayer.addChatMessage(updateWeaponStreaks(matcher.group(2)));
			}
			// If it was the player's death (last name in the message is their username)
			else if (matcher.group(3).equals(name)) {
				totalStreak = 0;
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Reset streaks." + EnumChatFormatting.RESET));
				resetWeaponStreaks();
			}
			else {
				System.out.println("Randy kill");
			}
		}
		// Other types of deaths that don't follow the same pattern
		// If player is sent to a different server (or starts a new game)
		else if (message.startsWith(S + "aSending you to ") && message.endsWith("!" + S + "r")) {
			totalStreak = 0;
			System.out.println("New game");
			resetWeaponStreaks();
		}
		// If the player kills themself with a grenade or firebomb
		else if ((message.startsWith(S + "r" + S + "f" + String.valueOf('\u926c') + " ") || message.startsWith(S + "r" + S + "f" + String.valueOf('\u9273') + " ")) && message.substring(10).equals(name + S + "r")) {
			totalStreak = 0;
			System.out.println("Suicide nade");
			resetWeaponStreaks();
		}
		// If they die from the C4
		else if (message.startsWith(S + "r" + S + "f" + String.valueOf('\u9276') + " ") && message.substring(10).equals(name + S + "r")) {
			totalStreak = 0;
			System.out.println("C4");
			resetWeaponStreaks();
		}
		// If they die from fall damage
		else if (message.startsWith(S + "r" + S + "f" + String.valueOf('\u9271') + String.valueOf('\u9272') + " ") && message.substring(11).equals(name + S + "r")) {
			totalStreak = 0;
			System.out.println("Fall damage");
			resetWeaponStreaks();
		}
		else if (message.contains(String.valueOf('\u926c')) || message.contains(String.valueOf('\u9273'))) {
			System.out.println("Nade message: " + message);
		}
		else if (message.contains("Scribee")) {
			System.out.println("Other message: " + message);
		}
	}
    
    private ChatComponentText updateWeaponStreaks(String weapon) {
    	int kills = 0;
    	
    	// If there was one, remove the headshot symbol to just get the characters that make up the gun
    	if (weapon.substring(weapon.length() - 1).equals(String.valueOf('\u9270'))) {
    		weapon = weapon.substring(0, weapon.length() - 1);
    	}
    	
    	// If the weapon already exists as a key in the map, increment its value
    	if (weaponStreaks.containsKey(weapon)) {
    		kills = weaponStreaks.get(weapon) + 1;
    		System.out.println("Added 1 kill for " + weapon);
    	}
    	// If the weapon doesn't exist in the map, the streak is 1
    	else {
    		kills = 1;
    		System.out.println("Created map entry for storing " + weapon + " kills");
    	}
    	
    	// Update the map
    	weaponStreaks.put(weapon, kills);
    	
    	// Return the streak message to be printed for the player
    	return new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Current " + weapon + " streak: " + EnumChatFormatting.RESET + kills);
    }
    
    private void resetWeaponStreaks() {
    	weaponStreaks.forEach(new BiConsumer<String, Integer>() {
			@Override
			public void accept(String k, Integer v) {
				weaponStreaks.put(k, 0);
				System.out.println("Reset " + k);
			}
		});
    }
}
