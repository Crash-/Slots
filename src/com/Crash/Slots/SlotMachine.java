package com.Crash.Slots;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SlotMachine {

	private String owner;
	private double amount, cost;
	private Block block;
	private int uses;
	private boolean isRolling;

	public SlotMachine(String Owner, double Amount, double Cost, int Uses, Block Block){

		block = Block;
		owner = Owner;
		amount = Amount;
		cost = Cost;
		uses = Uses;

	}

	public int getUses(){ return uses; }

	public double getAmount(){ return amount; }

	public double getCost(){ return cost; }

	public void subtractAmount(double amt){ amount -= amt; }

	public void addAmount(double amt){ amount += amt; }

	public void setAmount(double amt){ amount = amt; }

	public String getOwner(){ return owner; }

	public Block getBlock(){ return block; }

	public boolean isOwner(Player player){

		return SlotsPlugin.getStatic().has(player, "slots.ownall") || (isOwned() ? owner.equalsIgnoreCase(player.getName()) : false);

	}

	public boolean isOwned(){ return owner != null; }

	public void setRolling(boolean val){ isRolling = val; }

	public void rollSlots(Player roller){

		SlotsEconomyHandler eco = SlotsPlugin.getStatic().getEconomyHandler();

		if(isRolling){

			roller.sendMessage(ChatColor.RED + "This machine is already rolling!");
			return;

		}
		
		if(!SlotsPlugin.getStatic().getSettings().linkedToAccounts()){

			if(amount == 0 && isOwned()){

				roller.sendMessage(ChatColor.GOLD + "This slot machine has no money inside!");
				return;

			}

			if(eco.hasEnough(roller, cost)){

				eco.subtractAmount(roller, cost);

			} else {

				roller.sendMessage(ChatColor.RED + "You don't have enough money to use this machine!");
				return;

			}

			if(owner != null)
				addAmount(cost);

		} else {
			
			if(isOwned() && eco.getBalance(owner) == 0){
				
				roller.sendMessage(ChatColor.GOLD + "The owner has no money in their account!");
				return;
				
			}
			
			if(eco.hasEnough(roller, cost)){
				
				eco.subtractAmount(roller, cost);
				
			} else {
				
				roller.sendMessage(ChatColor.RED + "You don't have enough money to use this machine!");
				return;
				
			}
			
			if(isOwned())
				eco.addAmount(owner, cost);
			
		}

		long delay = SlotsPlugin.getStatic().getSettings().getSpeed();

		SlotRoller slotroller = new SlotRoller(roller, this);
		
		isRolling = true;

		slotroller.setID(SlotsPlugin.getStatic().getServer().getScheduler().scheduleSyncRepeatingTask(SlotsPlugin.getStatic(), slotroller, delay, delay));

		slotroller.updateSign();
		
		uses++;

	}

	public boolean equals(Object o){

		if(o instanceof SlotMachine){

			return ((SlotMachine) o).getBlock().getLocation().equals(block.getLocation());

		} else if(o instanceof Block){

			return ((Block)o).getLocation().equals(block.getLocation());

		} else if(o instanceof Location){

			return ((Location)o).equals(block.getLocation());

		}

		return false;

	}

}
