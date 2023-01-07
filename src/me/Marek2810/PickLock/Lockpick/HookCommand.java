package me.Marek2810.PickLock.Lockpick;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.PickLock.Utils.ItemUtils;
import net.md_5.bungee.api.ChatColor;

public class HookCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if (! (sender instanceof Player) ) {   					
		String msg = PickLock.inst.getConfig().getString("messages.console-cannot-do-that");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
		return true;
	}
	// /sperhak
	else if (ItemUtils.getItem("hooks.", "basic") != null) {
	//else if (getItem("hooks.", "basic") != null) {
		Player player = (Player) sender;
		String msg = PickLock.inst.getConfig().getString("messages.get-hook");
		player.getInventory().addItem(ItemUtils.getItem("hooks.", "basic"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));	        			
		return true;
	}
	return false;
}
	
}
