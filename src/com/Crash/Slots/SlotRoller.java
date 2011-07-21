package com.Crash.Slots;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SlotRoller implements Runnable {

	private String[] rolls = new String[3];
	private int currentRoll = 0, id;
	private Player roller;
	private SlotMachine myMachine;
	
	public SlotRoller(Player Roller, SlotMachine machine){
		
		roller = Roller;
		myMachine = machine;
		
	}
	
	private String generateRoll(){
		
		double chance = 0, random = Math.random();
		
		for(SlotRoll roll : SlotsPlugin.getStatic().getDataHandler().getRollList()){
			
			if(random >= chance && random < chance + roll.getChance())
				return roll.getName();
			
			chance += roll.getChance();
			
		}
		
		return null;
		
	}
	
	public void setID(int val){ id = val; }
	
	@Override
	public void run() {

		if(currentRoll == 3){
			
			if(rolls[0].equals(rolls[1]) && rolls[1].equals(rolls[2])){
				
				SlotRoll roll = SlotsPlugin.getStatic().getDataHandler().getRoll(rolls[0]);
				
				if(roll == null){
					
					roller.sendMessage(ChatColor.RED + "Error finding your winnings.");
					stopTask();
					return;
					
				}
				
				double pay = roll.getPay();
				
				if(SlotsPlugin.getStatic().getSettings().multiplyWinnings())
					pay *= myMachine.getCost();
				
				roller.sendMessage(ChatColor.GREEN + "Congratulations you won!");
				
				SlotsEconomyHandler eco = SlotsPlugin.getStatic().getEconomyHandler();
				
				if(pay > myMachine.getAmount()){
					
					if(!SlotsPlugin.getStatic().getSettings().subtractOvercost()){
						
						roller.sendMessage("The pay went over the machine's balance, so you only won " + myMachine.getAmount());
						pay = myMachine.getAmount();
						
					} else {
						
						if(eco.hasEnough(myMachine.getOwner(), pay - myMachine.getAmount())){
							
							eco.subtractAmount(myMachine.getOwner(), pay - myMachine.getAmount());
							roller.sendMessage(ChatColor.GREEN + "The pay over the machine's balance, the owner paid " + (pay - myMachine.getAmount()) + " directly out of their account.");
							
						} else {
							
							pay = myMachine.getAmount();
							pay += eco.getBalance(myMachine.getOwner());
							
							eco.setAmount(roller, 0);
							
							roller.sendMessage(ChatColor.GREEN + "The pay went over the machine's balance and the owner's account balance, you only won " + pay);
							
						}
						
					}
					
					myMachine.setAmount(0);
					
				} else {
					
					myMachine.subtractAmount(pay);
					
				}
				
				eco.addAmount(roller, pay);
				
			}
			
		} else {
			
			String roll = generateRoll();
			
			rolls[currentRoll] = roll;
			
			roller.sendMessage(ChatColor.GOLD + "You rolled a " + roll + ".");
			
		}
		
		if(currentRoll >= 4){
		
			stopTask();
			return;
			
		}
		
		currentRoll++;
		
	}
	
	public void stopTask(){
		
		SlotsPlugin.getStatic().getServer().getScheduler().cancelTask(id);
		
	}

}
