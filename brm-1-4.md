# 认识配置文件

## 配置文件
配置文件用来储存配置信息, 以便使用文件开关功能、储存数据、修改信息.  
我们往往需要读写配置文件. Bukkit为我们提供了配置API.   

## 配置API
配置API是BukkitAPI提供的读写配置文件的工具. 其相对而言较为简单, 是插件开发中常用的API.  

目前为止, 配置API只有YAML配置功能可用. 这也是大多数插件为什么配置文件是YAML文件的原因.  
在本文中, 我们也将使用YAML配置API.   

*现在的配置API的类均在 `org.bukkit.configuration` 和 `org.bukkit.configuration.file` 包中. 在早期版本(MC 1.1以及之前), 这些东西都在`org.bukkit.util.configuration`包, 而并非上面的这两个包.*   

# 了解YAML文件

相信开服的经验已经使你对YAML文件有了初步认识.  
YAML文件的文件后缀是`.yml`. 其配置文件结构需要严格遵守YAML标准.  

下面即是一个符合标准的YAML配置文件的内容:  
~~~
a: 1
b: "abc"
c: abc
d:
  e: true
  f: 666.233
  g:
  - '2333'
  - '998'
~~~

在本节中, 我们暂不学习如何使用配置API读写配置文件. 首先对YAML配置文件做简单了解.    

第一行 `a: 1` 代表`a`键(Key)对应的值(Value)为`1`.  
在读写YAML配置文件时, 需要知道键, 由键获取这个键对应的数据.  

在d键中, 存在e、f、g三个子键, 相信你可以**根据空格看出来谁是谁的子键**, 其对应的名称分别为`d.e`、`d.f`、`d.g`.  
也就是说, 我们想获取那个现在值为`true`的键对应的数据, 应该获取键`d.e`的数据.

子键也可以有其子键, 例如:
```yml
a:
  b:
    c:
	  d:
	    e:
		  f:
		    g: 233
```
那么`a.b.c.d.e.f.g`键的值为`233`.  

回到第一份YAML配置内容中.  
`a`键对应的值是`1`, `1`可以代表是一个数字, 也可以代表是一个字符串.  
这意味着, 字符串在一些情况下可以不必带有引号.  
*那我在实际使用配置API的时候获取到的`a`键值究竟是什么类型? 具体会在后面提及.*  

`d.e`键对应的值是一个boolean类型数据.  
`d.g`键对应的是一个StringList类型数据.

YAML中字符串可以用双引号表示, 也可以使用单引号表示, 但是不能一半是双引号, 一半是单引号.  
YAML中注释使用#, 类似Java中的//, 请看下面的实例:
```yml
#我是注释
a: 'abc' #这是一个字符串abc
b: "def" #用双引号也可以
c: 'hji" #这样不可以
d: '""' #这是字符串, 内容是一对英文双引号
e: "''" #这是字符串, 内容是一对英文单引号
```

# 感受配置API的使用
在后面的内容中, 将详细叙述配置API的使用方式. 在这里, 通过一个实例来感受配置API的使用方式.

首先我们需要创建`config.yml`文件. 默认的`config.yml`文件要与`plugin.yml`文件处于同一目录下. 在这里我们在默认`config.yml`文件中存入这些信息:  
```yml
a: 1
```
完成后, 我们需在`onEnable`方法中插入这样的语句:
```java
saveDefaultConfig(); //我建议这个东西写在主类onEnable方法开头那里或者onLoad方法里
System.out.println("Hello! Test Number is "+getConfig().getInt("a")); //输出文件
```  
插件加载后, 可以发现控制台输出:
```
Hello! Test Number is 1
```

在plugins文件夹中生成了以插件名为名的文件夹, 打开该文件夹里的config.yml文件, 并将其中的`a`键改为其他整数后使用reload指令, 可以显示该数值.

# 基本用法(操作config.yml)
## 默认配置文件
每个插件都可以有一个`config.yml`文件作为其配置文件.  
我们首先需要准备一份默认配置文件, 也就是插件放进`plugins`文件夹后第一次被加载自动生成的配置文件.  

首先我们需要创建`config.yml`文件. 默认的`config.yml`文件要与`plugin.yml`文件处于同一目录下. 在这里我们在默认`config.yml`文件中存入这些信息:  
```yml
a: 1
b: "abc"
c: abc
d:
  e: true
  f: 666.233
  g:
  - '2333'
  - '998'
```
完成后, 我们需在`onEnable`方法中插入这样的语句:
```java
saveDefaultConfig(); //我建议这个东西写在主类onEnable方法开头那里或者onLoad方法里
```
该语句需要保证在读写配置文件之前被执行.  
它可以自动判断插件配置文件夹中是否存在`config.yml`文件, 在没有的时候将该插件jar文件的根目录中`config.yml`保存至插件配置文件夹.

## 写入配置文件
让我们尝试写入配置文件, 在`onEnable`方法被调用时把配置文件中键`h`改为字符串`baka`.  
我们可以这样做:
```java
public void onEnable(){
    this.saveDefaultConfig();
	this.getConfig().set("h","baka");
	this.saveConfig();
}
```
执行后可发现, 插件配置文件夹中`config.yml`文件的`h`键内容已经成为`baka`.  
`set`以后要记得`saveConfig`!

## 读取配置文件中数据
我们可以使用`get`来读取数据.
```java
public void onEnable(){
    this.saveDefaultConfig();
	System.out.println(getConfig().get("a"));  //这里的get方法返回Object类型数据
}
```
后台将`a`键的值, 也就是将会输出`1`.

对于不同的数据, 配置API给出了不同的get方法. 例如 `getString`、`getBoolean`、`getDouble`、`getInt`等.  

还记得前面的那个"类型问题"吗? `a`键的值究竟是什么数据类型呢?   
答案已经揭晓, 取决于你使用的是哪个get方法. 如果使用`getString`获取`a`键的值, 那么获取到的将是`String`类型, 如果是`getInt`, 那么就是`int`类型, 如果是`getDouble`, 那么就是`double`类型......

对于`d.g`键, 其内容这样获取:
```java
List<String> list = getConfig().getStringList("d.g");
```

## 在其他类中操作配置文件
我们已经知道如何注册监听器. 那么我们免不了遇到在其他类中操作配置文件的情况.  
然而你会发现, `getConfig`方法并不是static(静态)的. 我们不能直接在其他类操作配置文件.    

如果你有一些Java开发常识, 此时你可能意识到了, 我们需要做一个静态的主类实例才行.  
在这里赘述一种我自己喜欢用的方式. 这是一个经过处理的主类, 有两处需要注意:  
```java
public class HelloWorld extends JavaPlugin{
    private static HelloWorld INSTANCE;
	
	public void onEnable(){
	    INSTANCE = this; //这个推荐放在最最最开头
		......

    ......
	
	public static HelloWorld getInstance(){
	    return INSTANCE;
	}
}
```

然后你就可以在其他类中这样操作配置文件:
```java
HelloWorld.getInstance().getConfig().你要做的各种操作
HelloWorld.getInstance().saveConfig(); //写入配置文件内容了以后记得保存!
```
# 操作自定义的配置文件
关于非`config.yml`的YAML文件的操作, 有很多种方式可以做到.  
下文叙述的是其中的一种.

## 默认配置文件
我们还是需要像`config.yml`那样准备一份默认配置文件, 放在与plugin.yml相同目录下. 不同的是, 除了`saveDefaultConfig`以外, 我们还需要其他的代码来保存默认配置文件.  

例如我们有`config.yml`和`biu.yml`两个配置文件, 插件加载时应该这样生成默认配置文件:  
```java
this.saveDefaultConfig(); //生成默认config.yml
this.saveResource("biu.yml", false); //生成默认biu.yml
```
*`saveResource`方法的第一个参数是文件名, 第二个参数是是否覆盖, 设置成false可以达到saveDefaultConfig的效果.*  

同理,利用`saveResource`可以生成你想生成的默认的非`config.yml`的配置文件.

如果我想实现在插件配置文件夹创建一个新的文件夹存放配置文件怎么做呢? 很简单:  
```
this.saveResource("test\biu.yml", false); //生成默认biu.yml, 放在test文件夹里, Jar文件中也需要有test文件夹
```

## 基本读写与保存
下面是一个读写与保存的示例:
```java
//读取
//this.getDataFolder()方法返回插件配置文件夹的File对象
File biuConfigFile = new File(this.getDataFolder(), "biu.yml");
// 对于在插件配置文件夹创建一个新的文件夹存放配置文件
// File biuConfigFile = new File(this.getDataFolder(), "test/biu.yml");
FileConfiguration biuConfig = YamlConfiguration.loadConfiguration(biuConfigFile);
biuConfigFile.get.......
biuConfigFile.set....... //set完了记得保存!
//保存
biuConfig.save(biuConfigFile);
```
