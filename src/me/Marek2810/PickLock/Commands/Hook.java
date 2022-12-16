package me.Marek2810.PickLock.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Marek2810.PickLock.Main;
import net.md_5.bungee.api.ChatColor;

public class Hook implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if (! (sender instanceof Player) ) {   					
		String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
		return true;
	}
	// /sperhak
	else if (getItem("hooks.", "basic") != null) {
		Player player = (Player) sender;
		String msg = Main.inst.getConfig().getString("messages.get-hook");
		player.getInventory().addItem(getItem("hooks.", "basic"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));	        			
		return true;
	}
	return false;
}
	public ItemStack getItem(String itemType, String type) {
    	if ( !(Main.inst.getConfig().contains(itemType + type + ".type")) ) return null;    	
    	Material itype = Material.getMaterial(Main.inst.getConfig().getString(itemType + type + ".material"));
        ItemStack item = new ItemStack(itype);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
        		Main.inst.getConfig().getString(itemType + type + ".name")) );
        meta.setCustomModelData(Main.inst.getConfig().getInt(itemType + type + ".customModelData") );
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

}