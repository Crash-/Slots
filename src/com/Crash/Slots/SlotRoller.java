package com.Crash.Slots;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;

public class SlotRoller implements Runnable {

	private String[] rolls = new String[3];
	private int currentRoll = 0, id;
	private Player roller;
	private SlotMachine myMachine;
	
	public SlotRoller(Player Roller, SlotMachine machine){
		
		roller = Roller;
		myMachine = machine;
		
	}
	
	private SlotRoll generateRoll(){
		
		double chance = 0, random = Math.random();
		
		for(SlotRoll roll : SlotsPlugin.getStatic().getDataHandler().getRollList()){
			
			if(random >= chance && random < chance + roll.getChance())
				return roll;
			
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
				
				if(myMachine.isOwned()){
				
					if(pay > myMachine.getAmount()){
					
						if(!SlotsPlugin.getStatic().getSettings().subtractOvercost()){
							
							roller.sendMessage("The pay went over the machine's balance, so you only won " + iConomy.format(myMachine.getAmount()));
							pay = myMachine.getAmount();
							
						} else {
							
							if(eco.hasEnough(myMachine.getOwner(), pay - myMachine.getAmount())){
								
								eco.subtractAmount(myMachine.getOwner(), pay - myMachine.getAmount());
								roller.sendMessage(ChatColor.GREEN + "The pay over the machine's balance, the owner paid " + iConomy.format(pay - myMachine.getAmount()) + " directly out of their account.");
								
							} else {
								
								pay = myMachine.getAmount();
								pay += eco.getBalance(myMachine.getOwner());
								
								eco.setAmount(roller, 0);
								
								roller.sendMessage(ChatColor.GREEN + "The pay went over the machine's balance and the owner's account balance, you only won " + iConomy.format(pay));
								
							}
							
						}
						
						if(myMachine.isOwned())
							myMachine.setAmount(0);
						
					} else {
						
						myMachine.subtractAmount(pay);
						
						roller.sendMessage(ChatColor.GREEN + "You won " + iConomy.format(pay) + " from the machine!");
						
					}
					
				} else {
					
					roller.sendMessage(ChatColor.GREEN + "You won " + iConomy.format(pay) + "from the machine!");
					
				}
					
				eco.addAmount(roller, pay);
				
			} else {
				
				roller.sendMessage(ChatColor.GOLD + "Sorry, you didn't win.");
				
			}
			
			stopTask();
			return;
			
		} else {
			
			SlotRoll roll = generateRoll();
			
			if(roll == null){
				
				roller.sendMessage(ChatColor.RED + "There was no roll to match the random value, so the machine stopped. You've been refunded your money.");
				SlotsPlugin.outConsole(Level.SEVERE, "Check that the random chances of all the rolls add up to 1!");
				SlotsEconomyHandler eco = SlotsPlugin.getStatic().getEconomyHandler();
				eco.addAmount(roller, myMachine.getCost());
				myMachine.subtractAmount(myMachine.getCost());
				stopTask();
				return;
				
			}
			
			rolls[currentRoll] = roll.getName();
			
			roller.sendMessage(ChatColor.GOLD + "You rolled a(n) " + roll.getColor() + roll.getName() + ChatColor.GOLD + ".");
			
			updateSign();
			
		}
		
		if(currentRoll >= 4){
		
			stopTask();
			return;
			
		}
		
		currentRoll++;
		
	}
	
	public void updateSign(){
		
		Sign sign = (Sign)myMachine.getBlock().getState();
		
		String line = "";
		
		for(String roll : rolls){
			
			if(roll == null){
				
				line += "  |";
				
			} else {
			
				SlotRoll slotroll = SlotsPlugin.getStatic().getDataHandler().getRoll(roll);
			
				line += slotroll.getColor() + slotroll.getSymbol() + ChatColor.BLACK + "|";
				
			}
			
		}
		
		line = line.substring(0, line.length() - 1);
		
		sign.setLine(2, line);
		sign.update();
		
	}
	
	public void stopTask(){
		
		SlotsPlugin.getStatic().getServer().getScheduler().cancelTask(id);
		
	}

}
