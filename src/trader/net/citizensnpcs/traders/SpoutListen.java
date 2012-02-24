package net.citizensnpcs.traders;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.event.inventory.InventoryClickEvent;
import org.getspout.spoutapi.event.inventory.InventoryCloseEvent;
import org.getspout.spoutapi.event.inventory.InventoryPlayerClickEvent;

public class SpoutListen implements Listener {

	@EventHandler()
	public void onInventoryClick(InventoryClickEvent e){
		this.handleInventoryClick(e, false);
	}
	
	@EventHandler()
	public void onInventoryPlayerClick(InventoryPlayerClickEvent e){
		this.handleInventoryClick(e, true);
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		handleKill(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		handleKill(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e){
		handleKill(e.getPlayer());
	}
	
	private void handleKill(Player p){
		if(!TraderTask.isUseSpout())return;
		TraderTask task = TraderTask.getTasks().get(p);
		if(task==null){
			return;
		}else{
			task.kill();
		}
	}
	
	private void handleInventoryClick(InventoryClickEvent e, boolean isPlayer){
		if(e.getItem()==null)return;
		if(!TraderTask.isUseSpout())return;
		Player p = e.getPlayer();
		TraderTask task = TraderTask.getTasks().get(p);
		if(task==null||task.getMode()==TraderMode.STOCK){
			return;
		}else{
			e.setCancelled(true);
		}
		int slot = e.getRawSlot();
		if(isPlayer){
			if((0<=slot&&slot<5)||(8<=slot&&slot<32)){
				task.handlePlayerClick(slot+4, p.getInventory());
			}else if(32<=slot&&slot<36){
				task.handlePlayerClick(slot-32, p.getInventory());
			}
		}else{
			if(slot>=0&&slot<36){
				task.handleTraderClick(slot, task.getNpc().getInventory());
			}else if(slot>=36&&slot<40){
				task.handlePlayerClick(slot-27, p.getInventory());
			}
		}
		System.out.println();
	}
	
}
