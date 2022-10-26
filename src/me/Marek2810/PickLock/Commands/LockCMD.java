package me.Marek2810.PickLock.Commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Marek2810.PickLock.Main;
import net.md_5.bungee.api.ChatColor;

public class LockCMD implements CommandExecutor {
	
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
				String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
				return true;
			}
			else {
				Player player = (Player) sender;
				if (player.hasPermission("picklock.lock")) {
					String msg = Main.inst.getConfig().getString("messages.ussage");
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg + "/zamok <remove/odstran/odstranit/zrusit> \n"
							+ "/zamok <info>"));    			
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
		else if (args.length > 0 ) {
			if (! (sender instanceof Player) ) {   					
				String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
				return true;
			}
			// /zamok odstranit/zrusit
			else if ( args[0].equalsIgnoreCase("remove")|| args[0].equalsIgnoreCase("odstranit")
					|| args[0].equalsIgnoreCase("odstran") || args[0].equalsIgnoreCase("zrusit") ) {
				if (! (sender instanceof Player) ) {   					
					String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
    				return true;
				}
				else {
					Player player = (Player) sender;					
					if (player.hasPermission("picklock.lock.remove")) {
						if ( activeCommandActions(player) ) {
							String msg = Main.inst.getConfig().getString("messages.finish-previos-action");
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
							return true;
						}
						removingLock.put(player, true);
						String msg = Main.inst.getConfig().getString("messages.to-remove-lock");
		    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
			else if ( args[0].equalsIgnoreCase("info") ) {
				if (! (sender instanceof Player) ) {   					
					String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
    				return true;
				}
				else {
					Player player = (Player) sender;
					if (player.hasPermission("picklock.lock.info")) {
						if ( activeCommandActions(player) ) {
							String msg = Main.inst.getConfig().getString("messages.finish-previos-action");
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
							return true;
						}
						infoLock.put(player, true);
						String msg = Main.inst.getConfig().getString("messages.to-lock-info");
		    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
			else if ( args[0].equalsIgnoreCase("admin") ) {
				if (! (sender instanceof Player) ) {   					
					String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
    				return true;
				}
				else {
					Player player = (Player) sender;
					if (args.length == 1) {						
						if (player.hasPermission("picklock.admin.lock")) {
							String msg = Main.inst.getConfig().getString("messages.ussage");
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg + 
									"/zamok admin <remove/odstran/odstranit/zrusit> \n"
									+ "/zamok admin <lock/zamkni> \n"
									+ "/zamok admin <unlock/odomkni>" ));	
							return true;
						}
						else {
							//no permissions
							String msg = Main.inst.getConfig().getString("messages.no-permission");
				    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
							return true;							
						}							
					}
					if (args.length > 1) {
						if ( args[1].equalsIgnoreCase("remove")|| args[1].equalsIgnoreCase("odstranit")
								|| args[1].equalsIgnoreCase("odstran") || args[1].equalsIgnoreCase("zrusit") ) {
							if (player.hasPermission("picklock.admin.lock.remove")) {
								if ( activeCommandActions(player) ) {
									String msg = Main.inst.getConfig().getString("messages.finish-previos-action");
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
									return true;
								}
								adminRemovingLock.put(player, true);
								String msg = Main.inst.getConfig().getString("messages.to-remove-lock");
				    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								return true;
							}
							else {
								//no permissions
								String msg = Main.inst.getConfig().getString("messages.no-permission");
		        				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		    					return true;
							}
						}
						// /zamok admin lock/zamkni
						else if ( args[1].equalsIgnoreCase("lock") || args[1].equalsIgnoreCase("zamkni") ) {
							if (player.hasPermission("picklock.admin.lock.lock")) {
								if ( activeCommandActions(player) ) {
									String msg = Main.inst.getConfig().getString("messages.finish-previos-action");
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
									return true;
								}
								adminLock.put(player, true);
								String msg = Main.inst.getConfig().getString("messages.to-lock");
				    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								return true;
							}
						}
						// /zamok admin unlock/odomkni
						else if ( args[1].equalsIgnoreCase("unlock") || args[1].equalsIgnoreCase("odomkni") ) {
							if (player.hasPermission("picklock.admin.lock.unlock")) {
								if ( activeCommandActions(player) ) {
									String msg = Main.inst.getConfig().getString("messages.finish-previos-action");
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
									return true;
								}
								adminUnlock.put(player, true);
								String msg = Main.inst.getConfig().getString("messages.to-unlock");
				    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								return true;
							}
						}
					}
				}					
			}
		}		
		return false;
	}
	
	public boolean activeCommandActions(Player player) {
		if ( !(removingLock.containsKey(player)) && !(infoLock.containsKey(player))
				&& !(adminRemovingLock.containsKey(player)) && !(adminLock.containsKey(player))
				&& !(adminUnlock.containsKey(player)) ) return false;
		return true;
	}
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent event) {
		Player player = (Player) event.getPlayer();
		if (removingLock.containsKey(player)) removingLock.remove(player);		
		if (infoLock.containsKey(player)) infoLock.remove(player);
		if (adminRemovingLock.containsKey(player)) adminRemovingLock.remove(player);
		if (adminLock.containsKey(player)) adminLock.remove(player);
		if (adminUnlock.containsKey(player)) adminUnlock.remove(player);
		return;
	}
}