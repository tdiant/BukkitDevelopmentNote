package tdiant.bukkit.stupidlogin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import tdiant.bukkit.stupidlogin.LoginManager;

public class PlayerLimitListener implements Listener {

	//这里只是把几个常见的情况进行了拦截，玩家说话、玩家鼠标操作和玩家移动.
	//你可以在这里按照这样的方式添加更多的拦截，使其更加安全
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		if(e.getMessage().substring(0, 1).equals("/"))
			return;
		e.setCancelled(needCancelled(e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		e.setCancelled(needCancelled(e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		e.setCancelled(needCancelled(e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onPlayerInventory(InventoryOpenEvent e) {
		e.setCancelled(needCancelled(e.getPlayer().getName()));
	}
	
	private boolean needCancelled(String playerName) {
		return !LoginManager.isLogin(playerName);
	}
	
	// 下面的两个监听用来修改玩家的登录状态
	
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent e) {
		LoginManager.setPlayerLogin(e.getPlayer().getName(), false);
	}
	
	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent e) {
		LoginManager.setPlayerLogin(e.getPlayer().getName(), false);
	}
}
