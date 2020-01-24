# 自定义事件

# 自定义事件
我们现在所了解的事件都是Bukkit提供的. 例如, 玩家移动等.  
那如果我们想自己去做一个事件呢?

比如, 我想自己做出来一个`RuaEvent`, 实现在玩家聊天说`rua`的时候触发.  
很明显, Bukkit只会提供玩家发送聊天信息的事件, 肯定不会单独为了实现在玩家聊天发送`rua`的时候单独做个事件. 那应该怎么做?  

首先想到的应该是监听玩家聊天事件, 然后判断玩家聊天发送的内容是什么, 如果是`rua`做我想做的事情. 这是常规的解决方法.  
但是如果我想做一个强化插件, 我想在玩家强化物品的时候触发一个事件给自己和其他插件, 那我应该怎么做? 不如自定义一个属于自己的事件!

这里我们以创建上文的`RuaEvent`事件举例, 我们的大致思路是这样的:  
1. 创建一个`RuaEvent`类.  
2. 监听玩家聊天, 判断玩家聊天内容, 如果是`rua`, 让Bukkit触发我们新建的`RuaEvent`对象.  

我们就先新建一个类`RuaEvent`, 让其继承`org.bukkit.event.Event`类. 在该类中写下这些固定代码:  
```java
public class RuaEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
```
HandlerList储存与监听本事件的监听器相关的对象.  
这意味着Bukkit中注册监听器的本质就是在每个对应的事件HandlerList中加入该监听器的有关对象.  
这也意味着Bukkit中事件的触发本质是遍历被触发事件的HandlerList, 调用监听器对应方法.

> 假如我想让服务器里的玩家触发的所有事件, 已知所有的诸如PlayerJoinEvent等玩家事件都继承了PlayerEvent, 那我可以监听PlayerEvent事件吗?  
> 答案是不可以, 因为PlayerEvent的getHandlerList方法永远会返回null, 结合上面的内容, 你应该可以意识到PlayerEvent是无法正常工作的吧.  
> 所以你只能把所有Player开头的Event监听一个遍才可以达到目的!  

现在我们的自定义事件雏形已经完成. 你可以根据自己的需要添加相关代码!  
这里我们示例的`RuaEvent`代码最终如下:  

```java
public class RuaEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Player p;

	public RuaEvent(Player p){
		this.p = p;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
```

等一等, 这样做出来的事件没有`setCancelled`方法和`isCancelled`方法, 这是不可取消的事件.  
如果想做成可取消事件, 需要实现`Cancellable`接口:  
```java
public class RuaEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private Player p;
	
	private boolean cancelledFlag = false;

	public RuaEvent(Player p){
		this.p = p;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelledFlag;
	}
	
	@Override
	public void setCancelled(boolean cancelledFlag) {
		this.cancelledFlag = cancelledFlag;
	}
}
```

如果是不可取消的事件, 无需实现`Cancelled`.  
截止到现在, `RuaEvent`已经自定义成功, 现在我们只需要做第二步即可:  

1. 如果RuaEvent是个不可取消事件  

```java
@EventHandler
public void onPlayerChat_DEMO1 (PlayerChatEvent e){ //如果RuaEvent是个不可取消事件
	if(e.getMessage().equals("rua"))
		Bukkit.getServer().getPluginManager().callEvent(new RuaEvent(e.getPlayer())); //触发事件
}
```

2. 如果RuaEvent是个可取消事件  

```java
@EventHandler
public void onPlayerChat_DEMO1 (PlayerChatEvent e){ //如果RuaEvent是个可取消事件
	if(e.getMessage().equals("rua")){
		RuaEvent event = new RuaEvent(e.getPlayer());
		Bukkit.getServer().getPluginManager().callEvent();
		if(event.isCancelled()) //这里加判断即可
			e.setCancelled(true);
	}
}
```

*在这里监听了**PlayerChatEvent**，但是此事件已被标记@Deprecated，实际的开发过程中不推荐监听此事件.*    
*实际开发中建议监听的是**AsyncPlayerChatEvent**事件. 注意这是异步监听，用法基本类同于上述事件的监听，具体请参见JavaDoc.*  
