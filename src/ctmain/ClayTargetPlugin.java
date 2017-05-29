package ctmain;


import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class ClayTargetPlugin extends JavaPlugin {
	public static EventCommandListener listener;
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		boolean toggle = true;
		List<Location> Locations = new ArrayList<Location>();
		if (getConfig() == null) {
			this.saveDefaultConfig();
		}
		if (getConfig().getList("Location") != null ) {
			Locations = (List<Location>) getConfig().getList("Location");	
			getLogger().info("Plugin has been Sucessfully Enabled");
		}else{
			getLogger().info("Plugin has been Sucessfully Enabled For the First Time");
			getConfig().options().copyDefaults(true);
		}
		listener = new EventCommandListener(this, Locations, toggle);
		this.getServer().getPluginManager().registerEvents(listener, this);
		this.getCommand("ctpregister").setExecutor(listener);
		this.getCommand("ctpunregister").setExecutor(listener);
		this.getCommand("ctpscore").setExecutor(listener);
		this.getCommand("ctpscorereset").setExecutor(listener);
		this.getCommand("ctptoggle").setExecutor(listener);
	}
	
	@Override
	public void onDisable() {
		getConfig().set("Location", listener.Locations);
		saveConfig();
		getLogger().info("Plugin has been Sucessfully Disabled");
	}
	
	
	
}
		
	

	

