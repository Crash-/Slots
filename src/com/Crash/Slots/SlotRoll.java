package com.Crash.Slots;

import org.bukkit.ChatColor;

public class SlotRoll {
	
	private String name, symbol;
	private double pay;
	private int chancenum, chanceden, color;
	
	public SlotRoll(String Name, String Symbol, double Pay, int Chancenum, int Chanceden, int Color){
		
		name = Name;
		symbol = Symbol;
		pay = Pay;
		chancenum = Chancenum;
		chanceden = Chanceden;
		color = Color;
		
	}
	
	public String getName(){ return name; }
	
	public String getSymbol(){ return symbol; }
	
	public double getPay(){ return pay; }
	
	public double getChance(){ return (double)chancenum / chanceden; }

	public int getNumerator(){ return chancenum; }
	
	public int getDenominator(){ return chanceden; }
	
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
