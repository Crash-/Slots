package com.Crash.Slots;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.util.config.Configuration;

public class SlotDataHandler {

	private ArrayList<SlotMachine> machineList = new ArrayList<SlotMachine>();
	private ArrayList<SlotRoll> rollList = new ArrayList<SlotRoll>();
	private SlotsPlugin plugin;
	
	public SlotDataHandler(){
		
		plugin = SlotsPlugin.getStatic();
		
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
	
	private void addSlotRoll(String name, String symbol, double pay, int chance, int color){
		
		rollList.add(new SlotRoll(name, symbol, pay, chance, color));
		
	}
	
	public void saveSlotData(File file){
		
		int slotNum = 0;
		
		Configuration config = new Configuration(file);
		
		for(SlotMachine machine : machineList){
			
			String key = "machines." + slotNum + ".";
			
			Object[] list = new Object[4];
			
			list[0] = machine.getBlock().getWorld().getName();
			list[1] = machine.getBlock().getX();
			list[2] = machine.getBlock().getY();
			list[3] = machine.getBlock().getZ();
			
			config.setProperty(key + "loc", list);
			config.setProperty(key + "owner", machine.getOwner());
			config.setProperty(key + "uses", machine.getUses());
			config.setProperty(key + "amount", machine.getAmount());
			
			slotNum += 1;
			
		}
		
		config.save();
		
	}
	
	public void loadSlotData(File file){
		
		Configuration config = new Configuration(file);
		
		config.load();
		
		List<String> keys = config.getKeys("machines");
		
		if(keys == null)
			return;
		
		for(String key : keys){
			
			try {
			
				double amount, cost = 0;
				int x, y, z, uses;
				String worldname, owner;
				
				List<Object> loc = config.getList("machines." + key + ".loc");
				
				worldname = (String)loc.get(0);
				
				World world = plugin.getServer().getWorld(worldname);
				
				x = (Integer)loc.get(1);
				y = (Integer)loc.get(2);
				z = (Integer)loc.get(3);
				uses = config.getInt("machines." + key + ".uses", 0);
				owner = config.getString("machines." + key + ".owner");
				if(owner != null && owner.isEmpty())
					owner = null;
				amount = config.getDouble("machines." + key + ".amount", 0);
				
				Block block = new Location(world, x, y, z).getBlock();
				
				if(block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN){
					
					System.out.println("[Slots] Error key " + key + " is not a sign!");
					continue;
					
				}
				
				try {
					
					String line = ((Sign)block.getState()).getLine(1).replaceAll("$", "");
					cost = Double.parseDouble(line);
					
				} catch(NumberFormatException e){
					
					System.out.println("[Slots] Error with key " + key + " getting cost.");
					continue;
					
				}
				
				SlotMachine machine = new SlotMachine(owner, amount, cost, uses, block);
				
				machineList.add(machine);
				
			} catch(Exception e){
				
				System.out.println("[Slots] Error when loading slot data at key : " + key + ".");
				
			}
			
		}
		
	}
	
	public void loadRollData(File file){
		
		Configuration config = new Configuration(file);
		
		config.load();
		
		List<String> keys = config.getKeys();
		
		for(String key : keys){
			
			try {
			
				String name = key, symbol;
				int chance, color;
				double pay;
				
				symbol = config.getString(key + ".symbol");
				if(symbol == null){
					
					System.out.println("[Slots] Roll " + key + " is missing a symbol value.");
					continue;
					
				}
				chance = config.getInt(key + ".chance", -1);
				if(chance == -1){
					
					System.out.println("[Slots] Roll " + key + " is missing a chance value.");
					continue;
					
				}
				color = config.getInt(key + ".color", -1);
				if(color == -1){
					
					System.out.println("[Slots] Roll " + key + " is missing a color value.");
					continue;
					
				}
				
				pay = config.getDouble(key + ".pay", -1);
				if(pay == -1){
					
					System.out.println("[Slots Roll " + key + " is missing a pay value.");
					continue;
					
				}
				
				addSlotRoll(name, symbol, pay, chance, color);
				
			} catch(Exception e){
				
				System.out.println("[Slots] Error while loading roll " + key + ".");
				
			}
			
		}
		
	}
	
	public void saveRollData(File file){
		
		Configuration config = new Configuration(file);
		
		for(SlotRoll roll : rollList){
			
			String name = roll.getName();
			
			config.setProperty(name + ".symbol", roll.getSymbol());
			config.setProperty(name + ".chance", roll.getChancePercent());
			config.setProperty(name + ".color", roll.getColorCode());
			config.setProperty(name + ".pay", roll.getPay());
			
		}
		
		config.save();
		
	}
	
}
