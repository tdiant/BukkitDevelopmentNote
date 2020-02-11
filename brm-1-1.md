# 最简单的插件

# Bukkit插件的本质
Bukkit插件本质是一个基于BukkitAPI的Java应用.  

Bukkit插件有下面三个特征：  
1. 插件成品文件格式为jar.    
2. 插件jar文件根目录须有一名为 `plugin.yml` 且符合规范的文件.    
3. 插件内须有且仅有一个类继承 `org.bukkit.plugin.java.JavaPlugin` 类, 这个类将作为插件的主类.    

# 简单的插件
在编写自己想做的插件之前, 不妨做一个简单的插件来了解一下Bukkit插件如何编写.    

一个最简单的插件大致应分为两步编写:  
1. 编写主类  
2. 编写plugin.yml文件  

无论是哪个插件, 这两步都是一开始应该去做的, 缺一不可.  

新建一个Java工程, 创建`tdiant.helloworld.HelloWorld`类作为插件的主类, 并继承`JavaPlugin`类.  
在主类里覆写`onEnable`方法和`onDisable`方法. 完成后, 代码应该类似这样:  

```java
package tdiant.helloworld;  
  
import org.bukkit.plugin.java.JavaPlugin;  
  
public class HelloWorld extends JavaPlugin {  
    // 关于onEnable与onDisable方法的作用会在下一节具体介绍
    @Override  
    public void onEnable(){  
        //your code here.  
    }  
  
    @Override      
    public void onDisable(){  
        //your code here.  
    }  
}  
```

创建`plugin.yml`文件. 打开plugin.yml文件并在其中输入如下信息：  
```yml
name: HelloWorld
main: tdiant.helloworld.HelloWorld
version: 1
author: tdiant
```

**特别注意: 如果你的插件是基于新版本API(1.13以及以上版本)编写的, 应当在plugin.yml中额外增加`api-version: 1.13`键值对.例如这样:**  
```yml
name: HelloWorld
main: tdiant.helloworld.HelloWorld
api-version: 1.13
version: 1
author: BakaRinya
```

**这会告诉Bukkit, 这个插件是基于新版API编写的.**  
我个人不推荐您编写的插件同时兼容全版本Bukkit, 请把1.13版本以及以上版本与1.13版本以下版本两种API的插件分开编写, 以防出现BUG.

*注意: 主类的名称并不是固定的, 但是`plugin.yml`文件的名称是固定的.*     

*对于创建plugin.yml, 如果你 不知道 或者 没有使用Maven或Gradle, 请在src文件夹下直接创建该文件即可, 就像创建一个类一样! 例如在Eclipse中应这样:*  
![](https://miao.su/images/2018/08/09/QQ201808091433546bda6.png)  

上面的plugin.yml文件逐行分析如下：

| 键 | 意义 | 备注 |
| :-:   | :-:   | :- |
| name | 插件名 | 不允许带有中文和空格, 推荐只含有下划线、英文. |  
| main | 插件的完整主类名 | 例如我这里插件主类为tdiant.helloworld.HelloWorld, 此处则需填写tdiant.helloworld.HelloWorld. |  
| version | 插件版本 | 您可以填写一个合理的String内容, 而不一定必须为数字, 例如可填写no_1. |  
| author | 作者 |  
  
在`onEnable`方法中添加相应的`System.out.println("Hello World")` 语句.  
可以发现, 当插件Jar被正常生成后, 会在控制台输出`Hello World`字符串, 这标志着我们的HelloWorld插件正常工作.  

Bukkit服务端会在插件被加载时调用`onEnable`方法, 被卸载时调用`onDisable`方法.