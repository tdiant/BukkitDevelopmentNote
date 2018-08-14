<p style="font-size:24px;">命令执行器</p>

# 认识命令机制
MC中的命令是一个字符串, 用来实现游戏内高级功能.  

在MC客户端中, 玩家将在聊天框内输入命令.  
**当且仅当在“聊天”内, 命令与普通的聊天内容的区别在于其内容的第一个字符是一个斜杠`/`**.  

该字符串中的空格表示一个分隔, 开头的一节为命令的名称.  
除去命令的名称, 剩下的部分从空格处断开可以分成一个数组.  

例如, `a b c`是一个命令, 其命令名称为`a`, 其参数可用一个数组`args`表示为:  
```java
args[0]: "b"
args[1]: "c"
```

# 定义新命令
如果我们需要定义一个新的命令, 首先我们需要在`plugin.yml`文件中增加相关信息:
```yaml
name: HelloWorld
main: tdiant.helloworld.HelloWorld
version: 1
author: tdiant
commands:
  rua:
    description: RUA!RUA!RUA!
```

在`plugin.yml`文件里, 我们增加了`commands.rua`键, 这就可以代表注册了一个`rua`命令. 我们给他增加了一个`description`子键表示对该命令的描述, 描述信息会出现在`/help`菜单里.  

请注意, 请尽可能不要在plugin.yml文件里出现中文! 这会出现问题!

`commands.命令名`键可以有很多个子键, 这些都不是必须添加的, 甚至它可以没有子键. 具体子键如下:  

| 键 | 用途 | 例子 |
| -----   | -----   | ---- |
| description | 描述作用. 将会在/help中显示 |  `description: "I am a cute command."` |
| aliases | 设置别名. 比如登录插件login命令也可以用/l命令代替 | `aliases: [l, log]` |
| permission | 设置命令需要的权限 | `permission: rua.use` |
| permission-message | 没权限时的提示语 | `permission-message: "YOU HAVE NO PERMISSION!"` |
| usage | 命令的用法 | `usage: /<command> YOUR_NAME` |

注意:  
1. `<command>`在`usage`里可以代表你的命令名.  
2. 你的命令设置了`aliases`后命令名不能按照`aliases`称呼. 比如你给`login`命令设置了`aliases: [l]`你不能也叫他`l`命令, 它还是`login`命令.  
3. 不推荐使用`permission`和`permission-message`, 因为`plugin.yml`里出现中文爱出问题. 事实上, 我们可以用`Player.hasPermission`方法在监听命令的时候自己亲自判断有没有权限.
4. 如果一个名称被别的插件注册了或设置为了某个命令的别称, 会出现冲突问题, 尽量避免.
5. 别弄中文的命令, 如果想搞, 去试试监听`PlayerCommandPreprocessEvent`.

# onCommand
我们可以类似`Listener`, 做一个`CommandExecutor`监听命令.  

```java
public class DemoCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		sender.sendMessage("HI!");
		return true; //true代表命令执行没问题, 返回false的话Bukkit会给命令输入方一个错误提示语
	}
}
```

然后也同理, 在onEnable里加入注册:  
```java
Bukkit.getPluginCommand("rua").setExecutor(new DemoCommand());
```
但是如果onCommand方法放在了主类里, 那就不需要注册了.  

`onCommand`方法有四个参数, 分别为:  
1. `CommandSender sender` —— 命令输入方, 实际传入的有可能是Console, 有可能是Player或者其他情况.    
2. `Command cmd` —— 所执行的命令对象.  
3. `String[] args` —— 参数. 例如`/rua a b`的话, `args[0]`为`a`, `args[1]`为`b`.  

如果你的命令希望只被玩家使用, 通常这样判断:
```java
if(!(sender instanceof Player)){
	sender.sendMessage("你不是玩家！不能用！");
	return true; //不返回true, Bukkit还会显示出来一串错误提示, 你可以试试看.
}
```

判断完为玩家后, 若希望判断其有没有权限执行命令, 可以:
```java
Player p=(Player)sender; //sender可以直接强转为Player
if(p.hasPermission("rua.use")){
	p.sendMessage("你有权限！");
}
```
玩家将会在聊天区域内看到输出:  
```java
你有权限！
```

Bukkit内可以用`ChatColor`表示颜色前缀, 例如:  
```java
p.sendMessage(ChatColor.RED+"你输错了！"); //输出红色的 "你输错了"
p.sendMessage(ChatColor.RED+"还可以"+ChatColor.YELLOW+"两种颜色混着用！");
p.sendMessage(ChatColor.BOLD+"猜猜我会显示成什么效果");
p.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"猜猜我会显示成什么效果");
p.sendMessage(ChatColor.BOLD+""+ChatColor.RED+"猜猜我会显示成什么效果");

String str = "&4哈哈"; //假如你从配置文件里读出来了一串 "&4哈哈".
p.sendMessage(str); //这样会显示出 "&4哈哈", 不带颜色
p.sendMessage(ChatColor.translateAlternateColorCodes('&',str)); //这样就带颜色了
```

还有其他的好玩的东西, 把下面的代码放在`onEnable`方法里试试看:  
```java
System.out.println(ChatColor.RED+"猜猜我是什么效果"); 
this.getLogger().info(ChatColor.RED+"你再猜猜我是什么效果");
```
以后推荐您用`getLogger().info`方法代替`System.out.println(也就是sout、sysout方法)`!

在实际应用的时候, 还要小心`args.length`! 玩家只输入`/rua`没有参数的时候, 小心因为自己的疏忽造成`ArrayIndexOutOfBoundsException`!
