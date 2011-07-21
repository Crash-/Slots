package com.Crash.Slots;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class SlotMachine implements Runnable {
	
	private SlotsPlugin plugin;
	private String[] rolls = new String[3];
	private int currentRoll = 0;
	private Block block;
	
	public SlotMachine(Block b){
		
		plugin = SlotsPlugin.getStatic();
		block = b;
		
	}
	
	public boolean equals(Object o){
		
		if(o instanceof Block){
			
			return ((Block)o).getLocation().equals(block.getLocation());
			
		} else if(o instanceof Location){
			
			return ((Location)o).equals(block.getLocation());
			
		}
		
		return false;
		
	}
	
	private String generateRoll(){
		
		double chance = 0, random = Math.random();
		
		for(SlotRoll roll : plugin.getRolls()){
			
			if(random >= chance && random < chance + roll.getChance())
				return roll.getName();
			
			chance += roll.getChance();
			
		}
		
		return null;
		
	}

	@Override
	public void run() {
		
		if(currentRoll ==)
		
	}

}
