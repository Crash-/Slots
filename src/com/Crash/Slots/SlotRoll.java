package com.Crash.Slots;

import org.bukkit.ChatColor;

public class SlotRoll {
	
	private String name, symbol;
	private double pay;
	private int chance, color;
	
	public SlotRoll(String Name, String Symbol, double Pay, int Chance, int Color){
		
		name = Name;
		symbol = Symbol;
		pay = Pay;
		chance = Chance;
		color = Color;
		
	}
	
	public String getName(){ return name; }
	
	public String getSymbol(){ return symbol; }
	
	public double getPay(){ return pay; }
	
	public double getChance(){ return chance / (double)100; }
	
	public int getChancePercent(){ return chance; }
	
	public int getColorCode(){ return color; }
	
	public ChatColor getColor(){ return ChatColor.values()[color]; }
	
	public boolean equals(Object o){
		
		if(o instanceof String){
			
			return ((String) o).equalsIgnoreCase(name);
			
		} else if(o instanceof SlotRoll){
			
			return ((SlotRoll) o).getName().equalsIgnoreCase(name);
			
		}
		
		return false;
		
	}
	
}
