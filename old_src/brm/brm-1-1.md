# 最简单的插件

# Bukkit插件的本质
插件本质是一个基于BukkitAPI的Java应用. 一个插件必须要有 主类 和 `plugin.yml`文件.

例如下面是一个常见插件, 让我们找一下它的主类和`plugin.yml`文件.  

![](pics\1-1-pic1.jpg)

# 简单的插件
在编写自己想做的插件之前, 不妨做一个简单的插件来了解一下Bukkit插件如何编写.    

新建一个Java工程, 导入开服用的服务端jar文件到工程的Libraries中. 创建`tdiant.helloworld.HelloWorld`类作为插件的主类, 并继承`JavaPlugin`类.  
在主类里覆写`onEnable`方法和`onDisable`方法. 完成后, 代码应该类似这样:  

```java
package tdiant.helloworld;  
  
import org.bukkit.plugin.java.JavaPlugin;  
  
public class HelloWorld extends JavaPlugin {
    @Override  
    public void onEnable(){  
        System.out.println("Hello World");
    }  
  
    @Override      
    public void onDisable(){  
        
    }  
}  
```

Bukkit服务端会在插件被启用时调用`onEnable`方法, 被停用时调用`onDisable`方法.

创建`plugin.yml`文件. 打开plugin.yml文件并在其中输入如下信息：  
```yml
name: HelloWorld
main: tdiant.helloworld.HelloWorld
version: 1
author: tdiant
```

> **特别注意: 如果你的插件是基于新版本API(1.13以及以上版本)编写的, 应当在plugin.yml中额外增加`api-version: 1.13`键值对.例如这样:**  
> ```yml
> name: HelloWorld
> main: tdiant.helloworld.HelloWorld
> api-version: 1.13
> version: 1
> author: BakaRinya
> ```
> **这会告诉Bukkit, 这个插件是基于新版API编写的.**  
> 若要兼容1.13及以上版本的同时兼容旧版本, 应特别注意各版本之间的 API 变化(譬如1.12进度系统取代了成就系统, 1.13的 Material 枚举发生了巨大变化).

*注意: 主类的名称并不是固定的, 但是`plugin.yml`文件的名称是固定的.*     

上面的plugin.yml文件逐行分析如下：

| 键 | 意义 | 备注 |
| :-:   | :-:   | :- |
| name | 插件名 | 不允许带有中文和空格, 推荐只含有下划线、英文. |  
| main | 插件的完整主类名 | 例如我这里插件主类为tdiant.helloworld.HelloWorld, 此处则需填写tdiant.helloworld.HelloWorld. |  
| version | 插件版本 | 您可以填写一个合理的String内容, 而不一定必须为数字, 例如可填写v1.0.0 |  
| author | 作者 |  -


可以发现, 当插件Jar被正常加载后, 会在控制台输出`Hello World`字符串, 这标志着我们的HelloWorld插件正常工作.  

![](pics/1-1-pic2.png)

# BukkitAPI中的Logger

## Logger
*这里只是简要提及, 不详细介绍, 只需要知道有这件事即可.*

BukkitAPI“修改”了我们常用的sout (即`System.out.println`), 将其“引入”了BukkitAPI提供的Logger.  
只有通过Logger输出的文本信息才能记录在服务端生成的log文件中.

在BukkitAPI插件开发时, 我们通常不用sout输出想往后台输出给服主看的文本信息, 而应用Logger.  
主类有`getLogger()`方法, 可以利用这个方法获得Logger.  
例如这样:

```java
public class HelloWorld extends JavaPlugin {
    @Override  
    public void onEnable(){  
        this.getLogger().info("Hello World");
    }  
  
    @Override      
    public void onDisable(){}  
}  
```

这样输出信息的方式与sout相比最主要的区别是, 如果你的插件`plugin.yml`里的名称为`Test`那么:

```java
this.getLogger().info("Hello World");
System.out.println("Hello World");
```

输出的结果是

```
[23:33:33 INFO]: 测试
[23:33:33 INFO]: [Test] 测试
```



## ChatColor
在所有能发彩色文字的地方, 你可以直接使用双s (即`§`符号, Windows系统下按住键盘Alt键, 在数字键盘区域依次按下0167后松开Alt键即可输入该字符) + 对应颜色代码(可以在Minecraft Wiki上查到)代表颜色.  
颜色是可以混用的: `§4比§c如§6这§2样`.

在开发中, 你不必这样, `ChatColor`可以替代.  

```java
p.sendMessage(ChatColor.RED+"你" + ChatColor.GREEN+"好"+ ChatColor.YELLOW + "!");
```

这样就可以发送一个 红色的“你”, 绿色的“好”, 黄色的感叹号 给玩家.  

后面了解配置文件的操作后, 一些插件允许服主在设定一些提示语时用`&`符号代替`§`, 插件处理这样的文本信息时, 可以这样处理成带颜色的字符串:  
```java
String str = "&4哈&c哈&6哈....."; //待处理字符串
p.sendMessage(str); //发给玩家的还是: &4哈&c哈&6哈.....
String str_finish = ChatColor.translateAlternateColorCodes('&',str); //处理好的字符串
p.sendMessage(str_finish); //发给玩家就是彩色的
```


> 提示：你可以使用  
> ```java 
> import static org.bukkit.ChatColor.*;
> ```
> 来导入`ChatColor`中的所有枚举。接下来你就可以更方便地写颜色代码：  
> ```java
> String str = RED + "/test help" + GREY + "    -    " + WHITE + "显示帮助菜单。";
> ```

=======
# 最简单的插件

# Bukkit插件的本质
插件本质是一个基于BukkitAPI的Java应用. 一个插件必须要有 主类 和 `plugin.yml`文件.

例如下面是一个常见插件, 让我们找一下它的主类和`plugin.yml`文件.  

![](pics\1-1-pic1.jpg)

# 简单的插件
在编写自己想做的插件之前, 不妨做一个简单的插件来了解一下Bukkit插件如何编写.    

新建一个Java工程, 导入开服用的服务端jar文件到工程的Libraries中. 创建`tdiant.helloworld.HelloWorld`类作为插件的主类, 并继承`JavaPlugin`类.  
在主类里覆写`onEnable`方法和`onDisable`方法. 完成后, 代码应该类似这样:  

```java
package tdiant.helloworld;  
  
import org.bukkit.plugin.java.JavaPlugin;  
  
public class HelloWorld extends JavaPlugin {
    @Override  
    public void onEnable(){  
        System.out.println("Hello World");
    }  
  
    @Override      
    public void onDisable(){  
        
    }  
}  
```

Bukkit服务端会在插件被启用时调用`onEnable`方法, 被停用时调用`onDisable`方法.

创建`plugin.yml`文件. 打开plugin.yml文件并在其中输入如下信息：  
```yml
name: HelloWorld
main: tdiant.helloworld.HelloWorld
version: 1
author: tdiant
```

> **特别注意: 如果你的插件是基于新版本API(1.13以及以上版本)编写的, 应当在plugin.yml中额外增加`api-version: 1.13`键值对.例如这样:**  
> ```yml
> name: HelloWorld
> main: tdiant.helloworld.HelloWorld
> api-version: 1.13
> version: 1
> author: BakaRinya
> ```
> **这会告诉Bukkit, 这个插件是基于新版API编写的.**  
> 若要兼容1.13及以上版本的同时兼容旧版本, 应特别注意各版本之间的 API 变化(譬如1.12进度系统取代了成就系统, 1.13的 Material 枚举发生了巨大变化).

*注意: 主类的名称并不是固定的, 但是`plugin.yml`文件的名称是固定的.*     

上面的plugin.yml文件逐行分析如下：

| 键 | 意义 | 备注 |
| :-:   | :-:   | :- |
| name | 插件名 | 不允许带有中文和空格, 推荐只含有下划线、英文. |  
| main | 插件的完整主类名 | 例如我这里插件主类为tdiant.helloworld.HelloWorld, 此处则需填写tdiant.helloworld.HelloWorld. |  
| version | 插件版本 | 您可以填写一个合理的String内容, 而不一定必须为数字, 例如可填写v1.0.0 |  
| author | 作者 |  -


可以发现, 当插件Jar被正常加载后, 会在控制台输出`Hello World`字符串, 这标志着我们的HelloWorld插件正常工作.  

![](pics/1-1-pic2.png)

# BukkitAPI中的Logger

## Logger
*这里只是简要提及, 不详细介绍, 只需要知道有这件事即可.*

BukkitAPI“修改”了我们常用的sout (即`System.out.println`), 将其“引入”了BukkitAPI提供的Logger.  
只有通过Logger输出的文本信息才能记录在服务端生成的log文件中.

在BukkitAPI插件开发时, 我们通常不用sout输出想往后台输出给服主看的文本信息, 而应用Logger.  
主类有`getLogger()`方法, 可以利用这个方法获得Logger.  
例如这样:

```java
public class HelloWorld extends JavaPlugin {
    @Override  
    public void onEnable(){  
        this.getLogger().info("Hello World");
    }  
  
    @Override      
    public void onDisable(){}  
}  
```

这样输出信息的方式与sout相比最主要的区别是, 如果你的插件`plugin.yml`里的名称为`Test`那么:

```java
this.getLogger().info("Hello World");
System.out.println("Hello World");
```

输出的结果是

```
[23:33:33 INFO]: 测试
[23:33:33 INFO]: [Test] 测试
```



## ChatColor
在所有能发彩色文字的地方, 你可以直接使用双s (即`§`符号, Windows系统下按住键盘Alt键, 在数字键盘区域依次按下0167后松开Alt键即可输入该字符) + 对应颜色代码(可以在Minecraft Wiki上查到)代表颜色.  
颜色是可以混用的: `§4比§c如§6这§2样`.

在开发中, 你不必这样, `ChatColor`可以替代.  

```java
p.sendMessage(ChatColor.RED+"你" + ChatColor.GREEN+"好"+ ChatColor.YELLOW + "!");
```

这样就可以发送一个 红色的“你”, 绿色的“好”, 黄色的感叹号 给玩家.  

后面了解配置文件的操作后, 一些插件允许服主在设定一些提示语时用`&`符号代替`§`, 插件处理这样的文本信息时, 可以这样处理成带颜色的字符串:  
```java
String str = "&4哈&c哈&6哈....."; //待处理字符串
p.sendMessage(str); //发给玩家的还是: &4哈&c哈&6哈.....
String str_finish = ChatColor.translateAlternateColorCodes('&',str); //处理好的字符串
p.sendMessage(str_finish); //发给玩家就是彩色的
```


> 提示：你可以使用  
> ```java 
> import static org.bukkit.ChatColor.*;
> ```
> 来导入`ChatColor`中的所有枚举。接下来你就可以更方便地写颜色代码：  
> ```java
> String str = RED + "/test help" + GREY + "    -    " + WHITE + "显示帮助菜单。";
> ```

