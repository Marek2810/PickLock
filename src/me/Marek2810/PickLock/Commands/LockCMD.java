package me.Marek2810.PickLock.Commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Marek2810.PickLock.Main;
import net.md_5.bungee.api.ChatColor;

public class LockCMD implements CommandExecutor {
	
	public static HashMap<Player, Boolean> removingLock = new HashMap<Player, Boolean>();
	public static HashMap<Player, Boolean> infoLock = new HashMap<Player, Boolean>();
	
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
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission!"));
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
			else if ( args[0].equalsIgnoreCase("remove")|| args[0].equalsIgnoreCase("odstranit")|| args[0].equalsIgnoreCase("odstran") || args[0].equalsIgnoreCase("zrusit") ) {
				if (! (sender instanceof Player) ) {   					
					String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
    				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
    				return true;
				}
				else {
					Player player = (Player) sender;					
					if (player.hasPermission("picklock.lock.remove")) {
						if ( removingLock.containsKey(player) || infoLock.containsKey(player) ) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Dokonči akciu z predchadzajúceho príkazu."));
							return true;
						}
						removingLock.put(player, true);
						String msg = Main.inst.getConfig().getString("messages.to-remove-lock");
		    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						return true;
					}					
					else {
						//no permissions
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission!"));
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
						if ( removingLock.containsKey(player) || infoLock.containsKey(player) ) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Dokonči akciu z predchadzajúceho príkazu."));
							return true;
						}
						infoLock.put(player, true);
						String msg = Main.inst.getConfig().getString("messages.to-lock-info");
		    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						return true;
					}	
					else {
						//no permissions
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission!"));
						return true;
					}
				}				
			}
		}		
		return false;
	}
	
	public void onPlayerQuit (PlayerQuitEvent event) {
		Player player = (Player) event.getPlayer();
		if (removingLock.containsKey(player)) removingLock.remove(player);
		if (infoLock.containsKey(player)) infoLock.remove(player);
		return;
	}
}