package me.Marek2810.PickLock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Lockpick implements Listener {

	List<Inventory> invs = new ArrayList<Inventory>();
    HashMap<Inventory, List<Integer>> invPins = new HashMap<Inventory, List<Integer>>();
    HashMap<Player, Integer> lastPin = new HashMap<Player, Integer>();
    
    public void createInv(Inventory inv) {    	    	
    	ItemStack fillItem = new ItemStack(Material.IRON_BARS);
    	ItemMeta fillMeta = fillItem.getItemMeta();
    	fillMeta.setDisplayName("  ");
    	fillItem.setItemMeta(fillMeta);
    	
    	ItemStack hookItem = new ItemStack(Material.STICK);
    	ItemMeta hookMeta = hookItem.getItemMeta();
    	hookMeta.setDisplayName( ChatColor.translateAlternateColorCodes('&', "&aNávod") );
    	List<String> hookLore = new ArrayList<String>();
    	hookLore.add("  ");
    	hookLore.add(ChatColor.translateAlternateColorCodes('&', "&bPosuň západky v správnom"));
    	hookLore.add(ChatColor.translateAlternateColorCodes('&', "&bporadí pre otvorenie zámku."));
    	hookMeta.setCustomModelData(2810201);
    	hookMeta.setLore(hookLore);
    	hookItem.setItemMeta(hookMeta);   	  	
    	
    	int i;
    	//1st row
    	for (i = 0; i <= 8; i+=2) {
    		inv.setItem(i, fillItem);
    	}
    	//2nd row
    	inv.setItem(9, hookItem);
    	
    	//3rd row
    	inv.setItem(18, fillItem);
    	for (i = 19; i <= 26; i+=2) {
    		inv.setItem(i, fillItem);
    	}    	 	
    }
    
    @EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!(invs.contains(event.getInventory()))) return;
		if (event.getCurrentItem() == null ) return;
		if (event.getCurrentItem().getItemMeta() == null ) return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null ) return;
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		List<Integer> pins = invPins.get(event.getInventory());
		ItemStack air = new ItemStack(Material.AIR);
		ItemStack iron = new ItemStack(Material.IRON_INGOT);		
		if (event.getClickedInventory().getItem(event.getSlot()).getType().toString() == "IRON_INGOT") {
			if (event.getSlot() == pins.get( lastPin.get(player) )) {
					event.getClickedInventory().setItem(pins.get( lastPin.get(player) ), air);
				if (event.getClickedInventory().getItem(pins.get( lastPin.get(player) )+9) == null ) {
					event.getClickedInventory().setItem(pins.get( lastPin.get(player) )+9, iron);
				}
				else if (event.getClickedInventory().getItem(pins.get( lastPin.get(player) )-9) == null ) {
					event.getClickedInventory().setItem(pins.get( lastPin.get(player) )-9, iron);
				}
				 lastPin.put(player, lastPin.get(player)+1);
			}				
			else {
				player.sendMessage(ChatColor.RED + "Zlá západka.");
			}
		}
		if (pins.size() == lastPin.get(player)) {
			player.sendMessage(ChatColor.GREEN + "WUALAAA!");
			Block block = Main.lock.lockpicking.get(player);
			Location loc = block.getLocation();
			String lockID = Main.lock.getLockID(loc);
			Main.yamlIsLocked.replace(lockID, false);
			Main.data.getConfig().set("locks." + lockID + ".locked", false);
    		Main.data.saveConfig();
    		new BukkitRunnable() {
				public void run() {
					player.closeInventory();
					cancel();
				}
			}.runTaskLater(Main.getPlugin(Main.class), 5);
		}		
		return;
	}
    
    public List<Integer> getPins(String lockID) {    	
        ConfigurationSection locks = Main.data.getConfig().getConfigurationSection("locks");
        if (locks == null) return null;
        ConfigurationSection lock = Main.data.getConfig().getConfigurationSection("locks." + lockID);
        if (lock == null) return null;        
        List<Integer> pins = new ArrayList<Integer>();
        for (Integer pin : Main.data.getConfig().getIntegerList("locks." + lockID + ".lockPath")) {
        	pins.add((int) pin);        	
        }
        
        return pins;
    }
    
    public void setPins(Inventory inv, String lockID) {
    	ItemStack pinItem = new ItemStack(Material.IRON_INGOT);
        ItemMeta pinMeta = pinItem.getItemMeta();
        pinMeta.setDisplayName("Západka");
        pinItem.setItemMeta(pinMeta);
        for (int pin :  getPins(lockID) ) {
        	inv.setItem(pin, pinItem);        	
        }
    }
    
    public void openLockpickInv(Player player, String lockID) {
    	Inventory inv = Bukkit.createInventory(null, 27,ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Lockpick");
    	createInv(inv);
    	setPins(inv, lockID);
    	invPins.put(inv, getPins(lockID));
    	invs.add(inv);
    	player.openInventory(inv);
    	lastPin.put(player, 0);
    }
}
