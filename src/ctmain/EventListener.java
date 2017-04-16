package ctmain;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import org.bukkit.util.Vector;
public class EventListener implements Listener {
	 public static HashMap <UUID, Integer> test = new  HashMap <UUID, Integer>();
	private final ClayTargetPlugin plugin;
	public EventListener(ClayTargetPlugin plugin) {
	this.plugin = plugin;
}
public int blockcount;
public int TaskID;
public int counter = 0;
List<String> array = new ArrayList<String>();

@SuppressWarnings("deprecation")
@EventHandler
public void onDispense(BlockDispenseEvent e){
	if ( ClayTargetPlugin.Locations.contains(e.getBlock().getLocation())) {
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
	}else {}
}

@EventHandler
public void onGround(EntityChangeBlockEvent event) { 
	if (event.getEntity() instanceof FallingBlock) {
		if (array.contains(String.valueOf(event.getEntity().getEntityId()))) {
			array.remove(String.valueOf(event.getEntity().getEntityId()));
			event.setCancelled(true);
		}
		else{
		}
	}
	}


@EventHandler
public void onArrowland(ProjectileHitEvent e)  {
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
			} else{
				test.put(Pid, 1);
		}
			Bukkit.broadcastMessage("Good Shot " + player.getDisplayName() + " Your Score is Now " + test.get(Pid));
				ent.setGlowing(false);
				ent.getWorld().createExplosion(ent.getLocation(), 2);
				ent.remove();

		}
		}
	
}

		
}
}
