package me.dablakbandit.setexpscale.scale;

import org.bukkit.entity.Player;

public class Normal extends Scale{

	private static Normal custom = new Normal();
	
	private Normal(){
		
	}
	
	public static Normal getInstance(){
		return custom;
	}

	@Override
	public void giveExp(Player player, double amount) {
		player.setTotalExperience(player.getTotalExperience()+(int)amount);
	}

	@Override
	public void removeExp(Player player, double amount) {
		player.setTotalExperience(player.getTotalExperience()-(int)amount);
	}

	@Override
	public void setExp(Player player, double amount) {
		player.setTotalExperience((int)amount);
	}

	@Override
	public int getDroppedAmount(Player player) {
		int i = player.getLevel()*7;
		if(i>100){
			i=100;
		}
		return (i);
	}

	@Override
	public int getExp(Player player) {
		return player.getTotalExperience();
	}

	@Override
	public int getExpToLevel(Player player) {
		return player.getExpToLevel();
	}
}
