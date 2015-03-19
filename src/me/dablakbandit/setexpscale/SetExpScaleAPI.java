package me.dablakbandit.setexpscale;

import org.bukkit.entity.Player;

public class SetExpScaleAPI {

	private static SetExpScaleAPI main = new SetExpScaleAPI();
	private SetExpScale plugin;
	
	private SetExpScaleAPI(){
		plugin = SetExpScale.getInstance();
	}
	
	public static SetExpScaleAPI getInstance(){
		return main;
	}
	
	public void giveExp(Player player, double amount){
		plugin.getScale().giveExp(player, amount);
	}
	
	public void removeExp(Player player, double amount){
		plugin.getScale().removeExp(player, amount);
	}
	
	public void setExpLevel(Player player, int level){
		player.setLevel(level);
	}
	
}
