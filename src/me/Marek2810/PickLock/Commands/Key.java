package me.Marek2810.PickLock.Commands;

import java.util.ArrayList;
import java.util.List;

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

public class Key implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    		if (args.length == 0) {
    			// /kluc
    			if (! (sender instanceof Player) ) {   					
					String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
    				return true;
				}
    			else {
    				Player player = (Player) sender;
    				if (player.hasPermission("picklock.key")) {
    					String msg = Main.inst.getConfig().getString("messages.ussage");
    	    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg + "/key get/získej/získaj <typ> \n"
    	    					+ msg + "/key process/opracuj"));    			
    	    			return true;
    				}
    				else {
    					//no permissions
    					String msg = Main.inst.getConfig().getString("messages.no-permission");
        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    					return true;
    				}
    			}  			
    		}
    		else if (args.length > 0) {
    			if (! (sender instanceof Player) ) {   					
					String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
    				return true;
				}
    			else {
    				Player player = (Player) sender;
    				// /key get
    				if (args[0].equalsIgnoreCase("get") ||
        					args[0].equalsIgnoreCase("ziskaj") || args[0].equalsIgnoreCase("ziskej")  ) {   
    					if (args.length == 1) {
	    					if (player.hasPermission("picklock.key")) {
	        					String msg = Main.inst.getConfig().getString("messages.ussage");
				    			player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg + "&7/kluc get/získej/ziskaj <typ>"));
				    			return true;				    			
	    					}
	    					else {
	    						//no permissions
	    						String msg = Main.inst.getConfig().getString("messages.no-permission");
	            				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	        					return true;
	    					}
			    		}
    					else if (args.length > 1) {
    						if (player.hasPermission("picklock.key.get")) {
    			    			if (getItem("keys.", args[1]) != null) {
    			    				String msg = Main.inst.getConfig().getString("messages.get-key");
    				        		player.getInventory().addItem(getItem("keys.", args[1]));
    				    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));	        			
    				        		return true;
    				    		}
    				    		else {
    				    			String msg = Main.inst.getConfig().getString("messages.unknown-key");
    				        		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    				        		return true;
    				    		}
    		    			}
    		    			else {
    		    				//no permissions
    		    				String msg = Main.inst.getConfig().getString("messages.no-permission");
    	        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    	    					return true;
    		    			} 
    					}	    			
    				}
    				// /key process
    				else if ( args[0].equalsIgnoreCase("opracuj") || args[0].equalsIgnoreCase("process") ) {
        				if (! (sender instanceof Player) ) {   					
    						String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
    	    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  
    	    				return true;
    					}
    	    			if (args.length == 1) {
    		    			if (player.hasPermission("picklock.key.process")) {
    		    				String msg = Main.inst.getConfig().getString("messages.ussage");
    		    				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg + "&7/kluc process/opracuj <ID>"));
    			    			return true;
    		    			}
    		    			else {
    		    				//no permissions
    		    				String msg = Main.inst.getConfig().getString("messages.no-permission");
    	        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    	    					return true;
    		    			}
    		    		}
    		    		else if (args.length > 1) {
    		    			//máme ID
    		    			//is player holding key?
    		    			if (player.hasPermission("picklock.key.process")) {
    		    				if (Main.lock.isKey(player.getInventory().getItemInMainHand())) {
    			    				setKey(player, Integer.valueOf(args[1]));
    			    				return true;
    			    			}
    			    			else {
    			    				String msg = Main.inst.getConfig().getString("messages.need-to-hold-key");
        	        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    			    				return true;
    			    			}
    		    			}
    		    			else {
    		    				//no permissions
    		    				String msg = Main.inst.getConfig().getString("messages.no-permission");
    	        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    	    					return true;
    		    			}
    		    		}	    			
    	    		}    		
        			else {
    					String msg = Main.inst.getConfig().getString("messages.unknown-cmd");
            			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            			return true;
    				}
    			}
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
	
	public void setKey (Player player, int keyID) {
		ItemStack itemInMainHand = new ItemStack(player.getInventory().getItemInMainHand());
		if ( !(Main.lock.isKey(itemInMainHand)) ) return;
		ItemMeta meta = itemInMainHand.getItemMeta();
		if ( meta.hasLore() ) {
			List<String> lore = meta.getLore();
			itemInMainHand.setAmount(1);
			player.getInventory().removeItem(itemInMainHand);
			lore.set(1, ChatColor.translateAlternateColorCodes('&', "&bID: " + keyID));
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			itemInMainHand.setItemMeta(meta);
			player.getInventory().addItem(itemInMainHand);
			return;
		}
		else {
	    	List<String> lore = new ArrayList<String>();
			player.getInventory().removeItem(itemInMainHand);
	    	lore.add("  ");
			lore.add(ChatColor.translateAlternateColorCodes('&', "&bID: " + keyID));
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.setLore(lore);
			itemInMainHand.setItemMeta(meta);
			player.getInventory().addItem(itemInMainHand);
	 		return;
		}
	}
}
	
