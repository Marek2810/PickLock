package me.Marek2810.PickLock.Listener;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import me.Marek2810.PickLock.Utils.LockUtils;

public class RedstoneListener implements Listener {
	
	@EventHandler
    public void onDoorRedstoneChange(BlockRedstoneEvent event) {
		if (event.getBlock() == null) return;
        Block block = event.getBlock();
        if (block.getBlockData() instanceof Door || block.getBlockData() instanceof TrapDoor ) {         	
        	if ( !(LockUtils.isLockable(block.getType().toString())) ) return;
        	if ( !(LockUtils.isLocked(block.getLocation()))) return;
        	event.setNewCurrent(event.getOldCurrent());
        }
    }

}
