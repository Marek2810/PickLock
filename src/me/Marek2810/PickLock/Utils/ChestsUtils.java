package me.Marek2810.PickLock.Utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Chest;

public class ChestsUtils {
	
	public static Boolean isDoubleChest(Block block) {
    	if (block.getBlockData() instanceof Chest chest) {
           if ( !(chest.getType().toString() == "SINGLE") ) return true;
        }			
    	return false;
    }
    
    public static Location getSecondChest(Block block) {
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
}
