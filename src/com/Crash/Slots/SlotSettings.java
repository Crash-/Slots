package com.Crash.Slots;

import java.io.File;

import org.bukkit.util.config.Configuration;

public class SlotSettings {

	private long speed;
	private double partialRollWinMultiplier;
	private boolean multiplyWinnings, subtractOvercost, saveOnDisable, saveOnSlotChange, partialRollWin, linkedToAccounts;
	
	public boolean multiplyWinnings(){ return multiplyWinnings; }
	
	public void setMultiplyWinnings(boolean val){ multiplyWinnings = val; }
	
	public boolean subtractOvercost(){ return subtractOvercost; }
	
	public void setSubtractOvercost(boolean val){ subtractOvercost = val; }
	
	public boolean saveOnDisable(){ return saveOnDisable; }
	
	public void setSaveOnDisable(boolean val){ saveOnDisable = val; }
	
	public long getSpeed(){ return speed; }
	
	public void setSpeed(long val){ speed = val; }
	
	public boolean saveOnSlotChange(){ return saveOnSlotChange; }
	
	public void setSaveOnSlotChange(boolean val){ saveOnSlotChange = val; }
	
	public boolean partialRollWin(){ return partialRollWin; }
	
	public void setPartialRollWin(boolean val){ partialRollWin = val; }
	
	public double getPartialRollWinMultiplier(){ return partialRollWinMultiplier; }
	
	public void setPartialRollWinMultiplier(double val){ partialRollWinMultiplier = val; }
	
	public boolean linkedToAccounts(){ return linkedToAccounts; }
	
	public void setLinkedToAccounts(boolean val){ linkedToAccounts = val; }
	
	public void loadSlotSettings(File file){
		
		Configuration config = new Configuration(file);
		
		config.load();
		
		speed = config.getInt("roll-speed", 20);
		multiplyWinnings = config.getBoolean("multiply-winnings", true);
		subtractOvercost = config.getBoolean("subtract-overcost", true);
		saveOnDisable = config.getBoolean("save-on-disable", true);
		saveOnSlotChange = config.getBoolean("save-on-slot-change", false);
		partialRollWin = config.getBoolean("partial-win.active", false);
		partialRollWinMultiplier = config.getDouble("partial-win.multiplier", 1.0);
		linkedToAccounts = config.getBoolean("linked-to-accounts", false);
		
	}
	
	public void saveSlotSettings(File file){
		
		Configuration config = new Configuration(file);
		
		config.setProperty("roll-speed", speed);
		config.setProperty("multiply-winnings", multiplyWinnings);
		config.setProperty("subtract-overcost", subtractOvercost);
		config.setProperty("save-on-disable", saveOnDisable);
		config.setProperty("save-on-slot-change", saveOnSlotChange);
		config.setProperty("partial-win.active", partialRollWin);
		config.setProperty("partial-win.multiplier", partialRollWinMultiplier);
		config.setProperty("linked-to-accounts", linkedToAccounts);
		
		config.save();
		
	}
	
}
