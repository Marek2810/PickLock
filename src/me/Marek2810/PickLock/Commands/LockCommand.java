package me.Marek2810.PickLock.Commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.Marek2810.PickLock.Utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;

public class LockCommand implements CommandExecutor {
	
	public static HashMap<Player, Boolean> removingLock = new HashMap<Player, Boolean>();
	public static HashMap<Player, Boolean> infoLock = new HashMap<Player, Boolean>();
	public static HashMap<Player, Boolean> adminRemovingLock = new HashMap<Player, Boolean>();
	public static HashMap<Player, Boolean> adminLock = new HashMap<Player, Boolean>();
	public static HashMap<Player, Boolean> adminUnlock = new HashMap<Player, Boolean>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (args.length == 0) {
			// /zamok
			if (! (sender instanceof Player) ) {   					
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("console-cannot-do-that") ));  	   
				return true;
			}
			else {
				Player player = (Player) sender;
				if (player.hasPermission("picklock.lock")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("ussage") + "/zamok <remove/odstran/odstranit/zrusit> \n"
							+ "/zamok <info>"));    			
					return true;
				}	
				else {
					//no permissions
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission") ));
					return true;
				}
			}
			
		}
		else if (args.length > 0 ) {
			if (! (sender instanceof Player) ) {   					
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("console-cannot-do-that") ));  	   
				return true;
			}
			// /zamok odstranit/zrusit
			else {
				Player player = (Player) sender;					
				if ( args[0].equalsIgnoreCase("remove")|| args[0].equalsIgnoreCase("odstranit")
						|| args[0].equalsIgnoreCase("odstran") || args[0].equalsIgnoreCase("zrusit") ) {
					if (player.hasPermission("picklock.lock.remove")) {
						if ( ChatUtils.activeCommandActions(player) ) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("finish-previos-action") ));
							return true;
						}
						removingLock.put(player, true);
		    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("to-remove-lock") ));
						return true;
					}					
					else {
						//no permissions
        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission") ));
    					return true;
					}				
				}			
				else if ( args[0].equalsIgnoreCase("info") ) {
					if (player.hasPermission("picklock.lock.info")) {
						if ( ChatUtils.activeCommandActions(player) ) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("finish-previos-action") ));
							return true;
						}
						infoLock.put(player, true);
		    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("to-lock-info") ));
						return true;
					}	
					else {
						//no permissions
        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission") ));
    					return true;
					}								
				}
				else if ( args[0].equalsIgnoreCase("admin") ) {
					if (args.length == 1) {
						if (player.hasPermission("picklock.admin.lock")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("ussage") + 
									"/zamok admin <remove/odstran/odstranit/zrusit> \n"
									+ "/zamok admin <lock/zamkni> \n"
									+ "/zamok admin <unlock/odomkni>" ));	
							return true;
						}
						else {
							//no permissions
				    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission") ));
							return true;							
						}							
					}
					if (args.length > 1) {
						if ( args[1].equalsIgnoreCase("remove")|| args[1].equalsIgnoreCase("odstranit")
								|| args[1].equalsIgnoreCase("odstran") || args[1].equalsIgnoreCase("zrusit") ) {
							if (player.hasPermission("picklock.admin.lock.remove")) {
								if ( ChatUtils.activeCommandActions(player) ) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("finish-previos-action") ));
									return true;
								}
								adminRemovingLock.put(player, true);
				    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("to-remove-lock") ));
								return true;
							}
							else {
								//no permissions
		        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("no-permission") ));
		    					return true;
							}
						}
						// /zamok admin lock/zamkni
						else if ( args[1].equalsIgnoreCase("lock") || args[1].equalsIgnoreCase("zamkni") ) {
							if (player.hasPermission("picklock.admin.lock.lock")) {
								if ( ChatUtils.activeCommandActions(player) ) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("finish-previos-action") ));
									return true;
								}
								adminLock.put(player, true);
				    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("to-lock") ));
								return true;
							}
						}
						// /zamok admin unlock/odomkni
						else if ( args[1].equalsIgnoreCase("unlock") || args[1].equalsIgnoreCase("odomkni") ) {
							if (player.hasPermission("picklock.admin.lock.unlock")) {
								if ( ChatUtils.activeCommandActions(player) ) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("finish-previos-action") ));
									return true;
								}
								adminUnlock.put(player, true);
				    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("to-unlock") ));
								return true;
							}
						}
					}							
				}
			}		
		}
		return false;
	}
}