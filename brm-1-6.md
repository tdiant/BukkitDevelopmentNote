<p style="font-size:24px;">插件制作实例</p>

这是第一单元的最后一章.  
在第一单元中, 你已经对Bukkit中常见且重要的系统有了初步的认识.

那么如何利用这些认识写一个Bukkit插件呢?  
在本节中, 将着重讨论“制作插件的思路”, 没有新内容.

# 插件制作的思路
## 做插件需要准备
如果你想开始做一个插件, 那你需要**①准备服务端Jar文件**, 在IDE中创建Project后**②创建主类和`plugin.yml`**. 这是编写一切插件的基本和开始.  

大多数开发者更倾向于使用Maven或Gradle.  
作者在此认为Maven和Gradle与插件开发相关的内容超出了本文的内容范围, 推荐您参考这些优秀文章:  
[如何利用Maven来管理你的插件 - author: 莫老](http://www.mcbbs.net/thread-711754-1-1.html)

## 如何做我想做的功能
如果你想做登录插件, 你需要监听登录事件和玩家的输入事件; 如果你想做小游戏, 你可能会监听玩家的移动、右键等事件.  

由此看来, 插件往往基于事件. 事件的监听是一个插件的灵魂.  
插件往往就是监听事件触发后执行某个代码, 由此运行的.  
在制作插件前的设计过程当中, 不妨思考一下 "我的插件应该监听哪些事件", 然后去寻找BukkitAPI中这些事件的对应的类, 监听它们.

对于怎么找到自己想要的事件, 相比你已经发现, 相应事件类的名字有特殊的含义.  
例如`PlayerMoveEvent`. 其实根据名称就能判断出来, 这是玩家移动. `EntityDamageByEntityEvent`, 很简单, 会在某个生物被其他生物攻击时被触发.  

`CreeperPowerEvent`, 这个事件也许就不是那么好猜了. 在拿捏不准的时候可以在JavaDoc上查询.  
> 1. SpigotAPI 最新版本JavaDoc: https://hub.spigotmc.org/javadocs/spigot/index.html?overview-summary.html   
> 2. BukkitAPI 1.7.10版本JavaDoc: https://jd.bukkit.org/   
> 3. BukkitAPI 中文版JavaDoc(仅供参考): https://docs.windit.net/Chinese_BukkitAPI/  

实际应用时应主要以1号JavaDoc为主. 在这份JavaDoc中, 右上角处有搜索栏, 可以在这里输入你想搜索的内容.  
比如我想搜索如何发Title, 我可以在这里搜索`title`这个关键字, 你就会轻易发现`Player`类下有`sendTitle`方法, 然后可以找到这个方法的详细介绍.  
每个开发者都会在插件制作过程中经常查看JavaDoc. 这是插件制作所必须的.


## 数据的储存
在学习Java基础的过程中, 你一定学习到了`Map`、`List`等类型. 这些类型在插件编写当中非常常见.  

如果我们想做一个登录插件, 也许我们会把没有登录的玩家名称加在一个`List`里, 然后监听很多事件, 诸如`PlayerMoveEvent`、`PlayerChatEvent`等, 利用`List.contains`就可以判断出玩家是不是登录了, 然后通过控制`event.setCancelled`方法控制是否禁止事件, 这就完成了登录插件的基本结构了.

如果我想实现 关闭服务器后再打开还能保留 的数据, 这种功能的实现思路往往是: 保存数据到配置文件->把数据文件读取出来.  
也就是要学会利用配置文件, 配置文件不只是能帮助你储存配置, 一定要意识到!

# 几个插件样例
也许你已经大致对插件制作有一定的了解了. 那就把知识应用到实际当中, 做个插件试试看.  
请你在下面的内容中**着重注意领悟插件制作的思路**.  
*我们假设服务端测试环境只有我们自己做的那个插件.*

限于文章篇幅, 这里仅做一个小示范.  
希望读者能够看完这个示范后, 能有所感悟. 试试看, 能不能做一个小游戏出来? 你现在完全可以做出来了!

下面的内容你可以在本教程的Git上获取到源代码:  
https://github.com/tdiant/BukkitDevelopmentNote/tree/master/source/

## StupidLogin - 简单的登录插件
~~我一直反复在说登录插件. 不做一个登录插件怎么能行呢? 233333~~  

### 基本准备
首先我们要写好基本的主类和`plugin.yml`, 引入服务端Jar作为Lib. 我这里创建了`StupidLogin`工程作为演示, `plugin.yml`目前设置了`name` `version` `main` `author`, 主类目前如下:  
```java
package tdiant.bukkit.stupidlogin;

public class StupidLogin extends JavaPlugin {
	public static StupidLogin instance;
	
	public void onEnable() {
		instance = this;		
		getLogger().info("StupidLogin插件启动！");
	}

	public void onDisable() {
		getLogger().info("StupidLogin插件已经卸载.");
	}
}
```

然后我们大致设计一下这个插件, 其实这个插件工作原理我已经不必多说, 你应该清楚了:  

**登录注册部分**  
1. 注册登录注册指令.    
2. 监听玩家进入服务器, 把玩家的名字写在 未登录玩家 的List当中.    
3. 玩家登录或注册.(这里注册后还需要输入登录指令)    
4. 玩家输入登录指令后, 把玩家名从 未登录玩家 List 里去掉.    

然后再写一个`Listener`, 监听玩家各种事件例如`PlayerMoveEvent`等, 在玩家未登录时禁止这些事件.

**在开头提醒: 不能监听PlayerEvent、EntityEvent或诸如此类的事件! 限制玩家未登录时的行动必须需要监听一遍所有事件!**

### LoginManager
LoginManager有 查询玩家是否登录、查询是否注册、注册玩家账号、修改是否登录的状态、查询字符串与玩家密码是否一致 的五个功能.  

首先先写出基本的结构, 把构思中的 未登录玩家 的List写出:  
```java
public class LoginManager {
	private static List<String> unloginList = new ArrayList<>();

	//获取玩家是否登录
	public static boolean isLogin(String playerName) {
		return !unloginList.contains(playerName); //如果玩家名字没在列表里就返回true, 代表已经登录
	}
	
	//设置玩家登录状态，flag=true为登录, false为未登录
	public static void setPlayerLogin(String playerName, boolean flag) {
		if(flag) {
			unloginList.remove(playerName); //设置为true就会把玩家从未登录玩家里删除
		} else {
			unloginList.add(playerName); //反之添加
		}
	}
}
```

然后再写出与配置文件有关的三个方法:
```java
	//判断玩家是否注册账号
	public static boolean isRegister(String playerName) {
	    //如果配置文件里有  player_data.玩家名  这个键就代表已经注册
		return StupidLogin.instance.getConfig().contains("player_data."+playerName); 
	}
	
	//让玩家注册账号
	public static boolean register(String playerName, String password) {
		if(isRegister(playerName))
			return false; //如果已经注册就return，阻止下面的操作
			
		//设置  player_data.玩家名.password 键为玩家的密码字符串
		StupidLogin.instance.getConfig().set("player_data."+playerName+".password", password); 
		StupidLogin.instance.saveConfig(); //设置完了别忘了保存！
		return true;
	}
	
	//判断密码是否正确
	public static boolean isCorrectPassword(String playerName, String password) {
		if(!isRegister(playerName))
			return false; //没注册能正确就怪了！
		
		//pass是玩家注册的密码
		String pass = StupidLogin.instance.getConfig().getString("player_data."+playerName+".password");
		return pass.equals(password); //返回结果
	}
```

### 事件监听
然后编写监听器.

首先先编写`PlayerLimitListener`, 用以禁止 玩家未登录时做的操作 ,并且在玩家登录和退出服务器时把玩家加进 未登录玩家 List 里.  
```java
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
	public void onPlayerChat(PlayerChatEvent e) { //不让聊天
		if(e.getMessage().substring(0, 0).equals("/")) //这里不拦截玩家用命令, 后面我们会处理一下限制玩家用命令
			return;
		e.setCancelled(needCancelled(e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) { //不让玩家移动
		e.setCancelled(needCancelled(e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) { //不让玩家跟别的东西交互，约等于屏蔽左右键
		e.setCancelled(needCancelled(e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onPlayerInventory(InventoryOpenEvent e) { //不让玩家打开背包
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
```

为了体现我们的友♂好, 在玩家上线时给玩家发一个提示:  
```java
package tdiant.bukkit.stupidlogin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import tdiant.bukkit.stupidlogin.LoginManager;

public class PlayerTipListener implements Listener {
	
	//玩家进入服务器后的“请您登录”提示语
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().sendMessage(
					LoginManager.isRegister(e.getPlayer().getName())?
							"欢迎回来！请输入/login 密码 登录服务器！":
							"欢迎第一次来到本服务器！请输入/register 密码 注册账号！"
				);
	}

}
```

### 指令

我认为指令部分可以通过代码看懂我的意思.  
在这里我会将login指令和register指令都放在这一个`CommandExecutor`里.  

对了, 要养成整理整齐`onCommand`方法的好习惯哦!
```java
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
```

### 在主类和plugin.yml注册
我们需要把监听器和命令在主类和`plugin.yml`中进行注册.

```yml
name: StupidLogin
main: tdiant.bukkit.stupidlogin.StupidLogin
version: 1
author: tdiant

commands:
  login:
    usage: "/login YOUR_PASSWORD"
    description: "Login your account."
  register:
    usage: "/register YOUR_PASSWORD"
    description: "Register your account."
```

在`plugin.yml`中的注册需要注意**不能出现中文, 请全英文**! 编码推荐UTF-8.

然后就是在主类注册一下了! `onEnable`方法内添加:  
```java
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
```

### 后记
至此, StupidLogin 已经完成! 希望你能阅读一下源码, 体会一下写这个插件的思路!

但是这个登录插件的功能未免显得简单. 有这些缺陷:  
1. 如果玩家密码带空格, 这个插件会出现问题. (提示: 需要对onCommand的args做处理, 做出玩家密码带空格时真正想输入的密码)  
2. 玩家名大小写可能会存在缺陷.   
3. `PlayerLimitListener`监听的事件太少, 可能有想不到的地方. (建议: 反编译或看开源的源代码, 看看AuthMe是如何做禁止的)  
4. `login`、`register`输入起来太麻烦, 没有简便指令.  
5. 没有更换密码功能.  
还可能有其他各种问题......

这些问题如何解决呢? 你来动手试一下吧!
