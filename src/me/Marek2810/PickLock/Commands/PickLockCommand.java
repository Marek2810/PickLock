package me.Marek2810.PickLock.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.PickLock.Utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;

public class PickLockCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    		if (args.length > 0) {
    			// /picklock reload
    			if (args[0].equalsIgnoreCase("reload")) {
    				if (sender.hasPermission("picklock.picklock.reload")) {
            			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("reload") ));
            			PickLock.inst.reloadConfig();
            			return true;
        			} 
    				else {
    					//no permissions
        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission") ));
    					return true;
        			}        			
    			}
    			else if (args[0].equalsIgnoreCase("levels")) {
    				Main.lockpick.generateLevels();
    				Main.console.sendMessage(ChatColor.GREEN + "DONE!");
    				return true;
    			}
    			else {
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("unknown-cmd") ));
    				return true;
    			}
    				
    		}    	   	
    	return false;
    }   
}