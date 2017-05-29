package ctmain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;


public class EventCommandListener implements Listener, CommandExecutor {
	
	private HashMap <UUID, Integer> test = new  HashMap <UUID, Integer>();
	private boolean toggle = true;
	public List<Location> Locations = new ArrayList<Location>();
	private final ClayTargetPlugin plugin;
	
	public EventCommandListener(ClayTargetPlugin plugin, List<Location> locations, boolean toggle) {
		this.plugin = plugin;
		this.Locations = locations;
		this.toggle = toggle;
	}
	public int blockcount;
	public int TaskID;
	public int counter = 0;
	List<String> array = new ArrayList<String>();
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDispense(BlockDispenseEvent e){
		if (toggle == true) {
			if (Locations.contains(e.getBlock().getLocation())) {
				e.setCancelled(true);
				String something = new String(e.getBlock().getState().getData().toString());
				String[] face = something.split(" ");
				World world = e.getBlock().getWorld();
			    int x = e.getBlock().getX();
			    int y = e.getBlock().getY();
			    int z = e.getBlock().getZ();
				Location newpos = new Location(world, x, y, z);
				Vector vector = null;
				 if(face[2].equals("NORTH")) {
				 		int[] rnd = {160,170,180,190,200};
				 		Random generator = new Random();
				 		int randomIndex = generator.nextInt(rnd.length);
				 		int result = rnd[randomIndex];
				 		newpos.setZ(newpos.getZ()-2);
						newpos.setPitch(-45);
						newpos.setYaw(result);
					} if(face[2].equals("SOUTH")) {
						int[] rnd = {-20,-10,0,10,20};
				 		Random generator = new Random();
				 		int randomIndex = generator.nextInt(rnd.length);
				 		int result = rnd[randomIndex];
				 		newpos.setZ(newpos.getZ()+2);
						newpos.setPitch(-45);
						newpos.setYaw(result);
					} if(face[2].equals("WEST")) {
						int[] rnd = {70,80,90,100,110};
				 		Random generator = new Random();
				 		int randomIndex = generator.nextInt(rnd.length);
				 		int result = rnd[randomIndex];
				 		newpos.setX(newpos.getX()-2);
						newpos.setPitch(-45);
						newpos.setYaw(result);
					} if(face[2].equals("EAST")) {
						int[] rnd = {-70,-80,-90,-100,-110};
				 		Random generator = new Random();
				 		int randomIndex = generator.nextInt(rnd.length);
				 		int result = rnd[randomIndex];
						newpos.setX(newpos.getX()+2);
						newpos.setPitch(-45);
						newpos.setYaw(result);
					} if(face[2].equals("UP")) {
						System.out.print("Incorrect Block Placement");
						return;
					} if(face[2].equals("DOWN")) {
						System.out.print("Incorrect Block Placement"); 
						return;
					}
					Byte blockdata = 9;
					FallingBlock block = newpos.getWorld().spawnFallingBlock(newpos, Material.SAND,blockdata);
					array.add(String.valueOf(block.getEntityId()));	
					vector = newpos.getDirection();
					block.setGlowing(true);
					block.setDropItem(false);
					block.setVelocity(vector.normalize().multiply(1.5));	
			}
		
		}
	}
	
	
	@EventHandler
	public void onGround(EntityChangeBlockEvent event) { 
		if (event.getEntity() instanceof FallingBlock) {
			if (array.contains(String.valueOf(event.getEntity().getEntityId()))) {
				array.remove(String.valueOf(event.getEntity().getEntityId()));
				event.setCancelled(true);
			}
		}
	}
	
	
	@EventHandler
	public void onArrowland(ProjectileHitEvent e)  {
		if (toggle == true) {
			if (e.getEntity().getShooter() instanceof Player) {
				Entity ent = e.getHitEntity();
				Player player = (Player) e.getEntity().getShooter();
				UUID Pid = player.getUniqueId();
				int score = 1;
				if (ent instanceof FallingBlock) {
					if (player instanceof Player) {
						if (test.get(Pid) != null ) {
							if (test.containsKey(player.getUniqueId())); {
								test.put(Pid, test.get(Pid) + score );
							}
						}else{
							test.put(Pid, 1);
						}
							List<Entity> players = new ArrayList<Entity>();
							players = e.getEntity().getNearbyEntities(100, 100, 100);
							if ( players.size() > 1 ) {
								for(Entity i : players) {
									if(i instanceof Player)
										i.sendMessage(ChatColor.GREEN + "Good Shot! " + player.getDisplayName() + " Your score is now " + ChatColor.GOLD + test.get(Pid));
								}
							}
							if (players.size() == 1) {
								player.sendMessage(ChatColor.GREEN + "Good Shot! " + player.getDisplayName() + " Your score is now: " + ChatColor.GOLD + test.get(Pid));
							}
							ent.setGlowing(false);
							ent.getWorld().createExplosion(ent.getLocation(), 2);
							ent.remove();
			
					}
				}
			}
		}	
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ctpregister") && sender instanceof Player) {
			Player player = (Player) sender;
			Block block = player.getTargetBlock((Set<Material>) null, 100);
			if (player.hasPermission("ctpperms")) {
			if (block.getType().equals(Material.DISPENSER )) {
				if(Locations.contains(block.getLocation())) {
					sender.sendMessage(ChatColor.RED + "Already Registered");
					return true;
				}
				Locations.add(block.getLocation());
				sender.sendMessage(ChatColor.GREEN + "Dispenser Set. Use /ctpunregister to unregister it");
				ClayTargetPlugin.listener.Locations = Locations;
				return true;
			}else{ sender.sendMessage(ChatColor.RED + "Block Is Not a Dispenser"); }
				return true;
			}else{ player.sendMessage(ChatColor.RED + "You do not have Permission to run this Commands"); }
				
		
		}else if (cmd.getName().equalsIgnoreCase("ctpunregister") && sender instanceof Player) {
			Player player = (Player) sender;
			Block block = player.getTargetBlock((Set<Material>) null, 100);
			if ( player.hasPermission("ctpperms")) {
				if (Locations.contains(block.getLocation())) {
					if (block.getType().equals(Material.DISPENSER )) {
						Locations.remove(block.getLocation());
						sender.sendMessage(ChatColor.GREEN + "Dispenser Unregistered");
						ClayTargetPlugin.listener.Locations = Locations;
						return true;
					}else{ sender.sendMessage(ChatColor.RED + "Block Is Not a Dispenser"); }
		 
					return true;
				}else{
					sender.sendMessage(ChatColor.RED + "This Block Isnt Registered");
					return true;
				}
			}else{ player.sendMessage(ChatColor.RED + "You do not have Permission to run this Commands"); }
		}else if (cmd.getName().equalsIgnoreCase("ctpscore") && sender instanceof Player) {
			if (test.isEmpty()) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Nobody Has Scored yet");
				return true;	
			}else{
				for (UUID key : test.keySet() ) {
					Player player = Bukkit.getPlayer(key);
					sender.sendMessage(ChatColor.DARK_GREEN + player.getDisplayName() + ": " + ChatColor.WHITE + test.get(key)) ;;
				}
				return true;
			}
		}else if (cmd.getName().equalsIgnoreCase("ctpscorereset") && sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("ctpperms")) {
				test.clear();
				sender.sendMessage(ChatColor.GREEN + "Scores Cleared");
				
				return true;
			}else{ player.sendMessage(ChatColor.RED + "You do not have Permission to run this Commands"); }
		}else if (cmd.getName().equalsIgnoreCase("ctptoggle") && sender instanceof Player) {
			if (toggle == true) {
				toggle = false;
				sender.sendMessage(ChatColor.DARK_RED + "Toggle is now false");
				return true;
			}else if (toggle == false) { 
				toggle = true; 
				sender.sendMessage(ChatColor.DARK_GREEN + "Toggle is now True");
				return true;
			}
		}
		
		return false;
	}
}
