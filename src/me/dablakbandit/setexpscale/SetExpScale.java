package me.dablakbandit.setexpscale;

import me.dablakbandit.setexpscale.scale.Custom;
import me.dablakbandit.setexpscale.scale.Scale;
import me.dablakbandit.setexpscale.scale.Scale.Scales;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SetExpScale extends JavaPlugin implements Listener{

	private static SetExpScale main;
	private Scale scale;
	
	private double getDoubleFromConfig(String s, double def){
		if(getConfig().isSet(s)){
			return getConfig().getDouble(s);
		}else{
			getConfig().set(s, def);
			saveConfig();
			return def;
		}
	}
	
	private String getStringFromConfig(String s, String def){
		if(getConfig().isSet(s)){
			return getConfig().getString(s);
		}else{
			getConfig().set(s, def);
			saveConfig();
			return def;
		}
	}
	
	private void loadScale(){
		String use = getStringFromConfig("Use", "Custom");
		Scales s = Scales.valueOf(use);
		if(s==null){
			s = Scales.Normal;
			System.out.print("[SetExpScale] Unknown Use in config: " + use);
		}
		System.out.print("[SetExpScale] Using Scale " + s.name());
		switch(s){
		case Custom:
			Custom c = (Custom)s.getScale();
			c.setScale(getDoubleFromConfig("Custom.Scale", 17.0));
			c.setRatio(getDoubleFromConfig("Custom.Dropped_Ratio", 0.75));
			break;
		case Normal:
			break;
		}
		this.scale = s.getScale();
	}
	
	public void onEnable(){
		main = this;
		saveDefaultConfig();
		loadScale();
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public Scale getScale(){
		return scale;
	}
	
	public static SetExpScale getInstance(){
		return main;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void expGain(PlayerExpChangeEvent e){
		scale.giveExp(e.getPlayer(), e.getAmount());
		e.setAmount(0);//Set the amount to zero to stop gaining extra exp
	}

	@EventHandler
	public void entityDeathEvent(EntityDeathEvent e){
		if(e.getEntity() instanceof Player){
			e.setDroppedExp(scale.getDroppedAmount((Player)e.getEntity()));
		}
	}
}
