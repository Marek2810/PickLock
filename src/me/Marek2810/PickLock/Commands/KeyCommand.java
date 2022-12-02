package me.Marek2810.PickLock.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Marek2810.PickLock.Utils.ChatUtils;
import me.Marek2810.PickLock.Utils.ItemUtils;
import me.Marek2810.PickLock.Utils.KeyUtils;
import net.md_5.bungee.api.ChatColor;

public class KeyCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    		if (args.length == 0) {
    			// /kluc
    			if (! (sender instanceof Player) ) {   					
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("console-cannot-do-that")));  	
    				return true;
				}
    			else {
    				Player player = (Player) sender;
    				if (player.hasPermission("picklock.key")) {
    	    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("ussage") + "/key get/získej/získaj <typ> \n"
    	    					+ ChatUtils.getMessage("ussage") + "/key process/opracuj"));    			
    	    			return true;
    				}
    				else {
    					//no permissions
        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission")));
    					return true;
    				}
    			}  			
    		}
    		else if (args.length > 0) {
    			if (! (sender instanceof Player) ) {   					
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("console-cannot-do-that")));  	
    				return true;
				}
    			else {
    				Player player = (Player) sender;
    				// /key get
    				if (args[0].equalsIgnoreCase("get") ||
        					args[0].equalsIgnoreCase("ziskaj") || args[0].equalsIgnoreCase("ziskej")  ) {   
    					if (args.length == 1) {
	    					if (player.hasPermission("picklock.key")) {
				    			player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("ussage") + "&7/kluc get/získej/ziskaj <typ>"));
				    			return true;				    			
	    					}
	    					else {
	    						//no permissions
	    	    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission")));
	    						return true;
	    					}
			    		}
    					else if (args.length > 1) {
    						if (player.hasPermission("picklock.key.get")) {
    			    			if (ItemUtils.getItem("keys.", args[1]) != null) {
    				        		player.getInventory().addItem(ItemUtils.getItem("keys.", args[1]));
    				    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("get-key") ));	        			
    				        		return true;
    				    		}
    				    		else {
    				        		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("unknown-key")));
    				        		return true;
    				    		}
    		    			}
    		    			else {
    		    				//no permissions
    	        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission")));
    							return true;
    		    			} 
    					}	    			
    				}
    				// /key process
    				else if ( args[0].equalsIgnoreCase("opracuj") || args[0].equalsIgnoreCase("process") ) {
    					if (! (sender instanceof Player) ) {   					
    	    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("console-cannot-do-that")));  	
    	    				return true;
    					}
    	    			if (args.length == 1) {
    		    			if (player.hasPermission("picklock.key.process")) {
    		    				player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("ussage") + "&7/kluc process/opracuj <ID>"));
    			    			return true;
    		    			}
    		    			else {
    		    				//no permissions
    	        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission") ));
    							return true;
    		    			}
    		    		}
    		    		else if (args.length > 1) {
    		    			//máme ID
    		    			//is player holding key?
    		    			if (player.hasPermission("picklock.key.process")) {
    		    				if (KeyUtils.isKey(player.getInventory().getItemInMainHand())) {
    		    					ItemUtils.setKey(player, Integer.valueOf(args[1]));
    			    				return true;
    			    			}
    			    			else {
        	        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("need-to-hold-key") ));
    			    				return true;
    			    			}
    		    			}
    		    			else {
    		    				//no permissions
    	        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission")));
    							return true;
    		    			}
    		    		}	    			
    	    		}    		
        			else {
        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("unknown-cmd")));
						return true;
    				}
    			}
    		}
    	return false;
	}	
	
}
	
