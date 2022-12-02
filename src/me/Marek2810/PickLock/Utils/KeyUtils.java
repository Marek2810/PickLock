package me.Marek2810.PickLock.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Marek2810.PickLock.PickLock;
import net.md_5.bungee.api.ChatColor;

public class KeyUtils {
	
	public static boolean isGoodKey(String lockID, int keyID, String keyType) {
		 int yamlKeyID = PickLock.yamlKeyID.get(lockID);
		 String yamlKeyType = PickLock.yamlKeyType.get(lockID);
		 if ( yamlKeyID == keyID && yamlKeyType.equalsIgnoreCase(keyType) ) return true;		 
		 return false;
	 }
	
	public static int setKey (Player player) {
		int keyID = getKeyID();
 		ItemStack item = new ItemStack(player.getInventory().getItemInMainHand());	    	
    	ItemMeta meta = item.getItemMeta();
    	List<String> lore = new ArrayList<String>();
    	lore.add("  ");
		lore.add(ChatColor.translateAlternateColorCodes('&', "&bID: " + keyID));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setAmount(1);
		player.getInventory().removeItem(item);
		meta.setLore(lore);
		item.setItemMeta(meta);
		player.getInventory().addItem(item);
 		return keyID;
 	}
	
	public static Boolean isKey(ItemStack item) {
    	String mat = item.getType().toString();       	
    	if (mat == "AIR") return false;
    	ItemMeta meta = item.getItemMeta();   	
    	if (! (meta.hasCustomModelData()) ) return false;	    	
        Integer keyCusModDat = meta.getCustomModelData();
        for (String key : PickLock.inst.getConfig().getConfigurationSection("keys").getKeys(false)) {
        	if ( !(PickLock.inst.getConfig().get("keys." + key + ".material").toString().equals(mat)) ) continue;
        	if (PickLock.inst.getConfig().getInt("keys." + key + ".customModelData") == keyCusModDat) {
        		return true;        		
        	}
        }        
        return false;
    }    
	
    public static Integer getKeyID() {
        int keyID = ThreadLocalRandom.current().nextInt(10000, 99999 + 1);
        while (isKeyUsed(keyID)) getKeyID();
        return keyID;
    }

    public static boolean isKeyUsed(Integer keyID) {
        ConfigurationSection locks = PickLock.data.getConfig().getConfigurationSection("locks");
        if (locks == null) return false;
        if (PickLock.yamlKeyID.containsValue(keyID)) return true;
        return false;
    }

    public static String getKeyType(ItemStack item) {       
        ItemMeta meta = item.getItemMeta();
        if (!isKey(item)) return null;
        Integer keyCusModDat = meta.getCustomModelData();
        for (String keys : PickLock.inst.getConfig().getConfigurationSection("keys").getKeys(false)) {
        	if (PickLock.inst.getConfig().getInt("keys." + keys + ".customModelData") == keyCusModDat) {
        		return PickLock.inst.getConfig().getString("keys." + keys + ".type");        		
        	}
        }        
        return null;
    }

}
