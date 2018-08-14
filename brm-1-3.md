<p style="font-size:24px;">对象化的MC</p>
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
  
# 实体(Entity)  
在MC中, 所有的生物, 例如一只羊, 乃至一个僵尸, 又或者是玩家, 都是生物, 他们都是Entity类型的对象.   
这个概念还可以更加进一步的扩充, 一个被点燃的TNT, 实际上, 它也是一个实体(TNTPrimed).   
  
# Material、ItemStack  
> 提醒: Bukkit 1.13中, Material枚举发生了翻天覆地的改动, 导致以前的插件无法更好兼容1.13, 以前的教程可能与实际环境有出入.

在Bukkit当中, 存在一个Enum(枚举)类型的`Material`, `Material`中含有各种物品和方块的种类.  
值得注意的是, 某些物品的Material和该物品放置后的方块的Material不同.  

例如, MC中一个苹果的种类是`Material.APPLE`; 石头方块的种类是`Material.STONE`;  

特殊的是, 某些物品与其对应的方块Material不一致, 例如红石比较器.  
红石比较器**物品**的种类是`Material.REDSTONE_COMPARATOR`, 而放置后的**方块种类**又分为`Material.REDSTONE_COMPARATOR_ON`(开启状态), `Material.REDSTONE_COMPARATOR_OFF`(关闭状态)两种, 红石比较器方块的种类不能用`Material.REDSTONE_COMPARATOR`来表示.

ItemStack用于反应一种描述物品堆叠的方式.  
一个ItemStack的实例, 囊括了物品的种类（其对应的Material）和数量等信息.  
例如, 玩家手中拿着三个苹果. 玩家手中的这三个苹果, 实质上是一个ItemStack, 它包括了这三个苹果的种类（Material.APPLE）、数量（3）与其他的一些信息. 
