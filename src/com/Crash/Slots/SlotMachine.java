package com.Crash.Slots;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SlotMachine {
	
	private String owner;
	private double amount, cost;
	private Block block;
	
	public SlotMachine(String Owner, double Amount, double Cost, Block Block){
	
		block = Block;
		owner = Owner;
		amount = Amount;
		cost = Cost;
		
	}
	
	public double getAmount(){ return amount; }
	
	public double getCost(){ return cost; }
	
	public void subtractAmount(double amt){ amount -= amt; }
	
	public void addAmount(double amt){ amount += amt; }
	
	public void setAmount(double amt){ amount = amt; }
	
	public String getOwner(){ return owner; }
	
	public void rollSlots(Player roller){
		
		SlotsEconomyHandler eco = SlotsPlugin.getStatic().getEconomyHandler();
		
		if(eco.hasEnough(roller, cost)){
			
			eco.subtractAmount(roller, cost);
			
		} else {
			
			roller.sendMessage(ChatColor.RED + "You don't have enough money to use this machine!");
			return;
			
		}
		
		SlotRoller slotroller = new SlotRoller(roller, this);
		
		addAmount(cost);
		
		int delay = SlotsPlugin.getStatic().getSettings().getSpeed();
		
		slotroller.setID(SlotsPlugin.getStatic().getServer().getScheduler().scheduleSyncRepeatingTask(SlotsPlugin.getStatic(), slotroller, delay, delay));
		
	}
	
	public boolean equals(Object o){
		
		if(o instanceof Block){
			
			return ((Block)o).getLocation().equals(block.getLocation());
			
		} else if(o instanceof Location){
			
			return ((Location)o).equals(block.getLocation());
			
		}
		
		return false;
		
	}

}
