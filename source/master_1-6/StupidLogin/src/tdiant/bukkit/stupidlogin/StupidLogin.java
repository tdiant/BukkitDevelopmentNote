package tdiant.bukkit.stupidlogin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import tdiant.bukkit.stupidlogin.command.PlayerLoginCommand;
import tdiant.bukkit.stupidlogin.listener.PlayerLimitListener;
import tdiant.bukkit.stupidlogin.listener.PlayerTipListener;

public class StupidLogin extends JavaPlugin {
	public static StupidLogin instance;
	
	public void onEnable() {
		instance = this;
		
		//Configuration
		this.saveDefaultConfig();  //输出默认配置
		//Listener
		Bukkit.getPluginManager().registerEvents(new PlayerLimitListener(), this);  //注册监听器
		Bukkit.getPluginManager().registerEvents(new PlayerLoginCommand(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerTipListener(), this);
		//Commands
		CommandExecutor ce = new PlayerLoginCommand();  //注册指令，这里两个指令公用同一个Executor
		Bukkit.getPluginCommand("login").setExecutor(ce);
		Bukkit.getPluginCommand("register").setExecutor(ce);
		
		getLogger().info("StupidLogin插件启动！");
	}

	public void onDisable() {
		getLogger().info("StupidLogin插件已经卸载.");
	}
}
