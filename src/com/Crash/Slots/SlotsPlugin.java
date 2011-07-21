package com.Crash.Slots;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;

public class SlotsPlugin extends JavaPlugin {

	private static Logger log = Logger.getLogger("Minecraft");
	private static SlotsPlugin staticPlugin = null;
	private ArrayList<SlotRoll> rollList = new ArrayList<SlotRoll>();
	private PermissionHandler Permissions = null;
	
	public static void outConsole(String s){
		
		log.log(Level.INFO, "[Slots] " + s);
		
	}
	
	@Override
	public void onDisable() {
		
		
		
	}
	
	@Override
	public void onEnable() {
		
		staticPlugin = this;
		
		PluginManager pm = getServer().getPluginManager();
		
		Plugin PermissionsPlugin = pm.getPlugin("Permissions");
		
		if(PermissionsPlugin == null)
			outConsole("Unable to find Permissions, using OP only.");
		else {
			
			Permissions = ((com.nijikokun.bukkit.Permissions.Permissions)PermissionsPlugin).getHandler();
			outConsole("Found and hooked Permissions plugin.");
			
		}
		
		outConsole("Slots v" + getServer().getVersion() + " enabled, by Crash");
		
	}
	
	public ArrayList<SlotRoll> getRolls(){ return rollList; }
	
	public static SlotsPlugin getStatic(){ return staticPlugin; }
	
	public boolean usingPermissions(){ return Permissions != null; }
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		return true;
		
	}

}
