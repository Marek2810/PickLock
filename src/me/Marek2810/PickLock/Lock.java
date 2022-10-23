package me.Marek2810.PickLock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Lock implements Listener {		

	 @EventHandler
	    public void onClick(PlayerInteractEvent event) {	     	
	    	//Right click
	        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	        	Player player = (Player) event.getPlayer();
	        	Material typeOfClickeBlock = event.getClickedBlock().getType();	              	
	        	//If player execute command /zamok info
	        	if ( Main.lockCMD.infoLock.containsKey(player) && Main.lockCMD.infoLock.get(player) == true  ) {
	        		if ( Main.chests.contains(typeOfClickeBlock.toString())
							|| Main.doors.contains(typeOfClickeBlock.toString())
							|| Main.trapdoors.contains(typeOfClickeBlock.toString()) ) {
	        			if ( hasLock(event.getClickedBlock().getLocation()) ) {
	        				String lockID = getLockID( event.getClickedBlock().getLocation() );
	        				ConfigurationSection lock = Main.data.getConfig().getConfigurationSection("locks." + lockID);
	        				player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
	        						"&7------------ \n"
	        						+ "&aLockID: &e" + lockID + "\n"
	        						+ "&aOwner: &e" + 
	        							Bukkit.getServer().getOfflinePlayer(UUID.fromString(lock.getString("owner"))).getName()
	        							+ "\n" 
	        						+ "&aKey type: &e" + lock.getString("keyType") + "\n" 
	        						+ "&aKey ID: &e" + lock.getInt("keyID") + "\n"
	        						+ "&aLocked: &e" + lock.getBoolean("locked") + "\n"
	        						+ "&7------------"));
	        				event.setCancelled(true);	
							Main.lockCMD.infoLock.remove(player);
							return;
	        			}
	        			else {
	        				String msg = Main.inst.getConfig().getString("messages.no-lock");
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	        				event.setCancelled(true);	
							Main.lockCMD.infoLock.remove(player);
							return;
	        			}
	        		}			        		
	        	}	
	        	//If player execute command /zamok zrusit/odstranit
	        	else if ( Main.lockCMD.removingLock.containsKey(player) && Main.lockCMD.removingLock.get(player) == true ) {
					if ( Main.chests.contains(typeOfClickeBlock.toString())
							|| Main.doors.contains(typeOfClickeBlock.toString())
							|| Main.trapdoors.contains(typeOfClickeBlock.toString()) ) {
						if ( hasLock(event.getClickedBlock().getLocation()) ) {							
							String lockID = getLockID( event.getClickedBlock().getLocation() );
							String UUID = Main.data.getConfig().getString("locks." + lockID + ".owner");
							if (UUID.equals(player.getUniqueId().toString())) {							
								Main.yamlIsLocked.remove(lockID);
								Main.yamlKeyID.remove(lockID);
								Main.yamlKeys.remove(lockID);
								Main.yamlLocations.remove(lockID);
								Main.data.getConfig().set("locks." + lockID, null);
								Main.data.saveConfig();							
								String msg = Main.inst.getConfig().getString("messages.remove-lock");
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));							
								event.setCancelled(true);	
								Main.lockCMD.removingLock.remove(player);
								return;
							}
							else {
								String msg = Main.inst.getConfig().getString("messages.remove-lock-not-owned");
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));							
								event.setCancelled(true);	
								Main.lockCMD.removingLock.remove(player);
								return;	
							}
						}
						else {
							String msg = Main.inst.getConfig().getString("messages.no-lock");
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
							Main.lockCMD.removingLock.remove(player);
							event.setCancelled(true);
							return;
						}
					}
					else {
						String msg = Main.inst.getConfig().getString("messages.no-lock");
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						Main.lockCMD.removingLock.remove(player);
						event.setCancelled(true);
						return;
					}
				}
	        	// /lock admin remove
	        	else if ( Main.lockCMD.adminRemovingLock.containsKey(player) 
	        			&& Main.lockCMD.adminRemovingLock.get(player) == true ) {
					if ( Main.chests.contains(typeOfClickeBlock.toString())
							|| Main.doors.contains(typeOfClickeBlock.toString())
							|| Main.trapdoors.contains(typeOfClickeBlock.toString()) ) {
						if ( hasLock(event.getClickedBlock().getLocation()) ) {							
							String lockID = getLockID( event.getClickedBlock().getLocation() );
							Main.yamlIsLocked.remove(lockID);
							Main.yamlKeyID.remove(lockID);
							Main.yamlKeys.remove(lockID);
							Main.yamlLocations.remove(lockID);
							Main.data.getConfig().set("locks." + lockID, null);
							Main.data.saveConfig();							
							String msg = Main.inst.getConfig().getString("messages.remove-lock");
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));							
							event.setCancelled(true);	
							Main.lockCMD.adminRemovingLock.remove(player);
							return;							
						}
						else {
							String msg = Main.inst.getConfig().getString("messages.no-lock");
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
							Main.lockCMD.adminRemovingLock.remove(player);
							event.setCancelled(true);
							return;
						}
					}
					else {
						String msg = Main.inst.getConfig().getString("messages.no-lock");
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						Main.lockCMD.adminRemovingLock.remove(player);
						event.setCancelled(true);
						return;
					}
				}
	        	// /lock admin lock
	        	else if ( Main.lockCMD.adminLock.containsKey(player) 
	        			&& Main.lockCMD.adminLock.get(player) == true ) {
	        		if ( Main.chests.contains(typeOfClickeBlock.toString())
							|| Main.doors.contains(typeOfClickeBlock.toString())
							|| Main.trapdoors.contains(typeOfClickeBlock.toString()) ) {
	        			if ( hasLock(event.getClickedBlock().getLocation()) ) {
	        				if ( !(isLocked(event.getClickedBlock().getLocation())) ) {	                            		
	                    		//unlock
								String lockID = getLockID( event.getClickedBlock().getLocation() );
		        				Main.yamlIsLocked.replace(lockID, true);
	                    		Main.data.getConfig().set("locks." + lockID + ".locked", true);
	                    		Main.data.saveConfig();
	                    		String msg = Main.inst.getConfig().getString("messages.on-lock");
	                    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	                    		Main.lockCMD.adminLock.remove(player);
	                    		event.setCancelled(true);                    		
	                    		return;
	                    	}
	                    	else {
	                    		//locked
	                    		String msg = Main.inst.getConfig().getString("messages.is-not-locked");
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								Main.lockCMD.adminLock.remove(player);
								event.setCancelled(true);
								return;
	                    	}
	        			}
	        			else {
	        				//not locked
	        				String msg = Main.inst.getConfig().getString("messages.no-lock");
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
							Main.lockCMD.adminLock.remove(player);
							event.setCancelled(true);
							return;
	        			}	        				        			
	        		}
	        	}
	        	// /lock admin unlock
	        	else if ( Main.lockCMD.adminUnlock.containsKey(player) 
	        			&& Main.lockCMD.adminUnlock.get(player) == true ) {
	        		if ( Main.chests.contains(typeOfClickeBlock.toString())
							|| Main.doors.contains(typeOfClickeBlock.toString())
							|| Main.trapdoors.contains(typeOfClickeBlock.toString()) ) {
	        			if (hasLock(event.getClickedBlock().getLocation())) {
	        				if ( isLocked(event.getClickedBlock().getLocation()) ) {	                            		
	                    		//unlock
								String lockID = getLockID( event.getClickedBlock().getLocation() );
	                    		Main.yamlIsLocked.replace(lockID, false);
	                    		Main.data.getConfig().set("locks." + lockID + ".locked", false);
	                    		Main.data.saveConfig();
	                    		String msg = Main.inst.getConfig().getString("messages.on-unlock");
	                    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	                    		Main.lockCMD.adminUnlock.remove(player);
	                    		event.setCancelled(true);
	                    		return;
	                    	}
	                    	else {
	                    		//unlocked     
	                    		String msg = Main.inst.getConfig().getString("messages.is-not-locked");
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								Main.lockCMD.adminUnlock.remove(player);
								event.setCancelled(true);
								return;
	                    	}
	        			}
	        			else {
	        				//not lock
	        				String msg = Main.inst.getConfig().getString("messages.no-lock");
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
							Main.lockCMD.adminUnlock.remove(player);
							event.setCancelled(true);
							return;
	        			}	        				        			
	        		}	        		
	        	}
	        	//Is block we can lock
	        	else if ( Main.chests.contains(typeOfClickeBlock.toString())
	        			|| Main.doors.contains(typeOfClickeBlock.toString())
	        			|| Main.trapdoors.contains(typeOfClickeBlock.toString()) ) {
	        		ItemStack itemInMainHand = new ItemStack(player.getInventory().getItemInMainHand());
	        		//Clicking with key
	        		if (isKey(itemInMainHand)) {	        			
	        			//Have lock
	        			if ( hasLock(event.getClickedBlock().getLocation() ) ) {
	        				ItemMeta meta = itemInMainHand.getItemMeta();
	        				boolean hasLore = meta.hasLore();
	        				//Check if key used
	        				if (hasLore) {
	        					List<String> lore = meta.getLore();
	        					String lore1 = lore.get(1);
	                            int keyID = Integer.parseInt(lore1.replaceAll("[^0-9]", ""));
	                            String lockID = getLockID(event.getClickedBlock().getLocation());
	                            if (lockID == null ) {
	                            	String msg = Main.inst.getConfig().getString("messages.error");
	                            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	                            	event.setCancelled(true);
	                            	return;
	                            }	                   
	                            //Check if key is correct
	                            if ( isGoodKey(lockID, keyID) ) {
	                            	//Is locked?
	                            	if ( isLocked(event.getClickedBlock().getLocation()) ) {	                            		
	                            		//unlock
	                            		Main.yamlIsLocked.replace(lockID, false);
	                            		Main.data.getConfig().set("locks." + lockID + ".locked", false);
	                            		Main.data.saveConfig();
	                            		String msg = Main.inst.getConfig().getString("messages.on-unlock");
	                            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	                            		event.setCancelled(true);
	                            		return;
	                            	}
	                            	else {
	                            		//lock
	                            		Main.yamlIsLocked.replace(lockID, true);
	                            		Main.data.getConfig().set("locks." + lockID + ".locked", true);
	                            		Main.data.saveConfig();
	                            		String msg = Main.inst.getConfig().getString("messages.on-lock");
	                            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	                            		event.setCancelled(true);
	                            		return;
	                            	}
	                            }
	                            else {
		        					//Wrong key
	                            	String msg = Main.inst.getConfig().getString("messages.wrong-key");
		    						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		    						event.setCancelled(true);
		    						return;
		        				}
	        				}
	        				else {
	        					//Wrong key (no lore)
	        					String msg = Main.inst.getConfig().getString("messages.wrong-key");
	    						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	    						event.setCancelled(true);
	    						return;
	        				}
	        			}	        			
	        			//Not having a lock
	        			else {
	        				ItemMeta meta = itemInMainHand.getItemMeta();
	        				boolean hasLore = meta.hasLore();
	        				//Is key used?
	        				if (hasLore) {
	        					String msg = Main.inst.getConfig().getString("messages.key-is-used");
	    						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		                        event.setCancelled(true);
		        				return;
	        				}
	        				else {
		    					//Make a lock	        					
		    					ArrayList<Location> lockLocations = new ArrayList<Location>();		    					
		    					lockLocations.add( event.getClickedBlock().getLocation() );		    					
			    				if (Main.chests.contains( event.getClickedBlock().getType().toString() )) {
			    					if ( isDoubleChest( event.getClickedBlock() ) ) {		
			    						lockLocations.add( getSecondChest(event.getClickedBlock()) );
			    					}
			    				}
			    				else if (Main.doors.contains( event.getClickedBlock().getType().toString() )) {
			    					lockLocations.add( getSecondDoorHlaf( event.getClickedBlock()) );	
			    					if ( isDoubleDoor( event.getClickedBlock() ) ) {
			    						Block secDoorBlock = getSecondDoor( event.getClickedBlock() );
			    						lockLocations.add( secDoorBlock.getLocation() );
			    						lockLocations.add( getSecondDoorHlaf( secDoorBlock ) );
			    					}			    					
			    				}
			    				makeLock(event, lockLocations);	                                                                       
		                        String msg = Main.inst.getConfig().getString("messages.on-first-lock");
		                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		                        event.setCancelled(true);
		                        return;
		    				}
	        			}
	        		}	        		
	        		//Not key
	        		else {
	        			if ( isLocked(event.getClickedBlock().getLocation()) ) {
	        				String msg = Main.inst.getConfig().getString("messages.is-locked");
	                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	        				event.setCancelled(true);
	        				return;
	        			}
	        		}
	        	}
	        } 
	 }
	 
	 @EventHandler
	 public void onBlockBreak(BlockBreakEvent event) {
		 Block block = event.getBlock();
     	//Is block we can lock
     	if ( Main.chests.contains(block.getType().toString()) 
     			|| Main.doors.contains(block.getType().toString())
     			|| Main.trapdoors.contains(block.getType().toString()) ) {
			 Player player = (Player) event.getPlayer();
     		 if (hasLock(event.getBlock().getLocation())) {
     			 event.setCancelled(true);     			
     			 String msg = Main.inst.getConfig().getString("messages.on-brake-with-lock");
     			 player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)); 
     			 return;			
     		 }
		 }
		 return;
	 }
	 
	 public boolean hasLock(Location loc) {
		 Object check = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
		 for (Object o : Main.yamlLocations.keySet()) {
			 if(o == null) return false;	
			 String lockID = o.toString();
			 Object lock = Main.yamlLocations.get(lockID);
				if (  lock.toString().contains(check.toString()) ) return true;				
			 }
		 return false;
	 }
	 
	 public String getLockID(Location loc){
		 Object check = loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
		 for (Object o : Main.yamlLocations.keySet()) {
			 if(o == null) return null;	
			 Object lock = Main.yamlLocations.get(o.toString());			 
				if (  lock.toString().contains(check.toString()) ) {
					return o.toString();				
				}
			 }
		 return null;
	 }
	 
	 public boolean isLocked(Location loc) {
		 if (!hasLock(loc)) return false;
		 String lockID = getLockID(loc);
		 Boolean isLocked = Main.yamlIsLocked.get(lockID);
		 if ( !isLocked ) return false;
		 return true;
	 }
	 
	 public boolean isGoodKey(String lockID, int keyID) {
		 int yamlKeyID = Main.yamlKeyID.get(lockID);
		 if (yamlKeyID == keyID) return true;		 
		 return false;
	 }
	 
	 public Block getSecondDoor(Block block) {
	    	Location loc = block.getLocation();
	    	if (block.getBlockData() instanceof Door door) {	    		
	           if (door.getHinge().toString().equalsIgnoreCase("left")) {
	        	   if ( door.getFacing().toString().equalsIgnoreCase("WEST") ) {
	        		   Location loc1 = loc.add(0, 0, -1);	        		  
	        		   return loc1.getBlock();
	        	   }
	        	   else if ( door.getFacing().toString().equalsIgnoreCase("EAST") )  {
	        		   Location loc1 = loc.add(0, 0, 1);
	        		   return loc1.getBlock();
	        	
	        	   }
	        	   else if (  door.getFacing().toString().equalsIgnoreCase("NORTH") )  {
	        		   Location loc1 = loc.add(1, 0, 0);
	        		   return loc1.getBlock();
	        	   }
	        	   else if (  door.getFacing().toString() == "SOUTH" )  {
	        		   Location loc1 = loc.add(-1, 0, 0);
	        		   return loc1.getBlock();
	        	   }
	           }
	           else if (door.getHinge().toString().equalsIgnoreCase("right")) {
	        	   if ( door.getFacing().toString().equalsIgnoreCase("WEST") ) {
	        		   Location loc1 = loc.add(0, 0, 1);
	        		   return loc1.getBlock();
	        	   }
	        	   else if ( door.getFacing().toString().equalsIgnoreCase("EAST") ) {
	        		   Location loc1 = loc.add(0, 0, -1);
	        		   return loc1.getBlock();
	        	   }
	        	   else if ( door.getFacing().toString().equalsIgnoreCase("NORTH") ) {
	        		   Location loc1 = loc.add(-1, 0, 0);
	        		   return loc1.getBlock();
	        	   }
	        	   else if ( door.getFacing().toString() == "SOUTH" )  {
	        		   Location loc1 = loc.add(1, 0, 0);
	        		   return loc1.getBlock();
	        	   }
	           }
	        }
	    	return block;
	    }	
	 
	 	public Boolean isDoubleDoor(Block block) {
	 		Location loc = block.getLocation();
	 		if (loc.clone().add(1, 0, 0).getBlock().getBlockData() instanceof Door) {
	 			return true;
	 		}
	 		else if (loc.clone().add(0, 0, 1).getBlock().getBlockData() instanceof Door) {
	 			return true;
	 		} 
	 		else if (loc.clone().add(-1, 0, 0).getBlock().getBlockData() instanceof Door) {
	 			return true;
	 		} 	
	 		else if (loc.clone().add(0, 0, -1).getBlock().getBlockData() instanceof Door) {
	 			return true;
	 		} 	
	 		return false;
	 	}
	 
	 	public int setKey (Player player) {
			int keyID = getKeyID();
	 		ItemStack item = new ItemStack(player.getInventory().getItemInMainHand());	    	
	    	ItemMeta meta = item.getItemMeta();
	    	List<String> lore = new ArrayList<String>();
	    	lore.add("  ");
			lore.add(ChatColor.translateAlternateColorCodes('&', "&bID: " + keyID));
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setAmount(1);
			player.getInventory().removeItem(item);
			meta.setLore(lore);
			item.setItemMeta(meta);
			player.getInventory().addItem(item);
	 		return keyID;
	 	}
	 
	    public void makeLock(PlayerInteractEvent event, List<Location> locations) {
	    	int lockID;			
			if (  Main.yamlKeys != null ) {
				lockID = Main.yamlKeys.size()+1;
				while (Main.yamlKeys.contains( Integer.toString(lockID) )){
					lockID++;
				}
			}
			else {
				lockID = 1;
			}
			String sLockID = Integer.toString(lockID);
	    	Player player = (Player) event.getPlayer();	    	
	    	int keyID = setKey(player);
			String material = event.getClickedBlock().getType().toString();			
			//Owner
	        Main.data.getConfig().set("locks." + lockID + ".owner", player.getUniqueId().toString());
	        //location
	        List<Object> locs = new ArrayList<Object>();
	        int i = 1;
	        for (Location loc : locations) {
    			locs.add(loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
    			Main.data.getConfig().set("locks." + lockID + ".location" + i, loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());	
    			i++;
	        }
	        Main.yamlLocations.put(sLockID, locs);
	        //material
	        Main.data.getConfig().set("locks." + lockID + ".material", material);	
	        //key type
	        Main.data.getConfig().set("locks." + lockID + ".keyType", getKeyType( event.getPlayer().getInventory().getItemInMainHand() ));
	        //keyID
	        Main.data.getConfig().set("locks." + lockID + ".keyID", keyID);
	        //locked
	        Main.data.getConfig().set("locks." + lockID + ".locked", true);
			Main.data.saveConfig();		
			Main.yamlKeys = Main.data.getConfig().getConfigurationSection("locks").getKeys(false);
        	Main.yamlIsLocked.put(sLockID, true);
        	Main.yamlKeyID.put(sLockID, keyID);        		
	    	return;
	    }
	    
	    public Location getSecondDoorHlaf(Block block) {
	    	Location loc = block.getLocation();
	    	if (block.getBlockData() instanceof Door door) {
	    		if (door.getHalf().toString().equalsIgnoreCase("BOTTOM")) {
	    			loc.add(0, 1, 0);
	    			return loc;	
	    		}
	    		else if (door.getHalf().toString().equalsIgnoreCase("TOP")) {
					loc.add(0, -1, 0);
					return loc;
	    		}	    		
	    	}
	    	return loc;
	    }
	    
	    public Boolean isDoubleChest(Block block) {
	    	if (block.getBlockData() instanceof Chest chest) {
	           if ( !(chest.getType().toString() == "SINGLE") ) return true;
	        }			
	    	return false;
	    }
	    
	    public Location getSecondChest(Block block) {
	    	Location loc = block.getLocation();
	    	if (block.getBlockData() instanceof Chest chest) {	    		
	           if (chest.getType().toString().equalsIgnoreCase("left")) {
	        	   if ( chest.getFacing().toString().equalsIgnoreCase("WEST") ) {
	        		   loc.add(0, 0, -1);
	        		   return loc;	        	
	        	   }
	        	   else if ( chest.getFacing().toString().equalsIgnoreCase("EAST") )  {
	        		   loc.add(0, 0, 1);
	        		   return loc;	        	
	        	   }
	        	   else if (  chest.getFacing().toString().equalsIgnoreCase("NORTH") )  {
	        		   loc.add(1, 0, 0);
	        		   return loc;
	        	   }
	        	   else if (  chest.getFacing().toString() == "SOUTH" )  {
	        		   loc.add(-1, 0, 0);
	        		   return loc;
	        	   }
	           }
	           else if (chest.getType().toString().equalsIgnoreCase("right")) {
	        	   if ( chest.getFacing().toString().equalsIgnoreCase("WEST") ) {
	        		   loc.add(0, 0, 1);
	        		   return loc;		        	
	        	   }
	        	   else if ( chest.getFacing().toString().equalsIgnoreCase("EAST") ) {
	        		   loc.add(0, 0, -1);
	        		   return loc;	        	
	        	   }
	        	   else if ( chest.getFacing().toString().equalsIgnoreCase("NORTH") ) {
	        		   loc.add(-1, 0, 0);
	        		   return loc;
	        	   }
	        	   else if ( chest.getFacing().toString() == "SOUTH" )  {
	        		   loc.add(1, 0, 0);
	        		   return loc;
	        	   }
	           }
	        }
	    	return loc;
	    }	    
	    
	    public Boolean isKey(ItemStack item) {
	    	String mat = item.getType().toString();       	
	    	if (mat == "AIR") return false;
	    	ItemMeta meta = item.getItemMeta();   	
	    	if (! (meta.hasCustomModelData()) ) return false;	    	
	        Integer keyCusModDat = meta.getCustomModelData();
	        for (String key : Main.inst.getConfig().getConfigurationSection("keys").getKeys(false)) {
	        	if ( !(Main.inst.getConfig().get("keys." + key + ".material").toString().equals(mat)) ) continue;
	        	if (Main.inst.getConfig().getInt("keys." + key + ".customModelData") == keyCusModDat) {
	        		return true;        		
	        	}
	        }        
	        return false;
	    }    
		
	    public Integer getKeyID() {
	        int keyID = ThreadLocalRandom.current().nextInt(10000, 99999 + 1);
	        while (isKeyUsed(keyID)) getKeyID();
	        return keyID;
	    }

	    public boolean isKeyUsed(Integer keyID) {
	        ConfigurationSection locks = Main.data.getConfig().getConfigurationSection("locks");
	        if (locks == null) return false;
	        if (Main.yamlKeyID.containsValue(keyID)) return true;
	        return false;
	    }

	    public String getKeyType(ItemStack item) {       
	        ItemMeta meta = item.getItemMeta();
	        if (!isKey(item)) return null;
	        Integer keyCusModDat = meta.getCustomModelData();
	        for (String keys : Main.inst.getConfig().getConfigurationSection("keys").getKeys(false)) {
	        	if (Main.inst.getConfig().getInt("keys." + keys + ".customModelData") == keyCusModDat) {
	        		return Main.inst.getConfig().getString("keys." + keys + ".type");        		
	        	}
	        }        
	        return null;
	    }
}