package me.Marek2810.PickLock.Lockpick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.RoleEngine.Utils.CharacterUtils;
import net.md_5.bungee.api.ChatColor;

public class LockpickUtils {
	
	public static List<Inventory> invs = new ArrayList<Inventory>();
	public static HashMap<Inventory, List<Integer>> invPins = new HashMap<Inventory, List<Integer>>();
	public static HashMap<Player, Integer> lastPin = new HashMap<Player, Integer>();
    
    public static HashMap<Integer, Double> levels = new HashMap<Integer, Double>();
    public static HashMap<UUID, Double> playerPickXP = new HashMap<UUID, Double>();

    public static ConfigurationSection lockpickingGUI = PickLock.inst.getConfig().getConfigurationSection("lockpicking.hook-gui");
    
	public static Inventory createInv(ItemStack itemInHand) { 
    	Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', 
    			lockpickingGUI.getString("inventory-title"))) ;
    	ItemStack fillItem = new ItemStack(Material.IRON_BARS);
    	ItemMeta fillMeta = fillItem.getItemMeta();
    	fillMeta.setDisplayName("  ");
    	fillItem.setItemMeta(fillMeta);
    	
    	ItemStack glassItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    	ItemMeta glassMeta = glassItem.getItemMeta();
    	glassMeta.setDisplayName("  ");
    	glassItem.setItemMeta(glassMeta);
    	
    	ItemStack hookItem = new ItemStack(itemInHand);
    	ItemMeta hookMeta = hookItem.getItemMeta();
    	hookItem.setAmount(1);
    	hookMeta.setDisplayName( ChatColor.translateAlternateColorCodes('&', 
    			lockpickingGUI.getString("tutorial-item-title")) );    	
    	if (lockpickingGUI.getStringList("lore") != null) {
    		List<String> hookLore = new ArrayList<String>();    		  	
	    	for (String loreLine : lockpickingGUI.getStringList("lore")) {
	    		hookLore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
	    	}
	    	hookMeta.setLore(hookLore);  
    	}    	
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
    	return inv;
    }
	
	public static boolean canBeLockpicked(String lockID) {
		if ( !(PickLock.locks.getConfig().getIntegerList("locks." + lockID + ".lockPath").isEmpty()) ) return true;
		return false;
	}
	
	public static List<Integer> getLockPath(Integer difficulty) {
        List<Integer> lockPath = new ArrayList<Integer>();        
        while (lockPath.size() < difficulty) {
            int pin = ThreadLocalRandom.current().nextInt(10, 9+difficulty+1);
            if (!lockPath.contains(pin)) {
                lockPath.add(pin);
            }
        }
        Collections.shuffle(lockPath);
        return lockPath;
    }
    
    public static Integer getLockPickDiff(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        Integer keyCusModDat = meta.getCustomModelData();
        for (String keys : PickLock.inst.getConfig().getConfigurationSection("keys").getKeys(false)) {
        	if ( !(PickLock.inst.getConfig().getString("keys." + keys + ".material").equals(item.getType().toString()) )) continue;
        	if (PickLock.inst.getConfig().get("keys." + keys + ".customModelData") != null) {
        		if (PickLock.inst.getConfig().getInt("keys." + keys + ".customModelData") != keyCusModDat) continue;           
        	}
        	return PickLock.inst.getConfig().getInt("keys." + keys + ".pick-difficulty");        	
        }        
        return null;
    }
	
	public static List<Integer> getPins(String lockID) {
		return PickLock.locks.getConfig().getIntegerList("locks." + lockID + ".lockPath");
	}
	
	public static Inventory setPins(Inventory inv, String lockID) {
		ItemStack pinItem = new ItemStack(Material.IRON_INGOT);
        ItemMeta pinMeta = pinItem.getItemMeta();
        pinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
        		lockpickingGUI.getString("pin-item-title")));
        pinItem.setItemMeta(pinMeta);
		for (int pin : getPins(lockID)) {
			inv.setItem(pin, pinItem);
		}
		return inv;
	}
	
	public static Boolean isHook(ItemStack item) {
    	String mat = item.getType().toString();       	
    	if (mat == "AIR") return false;    	
    	ConfigurationSection hooks = PickLock.inst.getConfig().getConfigurationSection("hooks");
        for (String hook : hooks.getKeys(false)) {
        	if ( !(hooks.getString(hook + ".material").equals(mat)) ) continue;
        	if (hooks.get(hook + ".customModelData") != null) {
        		ItemMeta meta = item.getItemMeta();
            	if (! (meta.hasCustomModelData()) ) return false;	    	
                Integer hookCusModDat = meta.getCustomModelData();
        		if (hooks.getInt(hook + ".customModelData") == hookCusModDat) {
            		return true;
        		}
        	}
        	else  {       		
        		return true;
        	}        	
        }        
        return false;
    }
	
    
   public static void generateLevels() {
	   	for (int i = 1; i <= PickLock.inst.getConfig().getInt("lockpicking.options.levels"); i++) {  
	   		if (i == 1) {
	   			levels.put(i, 0.0);
	   		}
	   		else if (i == 2) {
	   			levels.put(i, 15.0);
	   		}
	   		else {
	   			double needXP = Double.valueOf( LockpickUtils.roundDouble( (levels.get(i-1)+(levels.get(i-1)-levels.get(i-2) )*1.05) ,4) );	 
	
	   			levels.put(i, needXP );		   		
	   		}
	   	}
   }
   
   private static String getPlayerFilePath(Player player) {
	   String path = "players." + player.getUniqueId().toString();
	   if (PickLock.rp) {
		   path = "players." + player.getUniqueId().toString() + "." + CharacterUtils.activeChar.get(player);
	   }	   
	   return path;
   }
   
   
   public static double getPlayerXP(Player player) {
	   String path = getPlayerFilePath(player);
	   if (PickLock.playerData.getConfig().get(path + ".xp") == null ){
		   PickLock.playerData.getConfig().set(path + ".xp", 0.0);
		   PickLock.playerData.saveConfig();
	   }
	   return PickLock.playerData.getConfig().getDouble(path + ".xp");
   }
   
   public static int getPlayerLevel(Player player) {
	   for (int i = 1; i < levels.size(); i++) {
		   if (getPlayerXP(player) < levels.get(i)) return i-1;
	   }
	   return 1;
   }   
   
   public static double roundDouble(double nubmer, int deciamls) {
	   int multiplier = 1;	   
	   for (int i = 0; i < deciamls; i++) {
		   multiplier *= 10;
		}
	   nubmer *= multiplier;
	   nubmer = Math.round(nubmer);
	   nubmer /= multiplier;
	   return nubmer;
   }
   
   public static void removeItem(Player player) {
	   ItemStack item = player.getInventory().getItemInMainHand();
	   item.setAmount(item.getAmount()-1);
	   player.updateInventory();
   }
   
   public static double getPlayerChance() {
	   return LockpickUtils.roundDouble(ThreadLocalRandom.current().nextDouble(1, 101), 4);
   }
   
   public static double getBrokeChance(int playerLevel, int lastPin, double baseBrokeChance, double brokePinMult, double brokeLevelMult) {
	   double chance;
		if (playerLevel == 1) {
			if ( lastPin == 0 ) {
				chance = baseBrokeChance;
			}
			else {
				if (lastPin < 2) {
					chance = baseBrokeChance*brokePinMult;
				}
				else {
					chance = baseBrokeChance*Math.pow(brokePinMult, lastPin);	
				}						
			}	
		}
		else {
			if ( lastPin == 0 ) {
				chance = baseBrokeChance*Math.pow(brokeLevelMult, playerLevel-1);
			}
			else {
				if (lastPin < 2) {
					chance = baseBrokeChance*Math.pow(brokeLevelMult, playerLevel-1)*brokePinMult;
				}
				else {
					chance = baseBrokeChance*Math.pow(brokeLevelMult, playerLevel-1)*Math.pow(brokePinMult, lastPin);
				}						
			}
		}				
		return LockpickUtils.roundDouble(chance, 4);		
   }
   
   public static double getXP(int playerLevel, double baseXP, int pinsCount, double xpPinMult, double xpLevelMult) {
	   double addXP = 0;
		if (playerLevel == 1) {
			if (pinsCount == 1) {
				addXP = baseXP * xpPinMult;
			}
			else {
				addXP = baseXP * Math.pow(xpPinMult, pinsCount-1);
			}
		}
		else if (playerLevel == 2) {
			if (pinsCount == 1) {
				addXP = baseXP * xpLevelMult;
			}
			else {
				addXP = baseXP * xpLevelMult * Math.pow(xpPinMult, pinsCount-1);
			}							
		}
		else if (playerLevel > 2) {
			if (pinsCount == 1) {
				addXP = baseXP * Math.pow(xpLevelMult, playerLevel-1);
			}
			else {
				addXP = baseXP * Math.pow(xpLevelMult, playerLevel-1) * Math.pow(xpPinMult, pinsCount-1);		
			}						
		}
		return LockpickUtils.roundDouble(addXP, 4);
   }
   
   public static void addXP(int playerLevel, double baseXP, int pinsCount, double xpPinMult, double xpLevelMult, Player player) {
	   	double xp = getXP(playerLevel, baseXP, pinsCount, xpPinMult, xpLevelMult);
	   	setXP(player, xp);
   }
   
   public static void removeXP(int playerLevel, double baseXP, int pinsCount, double xpPinMult, double xpLevelMult, Player player) {
	   double xp = getXP(playerLevel, baseXP, pinsCount, xpPinMult, xpLevelMult);
	   xp *= -0.5;
	   setXP(player, xp);   
   }
   
   private static void setXP(Player player, double xp) {
	   String playerYAMLpath = getPlayerFilePath(player);
	   double playerXP = PickLock.playerData.getConfig().getDouble(playerYAMLpath + ".xp");
	   PickLock.playerData.getConfig().set(playerYAMLpath + ".xp", playerXP+xp);				
	   PickLock.playerData.saveConfig();
   }
	
}