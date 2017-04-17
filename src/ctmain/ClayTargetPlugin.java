package ctmain;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ClayTargetPlugin extends JavaPlugin {
	public static boolean toggle = true;
	public static List<Location> Locations = new ArrayList<Location>();
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new EventListener(this), this);
if (getConfig() == null) {
	this.saveDefaultConfig();
}
	if (getConfig().getList("Location") != null ) {
	Locations = (List<Location>) getConfig().getList("Location");	
	getLogger().info("Plugin has been Sucessfully Enabled");
	}else {
	getLogger().info("Plugin has been Sucessfully Enabled For the First Time");
	getConfig().options().copyDefaults(true);
	}
	}
	
	@Override
	public void onDisable() {
		getConfig().set("Location", Locations);
		saveConfig();
		getLogger().info("Plugin has been Sucessfully Disabled");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ctpregister") && sender instanceof Player){
			Player player = (Player) sender;
			Block block = player.getTargetBlock((Set<Material>) null, 100);
			if ( player.hasPermission("ctpperms")) {
			if (block.getType().equals(Material.DISPENSER )) {
				if(Locations.contains(block.getLocation())) {
					sender.sendMessage("Already Registered");
					return true;
				}
	Locations.add(block.getLocation());
	sender.sendMessage("Dispenser Set. Use /ctpunregister to unregister it");
				return true;
			}else {sender.sendMessage("Block Is Not a Dispenser");}
				return true;
			}else {player.sendMessage("You do not have Permission to run this Commands");}
				
		
		}
		else if (cmd.getName().equalsIgnoreCase("ctpunregister") && sender instanceof Player) {
			Player player = (Player) sender;
			Block block = player.getTargetBlock((Set<Material>) null, 100);
			if ( player.hasPermission("ctpperms")) {
				if (Locations.contains(block.getLocation())) {
			if (block.getType().equals(Material.DISPENSER )) {
				Locations.remove(block.getLocation());
				sender.sendMessage("Dispenser Unregistered");
				return true;
				}
			else {sender.sendMessage("Block Is Not a Dispenser");}
		 
				return true;
				}else {sender.sendMessage("This Block Isnt Registered");
				return true;}
			}else {player.sendMessage("You do not have Permission to run this Commands");}

		}
		else if (cmd.getName().equalsIgnoreCase("ctpscore") && sender instanceof Player) {
			if ( EventListener.test.isEmpty()) {
				Bukkit.broadcastMessage("Nobody Has Scored yet");
				return true;	
			}else {
			for ( UUID key : EventListener.test.keySet() ) {
				Player player = Bukkit.getPlayer(key);
				sender.sendMessage(player.getDisplayName() + ": " + EventListener.test.get(key)) ;;
			}
			return true;
			}
		}
			
		else if (cmd.getName().equalsIgnoreCase("ctpscorereset") && sender instanceof Player) {
			Player player = (Player) sender;
			if ( player.hasPermission("ctpperms")) {
		EventListener.test.clear();
		sender.sendMessage("Scores Cleared");
		
		return true;
		
		}else {player.sendMessage("You do not have Permission to run this Commands");}
		
		}else if (cmd.getName().equalsIgnoreCase("ctptoggle") && sender instanceof Player) {
	
			
			if (toggle == true) {
				toggle = false;
				sender.sendMessage("Toggle is now false");
				return true;
			}else if (toggle == false) { 
				toggle = true; 
				sender.sendMessage("Toggle is now True");
				return true;
				}
			
		}
		
		return false;
		}
	
}
		
	

	

