package me.Marek2810.PickLock.Utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;

public class DoorsUtils {
	
	public static Block getSecondDoor(Block block) {
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
 
 	public static Boolean isDoubleDoor(Block block) {
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
 	
 	public static Location getSecondDoorHlaf(Block block) {
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
}
