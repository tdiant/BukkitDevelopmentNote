<p style="font-size:24px;">HelloWorld插件</p>

# 了解Bukkit插件  
Bukkit插件本质是一个基于BukkitAPI的Java应用. 但Bukkit插件有下面三个特征：    
1. 插件成品文件格式为jar.    
2. 插件jar文件根目录须有一名为 `plugin.yml` 且符合规范的文件.    
3. 插件内须有且仅有一个类继承 `org.bukkit.plugin.java.JavaPlugin` 类, 这个类将作为插件的主类.    
  
# 感受HelloWorld插件  
不妨做一个HelloWorld插件来感受一下.    

新建一个Java工程, 创建`tdiant.helloworld.HelloWorld`类作为插件的主类并继承`JavaPlugin`类, 创建`plugin.yml`文件.     
主类的名称并不是固定的, 但是plugin.yml文件的名称是固定的.     
  
在主类里覆写`onEnable`方法和`onDisable`方法. 完成后, 代码应该类似这样：  

```
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
  
打开plugin.yml文件并在其中输入如下信息：  
```
name: HelloWorld  
main: tdiant.helloworld.HelloWorld  
version: 1  
author: tdiant  
```
  
上面的plugin.yml文件逐行分析如下：

| 键 | 意义 | 备注 |
| -----   | -----   | 
| name | 插件名 | 可带空格, 不允许带有中文和空格, 推荐只含有下划线、英文. |  
| main | 插件的完整主类名 | 例如我这里插件主类为tdiant.helloworld.HelloWorld, 此处则需填写tdiant.helloworld.HelloWorld. |  
| version | 插件版本 | 您可以填写一个合理的String内容, 而不一定必须为数字, 例如可填写no_1. |  
| author | 作者 |  
  
在`onEnable`方法中添加相应的`sout("Hello World")` 语句.  
可以发现, 当插件Jar被正常生成后, 会在控制台输出`Hello World`字符串, 这标志着我们的HelloWorld插件正常工作.  