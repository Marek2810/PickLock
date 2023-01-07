package me.Marek2810.PickLock.Listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.PickLock.Utils.LockUtils;
import net.md_5.bungee.api.ChatColor;

public class BlockBreakListener implements Listener {

	@EventHandler
	 public void onBlockBreak(BlockBreakEvent event) {
		 Block block = event.getBlock();
    	//Is block we can lock
    	if ( PickLock.chests.contains(block.getType().toString()) 
    			|| PickLock.doors.contains(block.getType().toString())
    			|| PickLock.trapdoors.contains(block.getType().toString()) ) {
			 Player player = (Player) event.getPlayer();
    		 if (LockUtils.hasLock(event.getBlock().getLocation())) {
    			 event.setCancelled(true);     			
    			 String msg = PickLock.inst.getConfig().getString("messages.on-brake-with-lock");
    			 player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)); 
    			 return;			
    		 }
		 }
		 return;
	 }
}
