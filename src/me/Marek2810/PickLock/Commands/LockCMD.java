package me.Marek2810.PickLock.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Marek2810.PickLock.Main;
import net.md_5.bungee.api.ChatColor;

public class LockCMD implements CommandExecutor {
	
	public static ArrayList<Player> removingLock = new ArrayList<Player>();
	public static ArrayList<Player> infoLock = new ArrayList<Player>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (args.length == 0) {
			// /zamok
			String msg = Main.inst.getConfig().getString("messages.ussage");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg + "/zamok <odstran/odstranit/zrusit>"));    			
			return true;
		}
		else if (args.length > 0 ) {
			if (! (sender instanceof Player) ) {   					
				String msg = Main.inst.getConfig().getString("messages.console-cannot-do-that");
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));  	   
				return true;
			}
			// /zamok odstranit/zrusit
			else if ( args[0].equalsIgnoreCase("odstranit")|| args[0].equalsIgnoreCase("odstran") || args[0].equalsIgnoreCase("zrusit") ) {
				Player player = (Player) sender;
				removingLock.add(player);
				String msg = Main.inst.getConfig().getString("messages.to-remove-lock");
    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
				return true;			
			}	
			else if ( args[0].equalsIgnoreCase("info") ) {
				Player player = (Player) sender;
				infoLock.add(player);
				String msg = Main.inst.getConfig().getString("messages.to-lock-info");
    			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
				return true;
			}
			else {
				// /zamok
				String msg = Main.inst.getConfig().getString("messages.ussage");
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg + "/zamok <odstranit/zrusit>"));    			
				return true;
			}
		}
		
		return false;
	}

}
