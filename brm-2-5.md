<p style="font-size:24px;">Bukkit 的多线程多任务框架</p>

# 前言
本节前半部分内容基本是对Javadoc的复述, 以及使用它们的注意事项. 如果此前您已经使用过了此包, 或者您有良好的文档阅读及应用能力, 建议您先阅读“注意事项”和“小技巧”一栏, 这才是本节教程更重要的知识!

# org.bukkit.scheduler 包结构
Bukkit 的多线程多任务框架放在了此包, 此包只含有三个接口(`BukkitSheduler`, `BukkitTask`, `BukkitWorker`)和一个抽象类(`BukkitRunnable`，实现了java.lang.Runnable). 相关实现在实现了 Bukkit API 的底层服务器代码中(比如CraftBukkit).  
他们之间的关系大致是这样的: `BukkitSheduler` 负责调度/创建任务，并管理他们(类似于线程池). `BukkitTask` 负责存储由 `BukkitSheduler` 调度的单个任务, 并提供获取它们的任务 id 以及取消它们的一系列方法. `BukkitWorker` 是处理对应异步任务的worker线程. `BukkitRunnable` 基本上是对 BukkitScheduler 的包装, 使用它比使用 BukkitScheduler 相对来说更简洁些.

# 访问 org.bukkit.scheduler 的两个入口
一是使用`org.bukkit.Bukkit.getScheduler()`或`org.bukkit.Bukkit.getServer().getScheduler()`获取`BukkitScheduler`实例.
另一个是构造一个继承`org.bukkit.scheduler.BukkitRunnable`的匿名内部类, 就像这样:
```java
new BukkitRunnable() {
    @Override
    public void run() {
        // 您的代码逻辑
    }
}.runxxx();
```
然后再调用 BukkitRunnable 里的各种方法(事实上最终它还是要访问`BukkitScheduler`, 因此两种方法是等效的). 您也可以直接在Runnable内调用BukkitRunnable的方法, 实现自我取消, 等等. 使用BukkitRunnable的优点在于它简单便捷.

# 如何使用
在这里只介绍Bukkit 任务调度API的核心 ———— BukkitScheduler 的使用方法, 并且不对那些已过时的方法做解释说明(通常情况下你不应该使用它们).  
值得注意的是, Bukkit 的调度任务系统是以 Minecraft 的游戏刻为时间单位的, 其中一个游戏刻(又叫做tick, 下文都使用`tick`指代游戏刻)对应现实世界的50ms(也就是说, 20 ticks是一秒). 不过实际上受服务器性能因素的影响, 不一定每一tick都完整地经过了50ms. 所以在您编写Bukkit 插件时, 请把你置身于 Minecraft 的世界里:)  
如果没有特别说明, 下文中所有介绍到的要求提供时间的方法, 均以tick为单位. 方法全名规则是前者为方法返回值, 后者为方法名和相关参数.

## 调度同步任务
### BukkitTask runTask(Plugin plugin, java.lang.Runnable task) 以及 BukkitTask runTaskLater(Plugin plugin, java.lang.Runnable task, long delay)
这是调度**同步任务**的主要的两个方法, 后一个提供了一个`delay`延迟选项, 用于指定调度任务多久后才开始执行. 默认情况下, delay值为1.
### BukkitTask runTaskTimer(Plugin plugin, java.lang.Runnable task, long delay, long period)
这是调度重复任务的方法, 所得的任务是**同步**的, `period`最低值为1，您不能将其设为比1低的值 (若设为0则等效于1, 小于0表示该任务不是重复的).  
由于是同步任务, 您在Runnable的run()方法中的代码, 是运行于服务器主线程的, 所以请仔细评估这些代码的效率, 因为这可能会影响服务器的性能(尤其是TPS指标), 从而降低服务器流畅度. 如果不与 Minecraft 有关, 请放在下面要介绍的异步任务.

## 调度异步任务
### BukkitTask runTaskAsynchronously(Plugin plugin, java.lang.Runnable task) 以及 BukkitTask runTaskLaterAsynchronously(Plugin plugin, java.lang.Runnable task, long delay)
这是调度**异步任务**的主要的两个方法, 同样提供一个`delay`延迟选项, 就不再解释其意了.
### BukkitTask runTaskTimerAsynchronously(Plugin plugin, java.lang.Runnable task, long delay, long period)
这是调度重复任务的方法, 所得的任务是**同步**的. 通常我们使用异步任务来处理非Minecraft的逻辑,比如数据库的CRUD(增删改查)操作.  
在异步任务中, 需要特别注意线程安全问题, 比如您不能随意调用 Bukkit API. 这个问题会稍后予以详细的解释说明.

### int scheduleSyncDelayedTask(Plugin plugin, java.lang.Runnable task) ; int scheduleSyncDelayedTask(Plugin plugin, java.lang.Runnable task, long delay) ; int scheduleSyncRepeatingTask(Plugin plugin, java.lang.Runnable task, long delay, long period)
这三者其实与`BukkitRunnable.runTaskxxx`是一样的，不过返回值变成了任务id, 如果您想在稍后取消任务, 会更麻烦, 因此不推荐使用. 不过方法名表明的含义更清晰 ———— 清楚地告诉代码读者这调度的是同步任务.

# 注意事项
## 线程安全
Bukkit API文档清楚地告诉我们异步任务中不应访问某些Bukkit API, 需要着重考虑线程安全. 大多数 Bukkit API 不是线程安全的.  
什么是线程安全呢?  
> 在拥有共享数据的多条线程并行执行的程序中，线程安全的代码会通过同步机制保证各个线程都可以正常且正确的执行，不会出现数据污染等意外情况。
> “引自百度百科”

大多数集合不是线程安全的, 比如经常使用的`HashMap`、`ArrayList`. 同样适用于非线程安全的对象.  
限于篇幅, 这里不作深入探讨. 想要了解更多, 书籍与搜索引擎是您的好伙伴.  
Bukkit 中的线程安全?  
Minecraft 中几乎所有的游戏逻辑都运行于主线程中, 而插件的大多数逻辑也是运行于主线程中的, 这包括插件命令的执行、(同步)事件的处理等等.  
如果我们调度了一个异步任务, 或者处于异步事件中, 那么就不应当访问与Minecraft游戏内容有关的API(比如操作方块、加载区块、踢出玩家等). 尝试这么做极有可能得到异常, 使得插件崩溃.

## 如何在异步任务中调度同步任务, 以访问 Bukkit 的非线程安全的方法?
一种就是`BukkitScheduler.runTask` (方法不带`asynchronously`字眼). 这返回的永远是同步任务, 可以大胆访问 Bukkit API, 就像这样:
```java
Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
    // 从数据库拉取些数据
    // 执行同步任务
    Bukkit.getScheduler().runTask(ExamplePlugin.instance, () -> player.sendMessage("你好, 世界!"));
});
```
另一种就是`BukkitScheduler.callSyncMethod`, 这个会在之后的小技巧一栏作介绍.
## Bukkit API中哪些操作是非线程安全的, 哪些又是线程安全的?
> 不完整列表. 仅供参考. 不保证线程安全的方法的行为将来会变化. 不对版本差异导致的行为不同作担保.

线程安全的有:
1. scheduler包自身.
2. Player#sendMessage()
> 你可以发现大量插件在AsyncPlayerChatEvent事件中调用player.sendMessage(). 因此我们有理由确信这是线程安全的.
3. PluginManager#callEvent(event)
> 用于触发事件的方法. 在`SimplePluginManager`中, 该方法使用了synchronized关键字对其实例加锁, 因此是线程安全的. 更多细节请阅读源代码.
4. 发包 - sendPacket
> 为何Player#sendMessage()是线程安全的就是因为它. 我们可以深入craftbukkit乃至nms(net.minecraft.server), sendPacket不过是将数据包传入netty管道, 让netty处理. 如果某个方法仅仅执行了发包流程而没有实际从游戏里加载数据, 那么一般可视其为线程安全的. 因此利用`World#spawnParticle`发送粒子效果以及`World#playEffect`向玩家发送特效、`Player#sendTitle`向玩家发title等也是线程安全的. 我们可以把相关数学运算放到异步线程中, 算完再切换线程发粒子特效.

非线程安全的有:
1. 设置/获取方块、加载/生成区块
2. 操作实体
3. 权限检查(是的. 某些情况下这是非线程安全的, 因为插件一同共享权限列表)

## 关闭插件时, 确保取消你调度的所有任务
最简单的方法就是在插件主类的`onDisable`方法写上这一行代码:
```java
Bukkit.getScheduler().cancelTasks(plugin);
```
其中plugin是你的插件实例, 通常是`this`.  
如果不这么做，那么你的插件被关闭之后, 残存的任务(一般是重复任务)仍在运行, 任务会调用相关变量, 而你在关闭插件时如果清理了那些变量, 将会导致一些无法预料的问题.

# 小技巧
## 使用 lambda 表达式替换匿名内部类
自Java 8开始提供对 lambda 表达式的支持. 匿名内部类转 lambda 表达式可使代码看上去更加简洁漂亮. 比如
```java
scheduler.runTask(this, new Runnable() {
    @Override
    public void run() {
        System.out.println("这是从在任务中输出的一句话.");
    }
});
```
可以替换成:
```java
scheduler.runTask(this, () -> System.out.println("这是从在任务中输出的一句话."));
```
是不是觉得匿名内部类多不优雅, 而 lambda 表达式一行就解决了所有问题? 尽早对丑陋的匿名内部类说byebye吧~

## 使用 BukkitScheduler 提供的`callSyncMethod`方法
> 其实这不应出现在这里的. 不过使用这种方法有点门槛, 如果没有学过相关概念, 你可能不知道从何下手. 该方法涉及到了 Java 的 Future 和 Callable 概念. 如果不知道是什么, 可以搜索来查找资料. 相对于线程安全, Future 和 Callable 概念理解起来容易多了.

这也是使你的代码置于服务器主线程执行的方法之一, 通常用于需要在主线程执行操作获取数据并返回给异步线程的场景.  
下面是鄙人对这些概念的粗略理解:
> 常规的Runnable的run方法是没有返回值的, 它是一个void方法. 这时我们需要使用`Callable`, `Callable`的call方法是有返回值的, 值类型受泛型影响. 使用Runnable还有一个缺点:我(Boss)命令手下一位职员做点任务. 命令完后(开线程, 使用Runnable), 我需要等待职员做完任务的一些反馈, 没有职员提供的数据不能继续工作. 然后在职员执行完任务之前我能干嘛? 没办法, 只能等, 无论职员会执行多久. 有没有办法, 在职员执行任务的过程中, 我还可以做点别的事情呢?  

Java提供了Future这个模式. 于是上面的情况变成了这样:
> 我命令手下一位职员做点任务. 命令完后(开线程, task为FutureTask), 我可以做些别的事情了, 比如与某某打情骂俏...... 之后我可以询问那位职员事情做完没有(Future#isDone()), 或者直接问他结果(Future#get()), 这个取值过程是阻塞的, 直到那位职员完成任务后才能报告结果. 如果我等不耐烦了我还可以使他停下来, 不做了(Future#cancel(boolean)). ~~甚至看不顺眼解雇他~~ 等待职员完成任务的同时, 又多了一份愉悦, 何乐而不为呢~

这里就不作更多介绍了. 欲了解更多内容和用法可以参考[Javadoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Future.html) 以及询问搜索引擎.

直接上食用方法吧! 这是一个使用主线程获取当前在线玩家数量并返回的例子:
```java
Future<Integer> future = Bukkit.getScheduler().callSyncMethod(ExamplePlugin.instance, () -> {
    // call方法是可以抛出异常的
    // 假设这个操作有些耗时...这是对主线程的sleep(事实上这最好不要超过50ms)
    Thread.sleep(1000);
    return Bukkit.getOnlinePlayers().size();
});
try {
    // 比如这里是数据库操作过程, 假设连接数据库并进行操作耗时1s, 这时我们应该可以拿到在线玩家数了
    // 如果操作过程小于1s更好, 只要等上面的方法执行完即可
    // future.get()是阻塞的, 直到执行完毕
    int players = future.get();
    // 向数据库写入数据
    System.out.println("玩家数:" + players);
} catch (InterruptedException | ExecutionException e) {
    // 异常处理
}
```
这段代码是在异步任务中运行的.

食用方法可以说是较复杂了, 如果你没有获取数据的需要, 仅仅需要在主线程内运行特定代码, 使用`BukkitScheduler#runTask()`更好. 没有必要为了 bigger 而 bigger, 唯有**simple**得人心.

## 类库推荐
很多开发者喜欢造些轮子, 来简化一些他们觉得繁复的东西.  
如果你的项目有了一定规模, 可能觉得在异步任务里总要写句`runTask`非常繁琐, 而且项目需要使用异步任务的的场景非常多, 这肯定不是办法.  
国外开发者aikar做了一个库, 叫做`TaskChain`, 可以用来解决上述的问题. 该类库使用的方法极其优雅, 而且将 lambda 表达式运用得淋淋尽致.
推荐尝试! ———— [Github传送门](https://github.com/aikar/TaskChain) (如果你的项目中线程间的关系并不复杂, 不必使用此类库, 尽量保持项目苗条而不失风度 :smile:)
