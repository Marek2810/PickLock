package me.Marek2810.PickLock;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Lockpick implements Listener {

	List<Inventory> invs = new ArrayList<Inventory>();
    HashMap<Inventory, List<Integer>> invPins = new HashMap<Inventory, List<Integer>>();
    HashMap<Player, Integer> lastPin = new HashMap<Player, Integer>();
    
    public static HashMap<Integer, Double> levels = new HashMap<Integer, Double>();
    public static HashMap<UUID, Integer> playerPickLevel = new HashMap<UUID, Integer>();
    public static HashMap<UUID, Double> playerPickXP = new HashMap<UUID, Double>();
    
    public void createInv(Inventory inv) {    	    	
    	ItemStack fillItem = new ItemStack(Material.IRON_BARS);
    	ItemMeta fillMeta = fillItem.getItemMeta();
    	fillMeta.setDisplayName("  ");
    	fillItem.setItemMeta(fillMeta);
    	
    	ItemStack hookItem = new ItemStack(Material.STICK);
    	ItemStack glassItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
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
    	for (i = 10; i <= 17; i++) {
    		inv.setItem(i, glassItem);
    	}  
    	
    	//3rd row
    	inv.setItem(18, fillItem);
    	for (i = 19; i <= 26; i+=2) {
    		inv.setItem(i, fillItem);
    	}    	 	
    }
        
    public void generateLevels() {
    	DecimalFormat numberFormat = new DecimalFormat("#.0000");
    	for (int i = 1; i <= 5; i++) {  
    		//Main.console.sendMessage("i = " + i);
    		if (i == 1) {
    			levels.put(i, 0.0);
    			//Main.console.sendMessage("Need xp: " + 0);
				/*
				 * for (int j = 1; j <= 8; j++) { if (j == 1) {
				 * Main.console.sendMessage("Chance " + j + " :" + 97); } else {
				 * Main.console.sendMessage("Chance " + j + " :" +
				 * numberFormat.format(97*(Math.pow(1.05, j-1))) ); } } for (int j = 1; j <= 8;
				 * j++) { if (j == 1) { Main.console.sendMessage("XP " + j + " :" + 1.5); } else
				 * { Main.console.sendMessage("XP " + j + " :" +
				 * numberFormat.format(1.5*(Math.pow(1.05, j-1))) ); } }
				 */
    		}
    		else if (i == 2) {
    			levels.put(i, 15.0);
    			//Main.console.sendMessage("Need xp: " + 15);
				/*
				 * for (int j = 1; j <= 8; j++) { if (j == 1) {
				 * Main.console.sendMessage("Chance " + j + " :" +
				 * numberFormat.format(97*(Math.pow(0.995, i-1)) )); } else {
				 * Main.console.sendMessage("Chance " + j + " :" +
				 * numberFormat.format(97*(Math.pow(0.995, i-1))*(Math.pow(1.05, j-1))) ); } }
				 * for (int j = 1; j <= 8; j++) { if (j == 1) { Main.console.sendMessage("XP " +
				 * j + " :" + numberFormat.format(1.5*(Math.pow(1.015, i-1)))); } else {
				 * Main.console.sendMessage("XP " + j + " :" +
				 * numberFormat.format(1.5*(Math.pow(1.015, i-1))*(Math.pow(1.05, j-1))) ); } }
				 */
    		}
    		else {
    			double needXP = Double.valueOf( numberFormat.format( (levels.get(i-1)+(levels.get(i-1)-levels.get(i-2) )*1.05)) );	 
    			levels.put(i, needXP );
    			//Main.console.sendMessage("Need xp: " + needXP );
				
				
				/*
				 * for (int j = 1; j <= 8; j++) { if (j == 1) {
				 * Main.console.sendMessage("Chance " + j + " :" +
				 * numberFormat.format(97*(Math.pow(0.995, i-1)) )); } else {
				 * Main.console.sendMessage("Chance " + j + " :" +
				 * numberFormat.format(97*(Math.pow(0.995, i-1))*(Math.pow(1.05, j-1))) ); } }
				 * for (int j = 1; j <= 8; j++) { if (j == 1) { Main.console.sendMessage("XP " +
				 * j + " :" + numberFormat.format(1.5*(Math.pow(1.015, i-1)))); } else {
				 * Main.console.sendMessage("XP " + j + " :" +
				 * numberFormat.format(1.5*(Math.pow(1.015, i-1))*(Math.pow(1.05, j-1))) ); } }
				 */			   		
    		}
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
				int playerLevel = playerPickLevel.get( player.getUniqueId() );
				double playerXP = playerPickXP.get( player.getUniqueId() );		
				double chance = 97;
				double playerCahnce = ThreadLocalRandom.current().nextDouble(1, 100 + 1);
				ItemStack item = new ItemStack(player.getInventory().getItemInMainHand());	    	
		 		item.setAmount(1);
		    	DecimalFormat numberFormat = new DecimalFormat("#.0000");
				if (playerLevel == 1) {					
					if (lastPin.get(player) == 0) {
						if ( playerCahnce <= chance) {
							player.getInventory().removeItem(item);
							player.closeInventory();
						}
					}
					else if (lastPin.get(player) == 1) {
						chance = chance*1.05;
						if ( playerCahnce <= chance) {
							player.getInventory().removeItem(item);
							player.closeInventory();
						}
					}
					else {
						chance = chance*Math.pow(1.05, lastPin.get(player));
						if ( playerCahnce <= chance) {
							player.getInventory().removeItem(item);
							player.closeInventory();
						}
					}
				}
				else {
					if (lastPin.get(player) == 0) {
						chance = chance*(Math.pow(0.995, playerLevel-1));
						playerXP = playerXP-((1.5*Math.pow(1.015, playerLevel-1))*0.5);
						if ( playerCahnce <= chance) {
							player.getInventory().removeItem(item);
							player.closeInventory();
						}
					}
					else if (lastPin.get(player) == 1) {
						chance = chance*(Math.pow(0.995, playerLevel-1))*1.05;
						playerXP = playerXP-(((1.5*Math.pow(1.015, playerLevel-1))*1.05)*0.5);
						if ( playerCahnce <= chance) {
							player.getInventory().removeItem(item);
							player.closeInventory();
						}
					}
					else {
						chance = chance*(Math.pow(0.995, playerLevel-1))*Math.pow(1.05, lastPin.get(player));
						playerXP = playerXP-((1.5*Math.pow(1.015, playerLevel-1))*(Math.pow(1.05, lastPin.get(player)))*0.5);
						if ( playerCahnce <= chance) {
							player.getInventory().removeItem(item);
							player.closeInventory();
						}
					}
					playerPickXP.put( player.getUniqueId(), Double.valueOf(numberFormat.format(playerXP)) );
					Main.playerData.getConfig().set("players." + player.getUniqueId() + ".xp" , playerPickXP.get(player.getUniqueId()));
					Main.playerData.saveConfig();				
				}							
				player.sendMessage(ChatColor.RED + "Zlá západka.");
				
			}
		}
		if (pins.size() == lastPin.get(player)) {
			ItemStack item = new ItemStack(player.getInventory().getItemInMainHand());	    	
	 		item.setAmount(1);
	 		player.getInventory().removeItem(item);
	    	DecimalFormat numberFormat = new DecimalFormat("#.0000");
			double playerXP = playerPickXP.get( player.getUniqueId() );
			int playerLevel = playerPickLevel.get( player.getUniqueId() );
			playerXP = playerXP+((1.5*Math.pow(1.015, playerLevel-1))*(Math.pow(1.05, lastPin.get(player))));
			playerPickXP.put( player.getUniqueId(), Double.valueOf(numberFormat.format(playerXP)) );
			Main.playerData.getConfig().set("players." + player.getUniqueId() + ".xp" , playerPickXP.get(player.getUniqueId()));
			if ( playerXP >= levels.get(playerLevel) ) {
				for (int i = playerLevel; i <= 251; i++) {
					if ( playerXP >= levels.get(i+1) ) {
						playerPickLevel.put(player.getUniqueId(), i+1);
						Main.playerData.getConfig().set("players." + player.getUniqueId() + ".level",
								playerPickLevel.get(player.getUniqueId()) );
					}
					else break;					
				}
			}			
			Main.playerData.saveConfig();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aÚspešne si lockpickol zámok."));
			Block block = Main.lock.lockpicking.get(player);
			Location loc = block.getLocation();
			String lockID = Main.lock.getLockID(loc);
			Main.yamlIsLocked.replace(lockID, false);
			Main.locks.getConfig().set("locks." + lockID + ".locked", false);
    		Main.locks.saveConfig();
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
        ConfigurationSection locks = Main.locks.getConfig().getConfigurationSection("locks");
        if (locks == null) return null;
        ConfigurationSection lock = Main.locks.getConfig().getConfigurationSection("locks." + lockID);
        if (lock == null) return null;        
        List<Integer> pins = new ArrayList<Integer>();
        for (Integer pin : Main.locks.getConfig().getIntegerList("locks." + lockID + ".lockPath")) {
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
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();
		if ( !(Main.playerData.getConfig().contains("players." + player.getUniqueId()) )) {
			Main.playerData.getConfig().set("players." + player.getUniqueId() + ".nick", player.getName());
    		Main.playerData.getConfig().set("players." + player.getUniqueId() + ".level", 1);
    		Main.playerData.getConfig().set("players." + player.getUniqueId() + ".xp", 0.0);    		
    		playerPickLevel.put(player.getUniqueId(), 1);
    		playerPickXP.put(player.getUniqueId(), 0.0);
    	}
		else {
			playerPickLevel.put(player.getUniqueId(), Main.playerData.getConfig().getInt("players." + player.getUniqueId() + ".level"));
			playerPickXP.put(player.getUniqueId(), Main.playerData.getConfig().getDouble("players." + player.getUniqueId() + ".xp"));
		}		
		Main.playerData.saveConfig();
		return;
	}
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = (Player) event.getPlayer();
		playerPickLevel.remove(player.getUniqueId());
		playerPickXP.remove(player.getUniqueId());
    	return;    	
    }
}
