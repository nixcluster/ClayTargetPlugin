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
	public static List<Location> Locations = new ArrayList<Location>();
	@SuppressWarnings("unchecked")
	public void onEnable() {
	Bukkit.getPluginManager().registerEvents(new EventListener(this), this);
	Locations = (List<Location>) getConfig().getList("Location");	
	getLogger().info("Plugin has been Sucessfully Enabled");
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
			if (block.getType().equals(Material.DISPENSER )) {
	Locations.add(block.getLocation());
				Bukkit.broadcastMessage("Dispenser Set. Use /ctpunregister to unregister it");
				System.out.println(Locations.toString());
				return true;
			}else {Bukkit.broadcastMessage("Block Is Not a Dispenser");}
				return true;
				
		
		}
		else if (cmd.getName().equalsIgnoreCase("ctpunregister") && sender instanceof Player) {
			Player player = (Player) sender;
			Block block = player.getTargetBlock((Set<Material>) null, 100);
			if (block.getType().equals(Material.DISPENSER )) {
				Locations.remove(block.getLocation());
					Bukkit.broadcastMessage("Dispenser Unregistered");
				
				}
			else {Bukkit.broadcastMessage("Block Is Not a Dispenser");}
		 
			System.out.println(block.getType());
				return true;
			

		}
		else if (cmd.getName().equalsIgnoreCase("ctpscore") && sender instanceof Player) {
			if ( EventListener.test.isEmpty()) {
				Bukkit.broadcastMessage("Nobody Has Scored yet");
				return true;	
			}else {
			for ( UUID key : EventListener.test.keySet() ) {
				Player player = Bukkit.getPlayer(key);
				Bukkit.broadcastMessage(player.getDisplayName() + ": " + EventListener.test.get(key)) ;;
			}
			return true;
			}
				
			
			}
		else if (cmd.getName().equalsIgnoreCase("ctpscorereset") && sender instanceof Player) {

		EventListener.test.clear();
		Bukkit.broadcastMessage("Scores Cleared");
		return true;
		
		}
		return false;
		}
	}
		
	

	

