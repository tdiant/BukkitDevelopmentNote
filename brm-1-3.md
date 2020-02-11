# 对象化的MC

Java是一个面向对象的语言. 俗话说得好, Java里万物皆对象.  
事实上, BukkitAPI下的MC世界, 正是一个由许多对象构成的世界.   
下面简述几个常用的类型. BukkitAPI中还有更多的类型. 这只是冰山一角.   

# 玩家(Player)  
服务器内的每一个玩家都有一个Player对象.  
如果你想获取这样的一个对象, 例如, 玩家PlayerName的Player对象, 你可以这样获取：  
```java
Bukkit.getPlayerExact("PlayerName");  
```

> 如果你翻看API, 你会发现存在`Bukkit.getPlayer("PlayerName");`这样的方法来获取.   
> 但是这个方法会“模糊地”获取玩家.    
> 假如服务器内有abc和ab这两个玩家, 如果你想获取abc的Player对象, 万一abc不在线, 你Bukkit.getPlayer("abc");返回的Player对象, 很有可能是ab的, 而不是abc的.   

`Player`是`Entity`的子类. 这意味着`Player`对象是一个确切的实体，那么如果玩家下线，它在服务器内对应的实体便消失，也就不存在所谓的`Player`对象了.  
因此，对一个下线的玩家使用`getPlayer`或`getPlayerExact`方法获取`Player`对象是无意义的. 这会得到null.  
如果要表示一个确切的玩家，无论其是否在线，可以使用`OfflinePlayer`对象.  
Bukkit中给出了`getOfflinePlayer`方法，你可以通过该方法获得一个`OfflinePlayer`对象.
  
# 实体(Entity)  
在MC中, 所有的生物, 例如一只羊, 乃至一个僵尸, 又或者是玩家, 都是生物, 他们都是Entity类型的对象.   
这个概念还可以更加进一步的扩充, 一个被点燃的TNT, 实际上, 它也是一个实体(TNTPrimed).   

# Material、ItemStack  
> 提醒: Bukkit 1.13中, Material枚举发生了**翻天覆地的改动**, 导致以前的插件无法更好兼容1.13, 以前的教程可能与实际环境有出入.

在Bukkit当中, 存在一个Enum(枚举)类型的`Material`, `Material`中含有各种物品和方块的种类.  
值得注意的是, 某些物品的Material和该物品放置后的方块的Material不同.  

例如, MC中一个苹果的种类是`Material.APPLE`; 石头方块的种类是`Material.STONE`;  

特殊的是, 某些物品与其对应的方块Material不一致, 例如红石比较器.  
红石比较器**物品**的种类是`Material.REDSTONE_COMPARATOR`, 而放置后的**方块种类**又分为`Material.REDSTONE_COMPARATOR_ON`(开启状态), `Material.REDSTONE_COMPARATOR_OFF`(关闭状态)两种, 红石比较器方块的种类不能用`Material.REDSTONE_COMPARATOR`来表示.

ItemStack用于反应一种描述物品堆叠的方式.  
一个ItemStack的实例, 囊括了物品的种类（其对应的Material）和数量等信息.  
例如, 玩家手中拿着三个苹果. 玩家手中的这三个苹果, 实质上是一个ItemStack, 它包括了这三个苹果的种类（Material.APPLE）、数量（3）与其他的一些信息. 

下面的例子中我们创建了几个ItemStack:  
```java
ItemStack item1 = new ItemStack(Material.DIAMOND); //一个钻石
ItemStack item2 = new ItemStack(Material.DIAMOND,2); //两个钻石
ItemStack item3 = new ItemStack(Material.WOOL,1); //一个羊毛(白色的)
ItemStack item4 = new ItemStack(Material.WOOL,1,15); //一个羊毛(黑色的)

item3.setDurability(0); //设置item3的耐久度为0, 对于羊毛、木板等就是设置颜色的 "子ID", 对于钻石剑那就是耐久度了.
item1.setAmount(20); //设置item1数量为20
Material m = item1.getType(); //得到item1的类型, 也就是Material.DIAMOND
```

Lore是物品的描述信息, 请看下图:  
![](http://miao.su/images/2018/08/15/QQ201808151647582b05e.png)  
该物品的Lore为:  
```
§8材质: §b铜锌合金
§8电荷量: §b10.0J
```
我们来给item1也设置这样的Lore.  
```java
ItemMeta im1 = item1.hasItemMeta()?item1.getItemMeta():Bukkit.getItemFactory().getItemMeta(item1.getType());
List<String> loreList = new ArrayList<>();
loreList.add("§8材质: §b铜锌合金");
loreList.add("§8电荷量: §b10.0J");
im1.setLore(loreList); //设置lore
// 这样设置名称: im1.setDisplayName("蓄电池 - I");
item1.setItemMeta(im1);

//下面这个是错误示范, 必须把im1 get得到后再set回去, 否则诸如下面这样无法生效
item1.getItemMeta().setLore(loreList);
```

# Location
任何一个坐标都可由一个Location代表.  

常见的实体对象是Entity的子类，故都提供了`getLocation`方法，返回的Location代表着它们的坐标位置.  
值得一提的是，如果应用`getLocation`获取实体位置，那么获取的位置是它的脚. 例如`Player.getLocation()`所获取的是玩家的脚的位置.  
对于这些实体对象，如果想修改他们所在的坐标位置，Bukkit没有提供`setLocation`方法，而是提供了`teleport`方法. 通过`teleport`方法可以传送某个实体.  

Location对象的基本使用也十分简单.  

**BlockLocation**  
Location中提供了`getBlockLocation()`、`getBlockX()`、`getBlockY()`、`getBlockZ()`四个方法.  
对于一个方块而言，其坐标的XYZ值均为整数，所以这些方法所获取的是此Location对应的最精确方块的坐标.  
通俗的理解，可以认为获取的是将XYZ四舍五入后的坐标值.  
`getBlock()`获取的此Location对应的最精确的方块的`Block`对象.  

**坐标运算**  
Location提供`add`（加）、`subtract`（减）方法.  

**两点间距离**
Location提供`distance`方法，参数为另一个Location，返回值为double，代表两点间距离.  
*Location还提供`distanceSquared`方法，代表两点间的方块距离，遵循四舍五入.*  

# Block
任何一个方块都可以用Block对象表示.  

在BukkitAPI中，任一世界里有且仅有一个方块对应着三个确切的XYZ值. 也就是一个坐标(XYZ都是整数)只对应一个方块.  

`getType`与`setType`方法可以设置方块的类型.  
*在1.9之前的版本还令设`getTypeId`与`setTypeId`方法. 随着Mojang官方废弃物品ID，BukkitAPI也废弃了这两个方法，并最终删除.*  

# World
在Minecraft中，每一个世界都是一个World对象. 一个World以若干Chunk（区块）组成.

# Inventory
对于玩家背包、箱子里存放的所有ItemStack对象, 我们可以认为他们都储存在了一个`Inventory`对象里.   
也就是意味着, 一个箱子、一个玩家都对应他们专属的`Inventory`对象, 用来储存它们存放着的物品.  
*不要认为箱子、背包是Inventory, 这是错误认知.*

## 简单操作Inventory
我们先以操作玩家背包为例, 练习`Inventory`的使用.
```java
//假设p是一个Player对象的变量
Inventory inv = p.getInventory();
inv.addItem(new ItemStack(Material.DIAMOND));
```
这样就给玩家背包里加了一个钻石, 如果玩家背包已满, 什么都不会发生.

每一个`Inventory`是一个储存物品GUI的体现, 每一个储存物品的格子都有自己的编号(index). 以玩家背包为例:  
![](http://www.miao.su/images/2018/08/15/QQ20180815165229d7c28.png)

这也就意味着:  
```java
inv.setItem(36,new ItemStack(Material.DIAMOND));
```
就是在玩家物品栏第一个位置放置了一个钻石, 或者代表原来在该处放置的物品被删除且替换成了一个钻石.

还有这些常用操作:  
```java
inv.setItem(36,null); //把36号格子清空
inv.removeItem(item1); //删除item1
if(inv.contains(item1)){ //判断inv里有没有item1
	System.out.println("lala");
}
inv.clear(); //清空整个inventory
// 这样也就意味着:
// p.getInventory().clear(); 是清空玩家背包
```

## 箱子GUI

服务器里经常会利用箱子的GUI做“按钮菜单”功能. 如何制作一个这样的箱子GUI呢?  
![](http://www.miao.su/images/2018/08/15/DoubleChest-slotscb78b.png)  

```java
Inventory inv = Bukkit.createInventory(player, 6*9, "URARA!"); 
//第一项是主人在这里, 可以设打开界面的玩家Player对象(还记得Inventory和箱子或玩家背包等一一对应吗)
//第二项必须是 9n (n∈N+且1≤n≤6)
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
	if(e.getInventory.getTitle().equals("URARA!"))
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
