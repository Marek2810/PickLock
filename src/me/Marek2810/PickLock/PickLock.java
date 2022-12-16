package me.Marek2810.PickLock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.PickLock.Commands.KeyCommand;
import me.Marek2810.PickLock.Commands.LockCommand;
import me.Marek2810.PickLock.Commands.PickLockCommand;
import me.Marek2810.PickLock.Commands.Listeners.AdminLockCmdListener;
import me.Marek2810.PickLock.Commands.Listeners.AdminRemoveCmdListener;
import me.Marek2810.PickLock.Commands.Listeners.AdminUnlockCmdListener;
import me.Marek2810.PickLock.Commands.Listeners.InfoCmdListener;
import me.Marek2810.PickLock.Commands.Listeners.PlayerListener;
import me.Marek2810.PickLock.Commands.Listeners.RemoveCmdListener;
import me.Marek2810.PickLock.Files.DataManager;
import me.Marek2810.PickLock.Listener.BlockListener;
import me.Marek2810.PickLock.Listener.ClickNoKey;
import me.Marek2810.PickLock.Listener.ClickWithKey;
import me.Marek2810.PickLock.Listener.PlaceSecBlockListener;
import me.Marek2810.PickLock.Listener.RedstoneListener;
import net.md_5.bungee.api.ChatColor;

public class PickLock extends JavaPlugin implements Listener {

    public static PickLock inst;   
    public static DataManager locks;
	public static LockCommand lockCMD;
	public static boolean rp;
	
	public static ConsoleCommandSender console;
	public static String logPrefix;
    
    public static Set<String> yamlKeys;    
    public static List<String> chests;
    public static List<String> doors;
    public static List<String> trapdoors;
    public static HashMap<String, Boolean> yamlIsLocked = new HashMap<String, Boolean>();
    public static HashMap<String, Integer> yamlKeyID = new HashMap<String, Integer>();
    public static HashMap<String, String> yamlKeyType = new HashMap<String, String>();
    public static HashMap<String, List<Object>> yamlLocations = new HashMap<String, List<Object>>();  
    
    @Override
    public void onEnable() {    	
    	rp = false;
    	inst = this;    	
    	console = this.getServer().getConsoleSender();    	
    	locks = new DataManager(this, "locks.yml");
    	this.saveDefaultConfig();
    	this.getCommand("picklock").setExecutor(new PickLockCommand());
    	this.getCommand("key").setExecutor(new KeyCommand());
		this.getCommand("lock").setExecutor(new LockCommand());
		this.getServer().getPluginManager().registerEvents(new AdminLockCmdListener(), this);
		this.getServer().getPluginManager().registerEvents(new AdminRemoveCmdListener(), this);
		this.getServer().getPluginManager().registerEvents(new AdminUnlockCmdListener(), this);
		this.getServer().getPluginManager().registerEvents(new InfoCmdListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new RemoveCmdListener(), this);
		this.getServer().getPluginManager().registerEvents(new RedstoneListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlaceSecBlockListener(), this);
		
		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
		this.getServer().getPluginManager().registerEvents(new ClickNoKey(), this);
		this.getServer().getPluginManager().registerEvents(new ClickWithKey(), this);
		
    	logPrefix = "&7[&6PickLock&7] ";
    	//loading block to locks
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading blocks abble to locks..."));
    	chests = this.getConfig().getStringList("locking.chests");
    	doors = this.getConfig().getStringList("locking.doors");
    	trapdoors = this.getConfig().getStringList("locking.trapdoors");
    	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aLoaded blocks to lcoks."));
    	if (locks.getConfig().getConfigurationSection("locks") != null) {
    		//Loading locked blocks
    		console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&eLoading locked blocks..."));
        	yamlKeys = locks.getConfig().getConfigurationSection("locks").getKeys(false);
        	if (yamlKeys != null) {
        		for (String lockID : yamlKeys) {
	        		yamlIsLocked.put(lockID, locks.getConfig().getBoolean("locks." + lockID + ".locked") );
	        		yamlKeyID.put(lockID, locks.getConfig().getInt("locks." + lockID + ".keyID"));
	        		yamlKeyType.put(lockID, locks.getConfig().getString("locks." + lockID + ".keyType"));
	        		List<Object> locs = new ArrayList<Object>();
	        		for (int i = 1; i <= 4; i++ ) {
	        			Object get = locks.getConfig().get("locks." + lockID + ".location" + i);	        			
	        			if (get != null ) {
	        				locs.add(locks.getConfig().get("locks." + lockID + ".location" + i));
	        			}
	        			else {
	        				break;
	        			}	        			
	        		}
	        		yamlLocations.put(lockID, locs);
	        	}
            	console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + "&aLoaded locked blocks."));   	
	        }
        }
    	if(Bukkit.getPluginManager().getPlugin("RoleEngine") != null) {
    		console.sendMessage(ChatColor.RED + "RP acitavated");
            rp = true;
    	}
    }

    @Override
    public void onDisable() {

    }
}