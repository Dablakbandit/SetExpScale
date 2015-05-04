package me.dablakbandit.setexpscale.scale;

import org.bukkit.entity.Player;

public abstract class Scale {

	public abstract void giveExp(Player player, double amount);
	public abstract void removeExp(Player player, double amount);
	public abstract void setExp(Player player, double amount);
	public abstract int getDroppedAmount(Player player);
	public abstract int getExp(Player player);
	public abstract int getExpToLevel(Player player);
	
	public enum Scales{
		Custom(me.dablakbandit.setexpscale.scale.Custom.getInstance()),
		Normal(me.dablakbandit.setexpscale.scale.Normal.getInstance()),
		Advanced(me.dablakbandit.setexpscale.scale.Advanced.getInstance());
		
		private Scale s;
		
		private Scales(Scale s){
			this.s = s;
		}
		
		public Scale getScale(){
			return s;
		}
	}
	
}
