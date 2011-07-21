package com.Crash.Slots;

public class SlotSettings {

	private int speed;
	private boolean multiplyWinnings, subtractOvercost;
	
	public boolean multiplyWinnings(){ return multiplyWinnings; }
	
	public void setMultiplyWinnings(boolean val){ multiplyWinnings = val; }
	
	public boolean subtractOvercost(){ return subtractOvercost; }
	
	public void setSubtractOvercost(boolean val){ subtractOvercost = val; }
	
	public int getSpeed(){ return speed; }
	
	public void setSpeed(int val){ speed = val; }
	
}
