# Bukkit类与箱子GUI的实现

服务器里经常会利用箱子的GUI做“按钮菜单”功能. 有些服务器可能利用`ChestCommand`插件做出了各种花样的菜单.   
如何写一个插件来实现这样的箱子GUI呢?  

# Bukkit类

我们早在事件监听注册监听器时就已经见过`Bukkit`类了.

```java
Bukkit.getPluginManager().registerEvents(this,this);
```

`Bukkit`类是服务器的单例. 我们可以通过它操作服务器.   
例如, 你可以用`Bukkit.banIP("某个IP")`来封禁某个IP号. 更多的用法可以在JavaDoc上查到.

你也可以利用`Server`对象操作服务器, 二者几乎没有差别(`Bukkit`类内部就是操作`Server`对象).
插件主类提供`getServer()`方法, 返回值就是一个`Server`对象.

# Inventory的使用

箱子GUI本质是一个Inventory界面. 首先我们需要创建一个Inventory对象出来.   
但我们不必直接`new Inventory(...)`, `Bukkit`类给我们提供了创建`Inventory`对象的方法:

```java
Inventory inv = Bukkit.createInventory(player, 6*9, "URARA!"); 
//第一项是主人, 在这里可以设打开界面的玩家Player对象(还记得Inventory和箱子或玩家背包等一一对应吗)
//第二项必须是 9n (n是1≤n≤6的正整数)
//第三项是标题
ItemStack item_bk = new ItemStack(Material.DIAMOND);

//在四周设置钻石边框
//这里用这样脑残的写法是为了告诉你一个大概的意思
//我相信你实际写的时候不会这么简单粗暴解决问题的, 应该会用上循环解决, 对吧
inv.setItem(0,item_bk);
inv.setItem(1,item_bk);
inv.setItem(2,item_bk);
inv.setItem(3,item_bk);
inv.setItem(4,item_bk);
inv.setItem(5,item_bk);
inv.setItem(6,item_bk);
inv.setItem(7,item_bk);
inv.setItem(8,item_bk);
inv.setItem(9,item_bk);
inv.setItem(17,item_bk);
inv.setItem(18,item_bk);
inv.setItem(26,item_bk);
inv.setItem(27,item_bk);
inv.setItem(35,item_bk);
inv.setItem(36,item_bk);
inv.setItem(44,item_bk);
inv.setItem(45,item_bk);
inv.setItem(46,item_bk);
inv.setItem(47,item_bk);
inv.setItem(48,item_bk);
inv.setItem(49,item_bk);
inv.setItem(50,item_bk);
inv.setItem(51,item_bk);
inv.setItem(52,item_bk);
inv.setItem(53,item_bk);

ItemStack item_button1 = new ItemStack(Material.GOLD);
ItemStack item_button2 = new ItemStack(Material.ANVIL);
inv.setItem(22,item_button1);
inv.setItem(31,item_button2);

//然后可以给玩家打开这个Inventory(注意, 我们还没做限制, 这个时候玩家可以自由的在这个GUI里拿东西出来)
p.openInventory(inv);
```

效果大概是这样的:  
![](http://www.miao.su/images/2018/08/15/QQ201808151748188c576.png)

然后我们监听`InventoryClickEvent`实现功能和限制:  
```java
@EventHandler
public void onInventoryClick(InventoryClickEvent e){
	//从这里可以看出来, 标题不是随意设置的, 我们经常用标题作为区分GUI的标志
	if(e.getWhoClicked​().getOpenInventory().getTitle().equals("URARA!"))
		e.setCancelled(true); //这样玩家就没办法拿出来物品了
		
	//getRawSlot获得玩家点击的格子编号
	//但是玩家点击GUI之外不是格子的地方也会触发InventoryClickEvent, 需要做处理!
	if(e.getRawSlot()<0 || e.getRawSlot()>e.getInventory().getSize() || e.getInventory()==null)
		return;
	
	//自从Mojang把HIM删掉以后, 能触发InventoryClickEvent的只有Player了
	//目前来说可以直接把它强转成Player
	Player p = (Player)e.getWhoClicked();
	
	if(e.getRawSlot()==22){ 
		p.sendMessage("你点击了金锭!");
		p.closeInventory();
	} else {
		p.sendMessage("你没有点击金锭!");
		p.closeInventory();
	}
}
```
基于这个思路, 你可以做出一个有功能的箱子GUI了!

> 思考: 如果遇到了某些能够修改箱子GUI的标题的插件(比如帮助加前缀)  
> 能不能利用 holder 来区分GUI呢?

