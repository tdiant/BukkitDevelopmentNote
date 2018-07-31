<p style="font-size:24px;">事件与事件的监听</p>
  
# 事件
事件用来描述服务器内的一切变化行为. 所有的事件都可以被监听.     
举例, 天气的变化, 玩家的移动. 玩家把树打掉, 又捡起了掉落地上的原木. 这四个都是事件.  

这也就意味着, 利用BukkitAPI, 我可以监听天气的变化, 在天气改变时执行某些代码.   

事件分为可控事件和不可控事件. 其最大区别在于能不能禁止(*也就是能不能setCancelled*).   
不难理解, 玩家如果退出服务器, 这不能被禁止, 它是不可控事件. 玩家的移动可以被禁止, 它是可控事件.   

# 监听器  
我们往往会想在一个事件被触发时, 执行一些代码, 此时我们便需要监听器.  

监听器本质上是一个实现`Listener`类的类. 其中有一些带有`@EventHandler`注解的方法.  
当某个事件触发时, Bukkit将会对应地调用这些带`@EventHandler`方法.   
监听事件简而言之就是说, 你告诉Bukkit, 在XX事件触发时, 调用某个你做标记的方法.  

在BukkitAPI中, 每一个事件都对应一个类. 事件类一般以Event结尾, 所以它们都叫做XXXXXEvent. 所有的事件都在org.bukkit.event包内. Bukkit中所有的事件均可在JavaDoc中查阅.   
```
//这里为了偷懒, 直接把主类实现Listener作为监听器
//如果是特别小的插件可以这样做, 节省开发时间成本(偷懒)
//如果是大型插件, 建议采用之后我们将介绍的方法, 让代码更有条理, 方便开发
public class HelloWorld extends JavaPlugin implements Listener{
    public void onEnable(){  
        this.getLogger().info("Hello World!");  
        Bukkit.getPluginManager().registerEvents(this,this);  
    }  
  
    public void onDisable(){}  
  
    @EventHandler
    public void onPlayerMove(PlayerQuitEvent e){  
        System.out.println("玩家退出了！");  
    }  
}
```
我们可以像上面那样监听一个事件.   
`PlayerQuitEvent` 事件可以监听玩家退出服务器, 这是一个不可控事件. 这可以实现监听到玩家退出服务器, 并且在后台输出`玩家退出了！`的字符串.   
在`onEnable`方法中, `registerEvents`方法注册了该监听器. 
*registerEvents方法的第一个参数是插件主类的实例, 第二个参数是监听器. 在这里主类就是监听器. 具体你可以在后面了解到*.    

监听器中带有`@EventHandler`的方法一次只能处理一个事件, 而不能处理多个事件, 所以你不可以在参数位置加多个Event的参数达到一个方法相应多个事件的目的.  