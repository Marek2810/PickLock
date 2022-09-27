package me.Marek2810.PickLock.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Marek2810.PickLock.Main;
import net.md_5.bungee.api.ChatColor;

public class Picklock implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    		if (args.length == 0) {
    			// /picklock
    			String msg = Main.inst.getConfig().getString("messages.ussage");
    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg + "/picklock reload"));    			
    			return true;
    		}
    		else if (args.length > 0) {
    			// /picklock reload
    			if (args[0].equalsIgnoreCase("reload")) {
    				String msg = Main.inst.getConfig().getString("messages.reload");
        			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        			Main.inst.reloadConfig();
        			return true;
    			}    			 		   		
    			else {
    				String msg = Main.inst.getConfig().getString("messages.unknown-cmd");
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    				return true;
    			}
    		}    	   	
    	return false;
    }
	
    
}