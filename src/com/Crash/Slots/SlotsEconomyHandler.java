package com.Crash.Slots;

import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.iConomy.system.Account;

public class SlotsEconomyHandler {

	public boolean accountExists(String player){
		
		return iConomy.hasAccount(player);
		
	}
	
	public boolean accountExists(Player player){
		
		return accountExists(player.getName());
		
	}
	
	public boolean hasEnough(String player, double amount){
		
		if(!accountExists(player))
			return false;
		
		Account acc = iConomy.getAccount(player);
		
		return acc.getHoldings().hasEnough(amount);
		
	}
	
	public boolean hasEnough(Player player, double amount){
		
		return hasEnough(player.getName(), amount);
		
	}
	
	public double getBalance(String player){
		
		if(!accountExists(player))
			return -1;
		
		Account acc = iConomy.getAccount(player);
		
		return acc.getHoldings().balance();
		
	}
	
	public double getBalance(Player player){
		
		return getBalance(player.getName());
		
	}
	
	public void addAmount(String player, double amount){
		
		if(!accountExists(player))
			return;
		
		Account acc = iConomy.getAccount(player);
		
		acc.getHoldings().add(amount);
		
	}
	
	public void addAmount(Player player, double amount){
		
		addAmount(player.getName(), amount);
		
	}
	
	public void subtractAmount(String player, double amount){
		
		if(!accountExists(player))
			return;
		
		Account acc = iConomy.getAccount(player);
		
		acc.getHoldings().subtract(amount);
		
	}
	
	public void subtractAmount(Player player, double amount){
		
		subtractAmount(player.getName(), amount);
		
	}
	
	public void setAmount(String player, double amount){
		
		if(!accountExists(player))
			return;
		
		Account acc = iConomy.getAccount(player);
		
		acc.getHoldings().set(0);
		
	}
	
	public void setAmount(Player player, double amount){
		
		setAmount(player.getName(), amount);
		
	}
	
}
