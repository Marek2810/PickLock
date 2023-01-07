package me.Marek2810.PickLock.Lockpick;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Marek2810.PickLock.PickLock;
import me.Marek2810.PickLock.Utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;

public class LockpickGUIlistener implements Listener {
	
	private ConfigurationSection lockPickOptions = PickLock.inst.getConfig().getConfigurationSection("lockpicking.options");
	private double baseXP = lockPickOptions.getDouble("base-xp");
	private double baseBrokeChance = lockPickOptions.getDouble("base-broke-hook-chance");
	private double brokeLevelMult = lockPickOptions.getDouble("broke-hook-level-multipler");
	private double brokePinMult = lockPickOptions.getDouble("broke-hook-pin-multipler");
	private double xpLevelMult = lockPickOptions.getDouble("xp-level-multipler");
	private double xpPinMult = lockPickOptions.getDouble("xp-pin-multipler");
	
	public static HashMap<Player, String> lockpicking = new HashMap<Player, String>();

	@EventHandler
	public void onGUIclick(InventoryClickEvent event) {
		if (!(LockpickUtils.invs.contains(event.getInventory()))) return;
		if (event.getCurrentItem() == null ) return;
		if (event.getCurrentItem().getItemMeta() == null ) return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null ) return;	
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		List<Integer> pins = LockpickUtils.invPins.get(event.getInventory());
		ItemStack item = event.getCurrentItem();
		if (item.getType().equals(Material.IRON_INGOT)) {
			int playerLevel = LockpickUtils.getPlayerLevel(player);
			int lastPin = LockpickUtils.lastPin.get(player);
			if ( event.getSlot() == pins.get(lastPin ) ) {
				//good pin
				event.getClickedInventory().setItem(pins.get( lastPin ), null);
				if (event.getClickedInventory().getItem(pins.get( lastPin )+9) == null ) {
					event.getClickedInventory().setItem(pins.get( lastPin )+9, item);
				}
				else if (event.getClickedInventory().getItem(pins.get( lastPin )-9) == null ) {
					event.getClickedInventory().setItem(pins.get( lastPin )-9, item);
				}
				LockpickUtils.lastPin.remove(player);
				LockpickUtils.lastPin.put(player, lastPin+1);	
				lastPin = LockpickUtils.lastPin.get(player);
				if (pins.size() == LockpickUtils.lastPin.get(player) ) {
					//unlock					
					LockpickUtils.addXP(playerLevel, baseXP, lastPin, xpPinMult, xpLevelMult, player);
					LockpickUtils.removeItem(player);								
					new BukkitRunnable() {
							public void run() {
								player.closeInventory();
								cancel();
							}
						}.runTaskLater(PickLock.getPlugin(PickLock.class), 5);		
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("on-lockpick") ));
					PickLock.locks.getConfig().set("locks." + lockpicking.get(player)  + ".locked", false);
					PickLock.locks.saveConfig();
					return;
				}
			}
			else {
				//bad pin
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("bad-pin") ));
				double chance = LockpickUtils.getBrokeChance(playerLevel, lastPin, baseBrokeChance, brokePinMult, brokeLevelMult);
				double playerChance = LockpickUtils.getPlayerChance();	
				LockpickUtils.removeXP(playerLevel, baseXP, lastPin+1, xpPinMult, xpLevelMult, player);
				if (playerChance <= chance) {
					LockpickUtils.removeItem(player);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatUtils.getMessage("on-hook-broke") ));
				}
				new BukkitRunnable() {
					public void run() {
						player.closeInventory();
						cancel();
					}
				}.runTaskLater(PickLock.getPlugin(PickLock.class), 5);	
			}
		}
	}
	
	@EventHandler
	public void onGUIclose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		if (LockpickUtils.invs.contains(inv)) {
			LockpickUtils.invs.remove(inv);		
			LockpickUtils.invPins.remove(inv);
			LockpickUtils.lastPin.remove(event.getPlayer());
			lockpicking.remove(event.getPlayer());
		}
	}	
}
