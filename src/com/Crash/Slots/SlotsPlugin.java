package com.Crash.Slots;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.Crash.Slots.SlotsPlugin.PlayerState;
import com.iConomy.iConomy;
import com.nijiko.permissions.PermissionHandler;

public class SlotsPlugin extends JavaPlugin {

	private static Logger log = Logger.getLogger("Minecraft");
	private static SlotsPlugin staticPlugin = null;
	private HashMap<String, PlayerState> playerState = new HashMap<String, PlayerState>();
	private BListener BlockListener = null;
	private PListener PlayerListener = null;
	private SlotDataHandler DataHandler = null;
	private SlotSettings Settings = null;
	private SlotsEconomyHandler EconomyHandler = null;
	private PermissionHandler Permissions = null;
	private File slotSaveFile = null, slotConfigFile = null, rollSaveFile = null;
	
	public static void outConsole(String s){
		
		log.log(Level.INFO, "[Slots] " + s);
		
	}
	
	public static void outConsole(Level l, String s){
		
		log.log(l, "[Slots] " + s);
		
	}
	
	@Override
	public void onDisable() {
		
		if(Settings.saveOnDisable())
			saveAllData();
		
		outConsole("Slots disabled.");
		
	}
	
	@Override
	public void onEnable() {
		
		staticPlugin = this;
		
		DataHandler = new SlotDataHandler();
		
		Settings = new SlotSettings();
		
		EconomyHandler = new SlotsEconomyHandler();
		
		BlockListener = new BListener();
		
		PlayerListener = new PListener();
		
		PluginManager pm = getServer().getPluginManager();
		
		Plugin PermissionsPlugin = pm.getPlugin("Permissions");
		
		slotSaveFile = new File("plugins/Slots/slots.yml");
		rollSaveFile = new File("plugins/Slots/rolls.yml");
		slotConfigFile = new File("plugins/Slots/config.yml");
		
		if(getDataFolder().mkdir())
			outConsole("Created Slots directory.");
		
		if(!slotSaveFile.exists())
			try { slotSaveFile.createNewFile(); } catch(Exception e){ outConsole("Error creating slot save file."); }
			
		if(!rollSaveFile.exists())
			try { rollSaveFile.createNewFile(); } catch(Exception e){ outConsole("Error creating roll save file."); }
				
		if(!slotConfigFile.exists()){
			
			try { slotConfigFile.createNewFile(); } catch(Exception e){ outConsole("Error creating slot config file."); }
			
			if(slotConfigFile.exists()){
				
				Settings.loadSlotSettings(slotConfigFile);
				Settings.saveSlotSettings(slotConfigFile);
				
			}
			
		}
			
		if(PermissionsPlugin == null)
			outConsole("Unable to find Permissions, using OP only.");
		else 
			Permissions = ((com.nijikokun.bukkit.Permissions.Permissions)PermissionsPlugin).getHandler();
		
		Plugin iConomyPlugin = pm.getPlugin("iConomy");
		
		if(iConomyPlugin == null){
			
			outConsole("Unable to find iConomy, disabling.");
			pm.disablePlugin(this);
			return;
			
		} else if(PermissionsPlugin != null)
			outConsole("Hooked iConomy and Permissions successfully.");
		
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, BlockListener, Event.Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, PlayerListener, Event.Priority.Normal, this);
		
		loadAllData();
		
		if(DataHandler.getRollList().size() == 0){
			
			log.log(Level.WARNING, "[Slots] The roll list is blank, you must add rolls in before starting!");
			pm.disablePlugin(this);
			return;
			
		}
		
		outConsole("Slots v" + getDescription().getVersion() + " enabled, by Crash");
		
	}
	
	public void loadAllData(){
		
		DataHandler.loadSlotData(slotSaveFile);
		DataHandler.loadRollData(rollSaveFile);
		Settings.loadSlotSettings(slotConfigFile);
		
		outConsole("All data loaded.");
		
	}
	
	public void saveAllData(){
		
		DataHandler.saveSlotData(slotSaveFile);
		DataHandler.saveRollData(rollSaveFile);
		Settings.saveSlotSettings(slotConfigFile);
		
		outConsole("All data saved.");
		
	}
	
	public File getSlotDataFile(){ return slotSaveFile; }
	
	public File getRollDataFile(){ return rollSaveFile; }
	
	public File getSlotConfigFile(){ return slotConfigFile; }
	
	public SlotDataHandler getDataHandler(){ return DataHandler; } 
	
	public SlotSettings getSettings(){ return Settings; }
	
	public SlotsEconomyHandler getEconomyHandler(){ return EconomyHandler; }
	
	public static SlotsPlugin getStatic(){ return staticPlugin; }
	
	public boolean usingPermissions(){ return Permissions != null; }
	
	public boolean has(Player player, String perm){
		
		return usingPermissions() ? Permissions.has(player, perm) : player.isOp();
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		if(!(sender instanceof Player))
			return false;
		
		System.out.println(sender.toString());
		
		Player player = (Player)sender;
		
		if(args.length == 0)
			return false;
		
		for(int i = 0; i < args.length; i++)
			args[i] = args[i].toLowerCase();
		
		if(args[0].equals("create")){
			
			if(args.length >= 2 && args[1].equals("server"))
				playerState.put(player.getName(), PlayerState.CREATE_SERVER);
			else 
				playerState.put(player.getName(), PlayerState.CREATE);
			
			player.sendMessage(ChatColor.GOLD + "Left click the sign to create the slot machine.");
			
		} else if(args[0].equals("delete")){
			
			playerState.put(player.getName(), PlayerState.DELETE);
			
			player.sendMessage(ChatColor.GOLD + "Left click to delete the slot machine.");
			
		} else if(args[0].equals("info")){
			
			playerState.put(player.getName(), PlayerState.INFO);
			
			player.sendMessage(ChatColor.GOLD + "Left click a sign to get the machine's info.");
			
		} else if(args[0].equals("deposit")){
			
			if(args.length == 1)
				return false;
			
			PlayerState state = PlayerState.DEPOSIT;
			
			double amount = 0;
			
			try {
				
				amount = Double.parseDouble(args[1]);
				
			} catch(Exception e){
				
				player.sendMessage(ChatColor.RED + "Error getting deposit amount.");
				return false;
				
			}
			
			state.setVal(amount);
			
			playerState.put(player.getName(), state);
			
			player.sendMessage(ChatColor.GREEN + "Left click a slot machine to deposit money into it.");
			
		} else if(args[0].equals("withdraw")){
			
			if(args.length == 1)
				return false;
			
			PlayerState state = PlayerState.WITHDRAW;
			
			double amount = 0;
			
			try {
				
				amount = Double.parseDouble(args[1]);
				
			} catch(Exception e){
				
				player.sendMessage(ChatColor.RED + "Error getting withdraw amount.");
				return false;
				
			}
			
			state.setVal(amount);
			
			playerState.put(player.getName(), state);
			
			player.sendMessage(ChatColor.GREEN + "Left click a slot machine to withdraw money from it.");
			
		} else if(args[0].equals("save")){
			
			if(!has(player, "slots.save")){
				
				player.sendMessage(ChatColor.RED + "You aren't allowed to save data!");
				return false;
				
			}
			
			saveAllData();
			player.sendMessage(ChatColor.GREEN + "Saved data successfully.");
			
		} else if(args[0].equals("reload")){
			
			if(!has(player, "slots.reload")){
				
				player.sendMessage(ChatColor.RED + "You aren't allowed to reload data!");
				return false;
				
			}
			
			loadAllData();
			player.sendMessage(ChatColor.GREEN + "Loaded data successfully.");
			
		} else if(args[0].equals("help") || args[0].equals("?") || args[0].equals("h")){
			
			player.sendMessage(ChatColor.GOLD + "======= Slots v" + getDescription().getVersion() + " =======");
			player.sendMessage(ChatColor.GOLD + "/slots create (server)");
			player.sendMessage(ChatColor.GOLD + "/slots delete");
			player.sendMessage(ChatColor.GOLD + "/slots info");
			player.sendMessage(ChatColor.GOLD + "/slots deposit <amount>");
			player.sendMessage(ChatColor.GOLD + "/slots withdraw <amount>");
			player.sendMessage(ChatColor.GOLD + "/slots save");
			player.sendMessage(ChatColor.GOLD + "/slots load");
			
		}
		
		return true;
		
	}
	
	public PlayerState getState(Player player){
		
		PlayerState state = playerState.remove(player.getName());
		
		if(state == null)
			return PlayerState.NONE;
		
		return state;
		
	}
	
	public void removeState(Player player){
		
		playerState.remove(player.getName());
		
	}
	
	public enum PlayerState {
		
		NONE,
		CREATE,
		CREATE_SERVER,
		DELETE,
		INFO,
		DEPOSIT,
		WITHDRAW;
		
		double val;
		
		public void setVal(double arg){
			
			val = arg;
			
		}
		
		public double getVal(){ return val; }
		
	}

}

class PListener extends PlayerListener {
	
	private SlotsPlugin plugin;
	
	public PListener(){
		
		plugin = SlotsPlugin.getStatic();
		
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			
			Block block = event.getClickedBlock();
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST){
				
				SlotMachine m = plugin.getDataHandler().getMachine(block);
				
				if(m == null)
					return;
				
				m.rollSlots(event.getPlayer());
				
			}
			
		} else if(event.getAction() == Action.LEFT_CLICK_BLOCK){
			
			Block block = event.getClickedBlock();
			
			PlayerState state = plugin.getState(event.getPlayer());
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST){
				
				if(state == PlayerState.NONE)
					return;
				
				SlotMachine m = plugin.getDataHandler().getMachine(block);
				
				if(m == null && (state != PlayerState.CREATE && state != PlayerState.CREATE_SERVER))
					return;
				
				if(state == PlayerState.CREATE || state == PlayerState.CREATE_SERVER){
					
					Sign sign = (Sign)block.getState();
					
					if(!sign.getLine(0).equalsIgnoreCase("[Slots]"))
						return;
					
					if(state == PlayerState.CREATE && !plugin.has(event.getPlayer(), "slot.create")){
						
						event.getPlayer().sendMessage(ChatColor.RED + "You aren't allowed to make slot machines!");
						event.setCancelled(true);
						return;
						
					}
					
					if(state == PlayerState.CREATE_SERVER && !plugin.has(event.getPlayer(), "slots.createserver")){
						
						event.getPlayer().sendMessage(ChatColor.RED + "You aren't allowed to make server owned slot machines!");
						event.setCancelled(true);
						return;
						
					}
					
					double pay;
					
					try {
						
						pay = Double.parseDouble(sign.getLine(1));
						
					} catch(NumberFormatException e){
						
						event.getPlayer().sendMessage(ChatColor.RED + "You must put in a cost amount on the second line.");
						return;
						
					}
					
					if(state == PlayerState.CREATE)
						plugin.getDataHandler().addSlotMachine(event.getPlayer().getName(), pay, 0, block);
					else
						plugin.getDataHandler().addSlotMachine(null, pay, 0, block);
					
					if(plugin.getSettings().saveOnSlotChange())
						plugin.getDataHandler().saveSlotData(plugin.getSlotDataFile());
					
					event.getPlayer().sendMessage(ChatColor.GREEN + "The slot machine was successfully created!");
					
					sign.setLine(0, ChatColor.YELLOW + "[Slots]");
					sign.update();
					
				} else if(state == PlayerState.DELETE){
					
					if(!plugin.has(event.getPlayer(), "slots.delete")){
						
						event.getPlayer().sendMessage(ChatColor.RED + "You aren't allowed to delete slot machines!");
						return;
						
					}
					
					if(!m.isOwner(event.getPlayer())){
						
						event.getPlayer().sendMessage(ChatColor.RED + "You don't own this slot machine!");
						return;
						
					}
					
					if(plugin.getDataHandler().removeSlotMachine(m))
						event.getPlayer().sendMessage(ChatColor.GREEN + "You removed the slot machine successfully.");
					
					block.setTypeId(0);
					block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), new ItemStack(323, 1));
					
					if(plugin.getSettings().saveOnSlotChange())
						plugin.getDataHandler().saveSlotData(plugin.getSlotDataFile());
					
				} else if(state == PlayerState.INFO){
					
					Player p = event.getPlayer();
					
					if(!plugin.has(p, "slots.withdraw")){
						
						p.sendMessage(ChatColor.RED + "You aren't allowed to get info on slot machines!");
						return;
						
					}
					
					if(m.isOwned()){
					
						p.sendMessage(ChatColor.GOLD + "Owner : " + m.getOwner());
						p.sendMessage(ChatColor.GOLD + "Balance : " + m.getAmount());
						
					} else {
						
						p.sendMessage(ChatColor.GOLD + "Owner : Server");
						p.sendMessage(ChatColor.GOLD + "Balance : <infinite>");
						
					}
					
					p.sendMessage(ChatColor.GOLD + "Cost : " + m.getCost());
					p.sendMessage(ChatColor.GOLD + "Uses : " + m.getUses());
					
				} else if(state == PlayerState.DEPOSIT){
					
					double amount = state.getVal();
					
					if(!plugin.has(event.getPlayer(), "slots.deposit")){
						
						event.getPlayer().sendMessage(ChatColor.RED + "You aren't allowed to deposit!");
						return;
						
					}
					
					if(!m.isOwned()){
						
						event.getPlayer().sendMessage(ChatColor.RED + "This is a server owned slot machine!");
						return;
						
					}
						
					if(!m.isOwner(event.getPlayer())){
						
						event.getPlayer().sendMessage(ChatColor.RED + "This isn't your slot machine.");
						return;
						
					}
					
					if(!plugin.getEconomyHandler().hasEnough(event.getPlayer(), amount)){
						
						event.getPlayer().sendMessage(ChatColor.RED + "You don't have enough money!");
						return;
						
					}
					
					plugin.getEconomyHandler().subtractAmount(event.getPlayer(), amount);
					
					m.addAmount(amount);
					
					event.getPlayer().sendMessage(ChatColor.GREEN + iConomy.format(amount) + " has been deposited into your slot machine.");
					
				} else if(state == PlayerState.WITHDRAW){
					
					double amount = state.getVal();
					
					if(!plugin.has(event.getPlayer(), "slots.withdraw")){
						
						event.getPlayer().sendMessage(ChatColor.RED + "You aren't allowed to withdraw!");
						return;
						
					}
					
					if(!m.isOwned()){
						
						event.getPlayer().sendMessage(ChatColor.RED + "This is a server owned slot machine!");
						return;
						
					}
						
					if(!m.isOwner(event.getPlayer())){
						
						event.getPlayer().sendMessage(ChatColor.RED + "This isn't your slot machine.");
						return;
						
					}
					
					if(m.getAmount() < amount){
						
						event.getPlayer().sendMessage(ChatColor.RED + "This slot machine doesn't have enough money in it!");
						return;
						
					}
					
					plugin.getEconomyHandler().addAmount(event.getPlayer(), amount);
					
					m.subtractAmount(amount);
					
					event.getPlayer().sendMessage(ChatColor.GREEN + "You withdrew " + iConomy.format(amount) + " from the machine.");
					
				}
				
			}
			
		}
		
	}
	
}

class BListener extends BlockListener {
	
	private SlotsPlugin plugin;
	
	public BListener(){
		
		plugin = SlotsPlugin.getStatic();
		
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event){
		
		Block block = event.getBlock();
		
		if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST){
			
			SlotMachine machine = plugin.getDataHandler().getMachine(block);
			
			if(machine == null)
				return;
			
			if(machine.isOwner(event.getPlayer()))
				event.getPlayer().sendMessage(ChatColor.GOLD + "To remove a machine use /slots delete and click the machine.");

			((Sign)event.getBlock().getState()).update();
			
			event.setCancelled(true);
			
		}
		
	}
	
}