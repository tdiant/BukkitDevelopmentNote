package tdiant.bukkit.stupidlogin.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import tdiant.bukkit.stupidlogin.LoginManager;

public class PlayerLoginCommand implements Listener, CommandExecutor {
	@EventHandler //用来拦截除了登录插件以外的指令
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		if( LoginManager.isLogin(e.getPlayer().getName()) )
			return;
		
		e.setCancelled(true);
		if( e.getMessage().split(" ")[0].contains("login") 
				|| e.getMessage().split(" ")[0].contains("register") )
			e.setCancelled(false);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		if(!(sender instanceof Player)) 
			return false;
		
		Player p = (Player)sender;
		
		if(cmd.getName().equalsIgnoreCase("login"))
			loginCommand(p,arg);
		else if(cmd.getName().equalsIgnoreCase("register"))
			registerCommand(p,arg);
		return true;
	}
	
	private void loginCommand(Player p, String[] args) {
		if(LoginManager.isLogin(p.getName())) {
			p.sendMessage("你已经登录了！");
			return;
		}
		if(LoginManager.isRegister(p.getName())) {
			p.sendMessage(ChatColor.RED+"你还没有注册！");
			return;
		}
		if(args.length!=1) {
			p.sendMessage(ChatColor.RED+"登录指令使用错误！");
			return;
		}
		if(LoginManager.isCorrectPassword(p.getName(), args[0])) {
			p.sendMessage(ChatColor.GREEN+"登录成功！");
			LoginManager.setPlayerLogin(p.getName(), true);
		}
	}
	
	private void registerCommand(Player p, String[] args) {
		if(LoginManager.isLogin(p.getName())) {
			p.sendMessage("你已经登录了！");
			return;
		}
		if(LoginManager.isRegister(p.getName())) {
			p.sendMessage("你已经登录了！");
			return;
		}
		if(args.length!=1) {
			p.sendMessage(ChatColor.RED+"注册指令使用错误！");
			return;
		}
		LoginManager.register(p.getName(), args[0]);
		p.sendMessage(ChatColor.GREEN+"注册成功！请登录！");
	}
}
