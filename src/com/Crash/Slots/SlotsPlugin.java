package com.Crash.Slots;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;

public class SlotsPlugin extends JavaPlugin {

	private static Logger log = Logger.getLogger("Minecraft");
	private static SlotsPlugin staticPlugin = null;
	private SlotDataHandler DataHandler = null;
	private SlotSettings Settings = null;
	private SlotsEconomyHandler EconomyHandler = null;
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
		
		DataHandler = new SlotDataHandler();
		
		Settings = new SlotSettings();
		
		EconomyHandler = new SlotsEconomyHandler();
		
		PluginManager pm = getServer().getPluginManager();
		
		Plugin PermissionsPlugin = pm.getPlugin("Permissions");
		
		if(PermissionsPlugin == null)
			outConsole("Unable to find Permissions, using OP only.");
		else {
			
			Permissions = ((com.nijikokun.bukkit.Permissions.Permissions)PermissionsPlugin).getHandler();
			outConsole("Found and hooked Permissions plugin.");
			
		}
		
		outConsole("Slots v" + getDescription().getVersion() + " enabled, by Crash");
		
	}
	
	public SlotDataHandler getDataHandler(){ return DataHandler; } 
	
	public SlotSettings getSettings(){ return Settings; }
	
	public SlotsEconomyHandler getEconomyHandler(){ return EconomyHandler; }
	
	public static SlotsPlugin getStatic(){ return staticPlugin; }
	
	public boolean usingPermissions(){ return Permissions != null; }
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		return true;
		
	}

}