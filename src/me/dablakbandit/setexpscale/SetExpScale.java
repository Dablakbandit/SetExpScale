package me.dablakbandit.setexpscale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.dablakbandit.setexpscale.command.SES;
import me.dablakbandit.setexpscale.scale.Advanced;
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
	
	private List<String> getListFromConfig(String s, List<String> def){
		List<String> r = new ArrayList<String>();
		if(getConfig().isSet(s)){
			r.addAll(getConfig().getStringList(s));
			return r;
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
		double cs = getDoubleFromConfig("Custom.Scale", 17.0);
		double cr = getDoubleFromConfig("Custom.Dropped_Ratio", 0.75);
		List<String> def = new ArrayList<String>();
		def.addAll(Arrays.asList(new String[]{"return (level+1)*20.0;"}));
		List<String> method = getListFromConfig("Advanced.Scale", def);
		double ar = getDoubleFromConfig("Advanced.Dropped_Ratio", 0.75);
		this.scale = s.getScale();
		switch(s){
		case Custom:
			Custom c = (Custom)s.getScale();
			c.setScale(cs);
			c.setRatio(cr);
			break;
		case Normal:
			break;
		case Advanced:
			Advanced a = (Advanced)s.getScale();
			a.setRatio(ar);
			a.setup(method);
			break;
		}
	}
	
	public void setScale(Scales s){
		if(s==null){
			s = Scales.Normal;
		}
		System.out.print("[SetExpScale] Using Scale " + s.name());
		switch(s){
		case Custom:
			Custom c = (Custom)s.getScale();
			double cs = getDoubleFromConfig("Custom.Scale", 17.0);
			double cr = getDoubleFromConfig("Custom.Dropped_Ratio", 0.75);
			c.setScale(cs);
			c.setRatio(cr);
			break;
		case Normal:
			break;
		case Advanced:
			Advanced a = (Advanced)s.getScale();
			List<String> def = new ArrayList<String>();
			def.addAll(Arrays.asList(new String[]{"return (level+1)*20.0;"}));
			List<String> method = getListFromConfig("Advanced.Scale", def);
			double ar = getDoubleFromConfig("Advanced.Dropped_Ratio", 0.75);
			a.setRatio(ar);
			a.setup(method);
			break;
		}
		this.scale = s.getScale();
	}
	
	public void onEnable(){
		main = this;
		saveDefaultConfig();
		loadScale();
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("ses").setExecutor(new SES());
	}
	
	public Scale getScale(){
		return scale;
	}
	
	public static SetExpScale getInstance(){
		return main;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
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
