package me.Marek2810.PickLock.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Marek2810.PickLock.PickLock;
import net.md_5.bungee.api.ChatColor;

public class ItemUtils {

	public static ItemStack getItem(String itemType, String type) {
    	if ( !(PickLock.inst.getConfig().contains(itemType + type + ".type")) ) return null;    	
    	Material itype = Material.getMaterial(PickLock.inst.getConfig().getString(itemType + type + ".material"));
        ItemStack item = new ItemStack(itype);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
        		PickLock.inst.getConfig().getString(itemType + type + ".name")) );
        meta.setCustomModelData(PickLock.inst.getConfig().getInt(itemType + type + ".customModelData") );
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
	
	public static void setKey (Player player, int keyID) {
		ItemStack itemInMainHand = new ItemStack(player.getInventory().getItemInMainHand());
		if ( !(KeyUtils.isKey(itemInMainHand)) ) return;
		ItemMeta meta = itemInMainHand.getItemMeta();
		List<String> lore = new ArrayList<String>();
		itemInMainHand.setAmount(1);
		player.getInventory().removeItem(itemInMainHand);
		if ( meta.hasLore() ) {
			lore = meta.getLore();			
			lore.set(1, ChatColor.translateAlternateColorCodes('&', "&bID: " + keyID));			
		}
		else {
	    	lore.add("  ");
			lore.add(ChatColor.translateAlternateColorCodes('&', "&bID: " + keyID));
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemInMainHand.setItemMeta(meta);
		player.getInventory().addItem(itemInMainHand);
		return;
	}
}
