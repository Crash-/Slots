package com.Crash.Slots;

import java.util.ArrayList;

import org.bukkit.block.Block;

public class SlotDataHandler {

	private ArrayList<SlotMachine> machineList = new ArrayList<SlotMachine>();
	private ArrayList<SlotRoll> rollList = new ArrayList<SlotRoll>();
	private SlotsPlugin plugin;
	
	public SlotDataHandler(){
		
		plugin = SlotsPlugin.getStatic();
		
		addSlotRoll("nigga", "n", 5, 1, 2, 3);
		
		addSlotRoll("NIGGUH", "N", 10, 1, 2, 2);
		
	}
	
	public ArrayList<SlotRoll> getRollList(){ return rollList; }
	
	public SlotRoll getRoll(String name){
		
		for(SlotRoll roll : rollList)	
			if(roll.equals(name))
				return roll;
		
		return null;
		
	}
	
	public ArrayList<SlotMachine> getMachineList(){ return machineList; }
	
	public SlotMachine getMachine(Block block){
		
		for(SlotMachine machine : machineList)
			if(machine.equals(block))
				return machine;
			
		return null;
		
	}
	
	public void addSlotMachine(String owner, double cost, int uses, Block block){
		
		machineList.add(new SlotMachine(owner, 0, cost, uses, block));
		
	}
	
	public boolean removeSlotMachine(Block block){
		
		for(SlotMachine machine : machineList){
			
			if(machine.equals(block)){
				
				machineList.remove(machine);
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public boolean removeSlotMachine(SlotMachine machine){
		
		return machineList.remove(machine);
		
	}
	
	private void addSlotRoll(String name, String symbol, double pay, int chancenum, int chanceden, int color){
		
		rollList.add(new SlotRoll(name, symbol, pay, chancenum, chanceden, color));
		
	}
	
}
