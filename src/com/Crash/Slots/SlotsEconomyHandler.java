package com.Crash.Slots;

import org.bukkit.entity.Player;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;

public class SlotsEconomyHandler {

	private Method method;
	
	public void setMethod(Method m){ method = m; }
	
	public boolean accountExists(String player){
		
		return method.hasAccount(player);
		
	}
	
	public boolean accountExists(Player player){
		
		return accountExists(player.getName());
		
	}
	
	public boolean hasEnough(String player, double amount){
		
		if(!accountExists(player))
			return false;
		
		MethodAccount acc = method.getAccount(player);
		
		return acc.hasEnough(amount);
		
	}
	
	public boolean hasEnough(Player player, double amount){
		
		return hasEnough(player.getName(), amount);
		
	}
	
	public double getBalance(String player){
		
		if(!accountExists(player))
			return -1;
		
		MethodAccount acc = method.getAccount(player);
		
		return acc.balance();
		
	}
	
	public double getBalance(Player player){
		
		return getBalance(player.getName());
		
	}
	
	public void addAmount(String player, double amount){
		
		if(!accountExists(player))
			return;
		
		MethodAccount acc = method.getAccount(player);
		
		acc.add(amount);
		
	}
	
	public void addAmount(Player player, double amount){
		
		addAmount(player.getName(), amount);
		
	}
	
	public void subtractAmount(String player, double amount){
		
		if(!accountExists(player))
			return;
		
		MethodAccount acc = method.getAccount(player);
		
		acc.subtract(amount);
		
	}
	
	public void subtractAmount(Player player, double amount){
		
		subtractAmount(player.getName(), amount);
		
	}
	
	public void setAmount(String player, double amount){
		
		if(!accountExists(player))
			return;
		
		MethodAccount acc = method.getAccount(player);
		
		acc.set(amount);
		
	}
	
	public void setAmount(Player player, double amount){
		
		setAmount(player.getName(), amount);
		
	}
	
	public String format(double amt){
		
		return method.format(amt);
		
	}
	
}
