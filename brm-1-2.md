# 事件
  
# 了解事件
## 什么是事件
事件是服务器里发生的事.  
例如, 天气的变化, 玩家的移动. 玩家把树打掉, 又捡起了掉落地上的原木. 这些都是事件.  

事件分为可控事件和不可控事件. 其最大区别在于能不能取消(*也就是能不能setCancelled*).   
不难理解, 玩家如果退出服务器, 这不能被取消, 它是不可控事件. 玩家的移动可以被取消, 它是可控事件.   

## 事件有什么用？

> 想象自己正在做一款登录插件, 登录插件是怎么制作出来的呢?  
*本章下方举例将延续这个题设展开.*

利用BukkitAPI, 你可以**监听事件**, 事件触发时执行某些代码.   
例如, 你可以监听玩家登录服务器, 玩家登录服务器后你可以执行某些代码.

那么, 如果你想写登录插件, 你需要监听玩家登录服务器的事件.  
玩家进入服务器以后, 记录存储起来他的用户名. 等待玩家输入指令进行登录, 登录完毕以后去掉他的用户名.  
然后再监听其他的各种事件(比如监听方块破坏事件), 如果这些事件被触发, 判断是哪个玩家触发的, 看看玩家用户名有没有存储起来, 如果有, 那么他没有登录, 那就把这个事件取消掉.

通过这样的例子可以发现, 事件是一个插件最重要的组成部分!  

# 监听事件

上面我们提到可以实现事件触发时执行某些代码. 实现这个目的的方法就是写一个监听器.  
**监听器实质上是一个实现了`Listener`的类, 其中包含一些带有`@EventHandler`注解的方法.**  

我们继续以上面的登录插件作为展开, 写一个“玩家不登录就不允许移动”的插件出来.  
因为截止到现在还没有说怎么注册命令, 这里我们设定玩家“只要右键空气就可以登录”.  
*这里我们为了偷懒, 下面把主类直接实现`Listener`当做监听器用.*

```java
public class HelloWorld extends JavaPlugin implements Listener{
	private List<String> playerNameList = new ArrayList<String>(); //这是没登录玩家列表

    public void onEnable(){  
        this.getLogger().info("Hello World!");  
        Bukkit.getPluginManager().registerEvents(this,this); //这里HelloWorld类是监听器, 将当前HelloWorld对象注册监听器  
    }  
  
    public void onDisable(){}  

	/*功能一：刚进入服务器的玩家都记录到“小本本”playerNameList上，他们是没登录的玩家*/
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){ //玩家登录服务器就会调用这个方法
		if(!playerNameList.contains(e.getPlayer().getName())) //先判断这个玩家的名是不是记过了
			playerNameList.add(e.getPlayer().getName()); //玩家一登录就给他记上名, 代表他没登录
	}

	/*功能二：没登录的玩家不让移动*/
    @EventHandler //这个注解告诉Bukkit这个方法正在监听某个事件
    public void onPlayerMove(PlayerMoveEvent e){ //玩家移动时Bukkit就会调用这个方法
        if(playerNameList.contains(e.getPlayer().getName()))
		    e.setCancelled(true); //判断玩家是不是没登录, 是则取消事件
    }

	/*功能三：右击空气登录（本质就是从playerNameList把他删了）*/
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){ //玩家交互时会调用这个方法(这个下面会解释)
		if(e.getAction()==Action.RIGHT_CLICK_AIR){ //判断是不是右键空气
			playerNameList.remove(e.getPlayerName());
		}
	}
}
```

从上面的代码我们可以看出每一个事件都对应着一个`XXXEvent`对象. 事件类都以`Event`作为名称的结尾.  
*稍后会详细讲述如何在JavaDoc找到需要的事件*.

**监听器类里由若干个带`@EventHandler`注解, 参数仅为一个`XXXEvent`的方法. 这些事件触发后会触发这些方法, 这就是事件监听的本质.**  
要特别注意, **监听器中带有`@EventHandler`的方法一个只能监听某一个事件, 而不能监听多个事件!** 换而言之, 这也就意味着, **你不能填写两个参数, 实现一个方法同时监听两个事件的目的!**  

这里我们用到了玩家交互事件. 这个事件抽象不易理解.  
确切的来说, `PlayerInteractEvent`指的是玩家与方块交互, 交互指的是左右键方块的几乎一切操作. 具体的解释完全可以在JavaDoc中了解到.  
如果你曾经用过领地插件`Residence`, 你肯定对某个领地的权限`use`印象很深, 这个`use`权限与`PlayerInteractEvent`事件差不多, 可以近似认为`Residence`插件的`use`权限就是通过监听`PlayerInteractEvent`写出来的.

要注意, **监听器必须要注册才能算生效**!   
我们的监听器里的方法都能监听到对应的事件的原因是, 在`onEnable`方法中, 我们写了这样的代码:  

```java
Bukkit.getPluginManager().registerEvents(this,this); //这行代码注册了HelloWorld类为监听器, 如果没有这行代码, 下面所有带@EventHandler注解的方法都不会在事件触发时被调用！
```

*registerEvents方法的第一个参数是监听器，第二个参数是插件主类的实例. 在这里主类就是监听器. 具体你可以在后面了解到.*    

# 理解客户端与服务端的关系
如果你实际去使用上面的那个代码, 你可能会发现一个问题: 玩家移动在游戏里还可以移动, 但是一会儿会被服务器"弹回来".  
这样确实是达到了取消玩家移动的目的, 但是, 为什么最终的效果不是"玩家一点都动不了"呢?

事实上, 我们无法在服务端取消玩家一点也不能移动.  
客户端移动玩家时, 会在客户端显示出移动后的样子, 然后才会传递给服务器玩家移动的信号, 服务端收到客户端的信号后, 服务器才会触发`PlayerMoveEvent`事件, 做出响应.  

也就是说, 客户端与服务端之间, 客户端往往都是"先斩后奏"的. 客户端不管你服务端取不取消, 先那么显示出来再说.  

*如果要是真的想实现让玩家在服务器的某个坐标一点也动不了, 也许需要发挥你的聪明才智了. 让玩家卡在一个透明方块里? 也许有更好的方案? 现在有人已经实现了!*  
*目前我们通常利用设置玩家移动速度的方法来让玩家无法移动!*  

# 查询我们想了解的事件

## 事件是怎么取名的

你可以发现, 玩家移动`PlayerMoveEvent`、玩家进入服务器`PlayerJoinEvent`事件都有明显的特征.  

1. 功能决定名称, 看了名称你就能大致明白它的功能.  
2. 都以`Event`作为结尾. 这也就说BukkitAPI中所有名字最后是`Event`的类都是事件类.
3. 开头的第一个词决定作用范围. 例如上面两个类开头都是`Player`, 这两个类都是与玩家有关的事件类.

所有的事件类都在`org.bukkit.event`包或其子包里.

## 可取消事件与不可取消事件怎么判断
例如`PlayerMoveEvent`在JavaDoc中, 我们可以注意到这些内容:  

```java
public class PlayerMoveEvent
extends PlayerEvent
implements Cancellable
```

`PlayerMoveEvent`事件实现了`Cancellable`接口.  
`Cancellable`中定义了`setCancelled`方法和`isCancelled`方法.  
通过`setCancelled`方法, 你可以在事件触发时设置是否取消该事件. 例如, 如果监听玩家移动, 事件触发时使用`setCancelled`方法, 可以取消玩家移动.  
`isCancelled`方法可以判断该事件是否被取消.

对于不可取消事件, 它们没有实现`Cancellable`接口, 因此它们无法被取消.  
就像玩家退出服务器, 你总不能像刀剑神域一样, 不让玩家退出服务器吧.  

*值得注意的是, 如果玩家并没有改变他的X/Y/Z, 而只是利用鼠标转了一下身, 这也属于玩家移动, 仍会触发`PlayerMoveEvent`事件.*

## 找到我们要找的事件

我们了解了如何监听事件, 那么我们想做到“不让玩家破坏方块”这个功能, 应该怎么做?  
思考后可以发现, 我们需要监听“方块被破坏”这个事件！那破坏方块后触发什么事件? 你需要在JavaDoc中找才能找到！

分析: 破坏方块这个事件是一个与方块有关的事件. 打开JavaDoc你可以发现`BlockXXXXEvent`这类的类有许多.  
你也许会说, 玩家破坏方块为什么不是一个与玩家有关的事件呢？很有道理！你也可以在玩家事件中找找看有没有这样的事件.

JavaDoc左侧上方是所有的包, 点击`org.bukkit.event.block`就能在左侧下方看所有与方块有关的事件了.  
你可以轻松地发现, 在前几个的位置迅速就能看到`BlockBreakEvent`, 根据名字就能判断出, 这就是你想找的方块破坏事件, 打开后看到描述为`Called when a block is broken by a player.`, 很明显, 监听它就对了.  

```java
@EventHandler
public void onBlockBreak(BlockBreakEvent e){
	e.setCancelled(true);
}
```

这样我们就写出了想要的功能.

## 并不是所有的事件都能监听. 

在查阅JavaDoc时你可能发现`PlayerEvent`、`BlockEvent`这种事件.这些都是不可以被监听的事件.  
你不可以通过监听`PlayerEvent`事件来达到一次性监听所有与玩家有关的事件的目的.  
*它们不能被监听的原因是没有做HandlerList. 在这里不多说明, 后面讲述如何自己做一个自定义事件时你会明白.*  

一般来说，如果事件名由两个词构成(例如`PlayerEvent`)都不能监听, 大多数事件都可以监听.

你可能好奇, 常见的登录插件都是把所有需要的玩家事件都写了`@EventHandler`注解方法一个个监听的？  
答案是, 的确如此. 你要想写登录插件, 你就应该去监听许许多多事件, 累也没办法, 就得这样写.

# EventHandler注解的参数
##监听优先级
想象一下, 如果有两个插件, 他们同时监听玩家移动. 其中一个插件判断后发现玩家没有充够450块钱, 于是它取消了这名玩家的移动. 但是另外一个插件判断后发现玩家非常帅, 于是它允许了这名玩家的移动.  
那么就会存在问题: 有一个插件`setCancelled(true)`, 而又有插件`setCancelled(false)`. 应该以谁为准?  
那就要看监听优先级了!

下面是两个插件处理`PlayerMoveEvent`的部分:
A插件:
```java
    // A插件
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerMove(PlayerMoveEvent e){
	    System.out.println("testA");
	    e.setCancelled(true);
	}
```
B插件:
```java
    // B插件
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent e){
	    System.out.println("testB");
	    e.setCancelled(false);
	}
```
在实际的运行中, 当玩家移动时你会发现, 控制台中先输出了`testA`后输出了`testB`, 玩家都在服务器内可以自如移动.  
这意味着A插件第一个响应了玩家移动, 然后B插件才相应的玩家移动.  
`@EventHandler`注解有一个成员叫做`priority`, 给他设置对应的`EventPriority`, 即可设置监听优先级. 在上面的例子中, Bukkit会在所有的LOWEST级监听被调用完毕后, 再去调用HIGHEST级监听.  

`EventPriority`提供了五种优先级, 按照被调用顺序,为:  
LOWEST < LOW < NORMAL(如果你不设置, 默认就是它) < HIGH < HIGHEST < MONITOR .  
其中, LOWEST最先被调用, 但对事件的影响最小. MONITOR最后被调用, 对事件的影响最大.

## ignoreCancelled
`@EventHandler`注解除了`priority`之外, 还有`ignoreCancelled`. 如果不设置, 它默认为false.  

让我们回到上面的A插件与B插件的例子中. 我们把B插件的`onPlayerMove`改成这样:
```java
    // B插件
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent e){
	    System.out.println("testB");
	    e.setCancelled(false);
	}
```
可以发现, 后台只输出了`testA`, 玩家无法在服务器中移动. 这说明B插件的`onPlayerMove`没有被触发.  
如果有其他监听已经取消了该事件, 设置`ignoreCancelled`为`true`将可以忽略掉这个事件, 所以B插件的`onPlayerMove`方法没有被触发.

## 监听器的注册
可能你已经发现了, 在之前的代码中, 我们都会在`onEnable`方法中插入这样的语句:  
```java
Bukkit.getPluginManager().registerEvents(this,this);  
```
当时解释的是, `registerEvents`方法注册了该监听器.  
如果没有这样的注册语句, 那么Bukkit就不会在事件触发时调用监听器类的对应方法.  

该方法的第一个参数是监听器, 第二个参数是插件主类的实例. 当时由于我们为了偷懒, 直接把主类实现了`Listener`作为监听器, 因此我们可以这样写.    
可我们不能写插件的时候把代码都堆在主类中. 这也就意味着, 我们可以把其他类实现`Listener`, 用同样的方式注册它, 这样我们就可以把监听事件部分的代码放在别的地方, 使插件代码更有条理性.     

我们新创建一个类, 让它实现`Listener`, 再写对应的方法监听玩家移动, 就像这样:  
```java
public class DemoListener {
    @EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
	    System.out.println("PLAYER MOVE!");
	}
}
```
现在我们在主类的`onEnable`方法里, 就可以注册它了!  
```java
Bukkit.getPluginManager().registerEvents(new DemoListener(), this);  
```

# 常用事件简介

这里可能罗列不会全面, 在我想到哪些“坑事件”后会列在这里.

## 登录、进入服务器
BukkitAPI中与登录有关的常见的有: `PlayerLoginEvent` `PlayerJoinEvent`.  
值得注意的是, 所有玩家进入服务器的事件都是不可取消事件.  

在玩家尝试连接服务器时, 会触发`PlayerLoginEvent`, 玩家完全地进入服务器后, 会触发`PlayerJoinEvent`.  
在`PlayerLoginEvent`触发的时候, 你不可以操控玩家`Player`对象获取其背包等信息, 而仅可以获取UUID、玩家名和网络信息(IP等)等.  
*顺便一提, 玩家如果不在线, 你不可以通过BukkitAPI操控其背包.  *
`PlayerJoinEvent`触发时, 服务器内将会出现玩家实体. 此时你可以当做玩家完全进入服务器, 对其自由操作.

打个比方, 你家有一扇防盗门, 有人想进入你家.  
首先他需要敲门, 在门外喊出自己的基本信息(名字等), 这是`PlayerLoginEvent`触发的时候. 如果你想从他背包里拿出东西, 不可以, 因为他在门外面.  
当你给他打开门, 他进了你家中站稳了以后, 这是`PlayerJoinEvent`触发的时候, 这时候不管你是想打他还是想拿走他的东西, 都可以.

## 玩家移动
在上面我们已经提及过, 玩家移动是“先斩后奏”被触发的. 具体请见上文.  

## 玩家打开背包
也许你会看到`InventoryOpenEvent`. 根据描述你大概明白, 类似右击箱子后出现的那种带格子的界面被打开可以被监听.  
但是有一件事很重要: 玩家按E打开背包是没办法被监听的.  

一般如果要实现禁止玩家打开背包, 其实最常规的做法就是开一个`BukkitRunnable`, 定时调用`p.closeInventory()`关闭玩家正在打开的背包实现的.  
*这里不详细讲述具体如何操作, 感兴趣可以在QQ群中问一些有经验的开发者.*  
*后面会讲述Runnable, 也许看后你会明白如何操作.*

感兴趣可以看看这个帖子: https://www.mcbbs.net/thread-965760-1-1.html  
