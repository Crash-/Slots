package com.Crash.Slots;

import java.util.ArrayList;

public class SlotDataHandler {

	private ArrayList<SlotRoll> rollList = new ArrayList<SlotRoll>();
	private SlotsPlugin plugin;
	
	public SlotDataHandler(){
		
		plugin = SlotsPlugin.getStatic();
		
	}
	
	public ArrayList<SlotRoll> getRollList(){ return rollList; }
	
	public SlotRoll getRoll(String name){
		
		for(SlotRoll roll : rollList){
			
			if(roll.equals(name))
				return roll;
			
		}
		
		return null;
		
	}
	
}
