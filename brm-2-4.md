<p style="font-size:24px;">深入配置API</p>
# 基本用法(操作config.yml)
## 默认配置文件
每个插件都可以有一个`config.yml`文件作为其配置文件.  
我们首先需要准备一份默认配置文件, 也就是插件放进`plugins`文件夹后第一次被加载自动生成的配置文件.  

首先我们需要创建`config.yml`文件. 默认的`config.yml`文件要与`plugin.yml`文件处于同一目录下. 在这里我们在默认`config.yml`文件中存入这些信息:  
```
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
```
saveDefaultConfig(); //我建议这个东西写在主类onEnable方法开头那里或者onLoad方法里
```
该语句需要保证在读写配置文件之前被执行.  
它可以自动判断插件配置文件夹中是否存在`config.yml`文件, 在没有的时候将该插件jar文件的根目录中`config.yml`保存至插件配置文件夹.

## 写入配置文件
让我们尝试写入配置文件, 在`onEnable`方法被调用时把配置文件中键`h`改为字符串`baka`.  
我们可以这样做:
```
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
```
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
```
List<String> list = getConfig().getStringList("d.g");
```

## 在其他类中操作配置文件
我们已经知道如何注册监听器. 那么我们免不了遇到在其他类中操作配置文件的情况.  
然而你会发现, `getConfig`方法并不是static(静态)的. 我们不能直接在其他类操作配置文件.    

如果你有一些Java开发常识, 此时你可能意识到了, 我们需要做一个静态的主类实例才行.  
在这里赘述一种我自己喜欢用的方式. 这是一个经过处理的主类, 有两处需要注意:  
```
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
```
HelloWorld.getInstance().getConfig().你要做的各种操作
HelloWorld.getInstance().saveConfig(); //写入配置文件内容了以后记得保存!
```
# 操作自定义的配置文件
关于非`config.yml`的YAML文件的操作, 有很多种方式可以做到.  
下文叙述的是其中的一种.

## 默认配置文件
我们还是需要像`config.yml`那样准备一份默认配置文件, 放在与plugin.yml相同目录下. 不同的是, 除了`saveDefaultConfig`以外, 我们还需要其他的代码来保存默认配置文件.  

例如我们有`config.yml`和`biu.yml`两个配置文件, 插件加载时应该这样生成默认配置文件:  
```
this.saveDefaultConfig(); //生成默认config.yml
this.saveResource("biu.yml", false); //生成默认biu.yml
```
*`saveResource`方法的第一个参数是文件名, 第二个参数是是否覆盖, 设置成false可以达到saveDefaultConfig的效果.*  

同理,利用`saveResource`可以生成你想生成的默认的非`config.yml`的配置文件.

## 基本读写与保存
下面是一个读写与保存的示例:
```
//读取
//this.getDataFolder()方法返回插件配置文件夹的File对象
File biuConfigFile = new File(this.getDataFolder(), "biu.yml");
FileConfiguration biuConfig = YamlConfiguration.loadConfiguration(biuConfigFile);
biuConfigFile.get.......
biuConfigFile.set....... //set完了记得保存!
//保存
biuConfig.save(biuConfigFile);
```

# 序列化
## 了解序列化
如果我自己做了一个类型, 例如下面的`BakaRua`类:
```
public class BakaRua{
    public String name;
	public String str;

    public BakaRua(String name, String str){
	    this.name = name;
		this.str = str;
	}
}
```
现在我们新建一个BakaRua对象:  
`BakaRua test = new BakaRua("tdiant", "hello!!");`  
我们想把test保存在配置文件里怎么办?  
很遗憾,`getConfig().set("demo",test);`是行不通的.

> 哪些东西可以直接set保存呢?
> 类似getInt, 所有拥有get方法的类型都可以直接保存. (包括List<String>)
>   
> 还有一些BukkitAPI给的类型, 例如ItemStack. 但不是全部都是这样.  
> 如果你想判断一个类型是不是可以直接set, 你可以在JavaDoc中找到它, 看它是否实现了ConfigurationSerializable类.

你可能想到了最简单粗暴的办法:
```
//这样set
getConfig().set("demo.name",test.name);
getConfig().set("demo.str",test.str);
//然后保存, 用的时候这样
getConfig().getString("demo.name");
getConfig().getString("demo.str");
```

这的确是一种切实可行的办法. 但是这真的是太麻烦了. 有没有一种方法直接set test这个对象, 直接get就得到这个对象的办法呢? 有! 你可以使用序列化和反序列化实现它!

## 让自定义类型实现序列化与反序列化
以上文`BiuRua`为例. 首先让他实现`ConfigurationSerializable`, 并添加`deserialize`方法. 如下:
```
public class BakaRua implements ConfigurationSerializable {
    public String name;
	public String str;

    public BakaRua(String name, String str){
	    this.name = name;
		this.str = str;
	}
	
	@Override
	public Map<String,Object> serialize() {
	    Map<String,Object> map = new HashMap<String,Object>();
		return map;
	}
	
	public static BakaRua deserialize(Map<String,Object> map){
	    
	}
}
```
然后继续完善`serialize`, 实现序列化. 我们只需要把需要保存的数据写入map当中即可.  
注意, 需要保存的数据要保证可以直接set, 不能则也需要为他实现序列化与反序列化.  
```
	@Override
	public Map<String,Object> serialize() {
	    Map<String,Object> map = new HashMap<String,Object>();
		map.put("name",name);
		map.put("str",str);
	    return map;
	}
```
序列化后, 数据即可直接set进配置文件里. 为了实现直接get的目的, 还需要进行反序列化.  
```
	public static BakaRua deserialize(Map<String,Object> map){
	    return new BakaRua(
		    (map.get("name")!=null?(String)map.get("name"):null),
			(map.get("str")!=null?(String)map.get("str"):null)
		);
	}
```
编写完毕后, 我们需要像注册监听器一样, 注册序列化. 在`onEnable`中加入如下语句:
```
ConfigurationSerialization.registerClass(BiuRua.class);
```
至此, 你就可以自由地对一个自定义的对象直接地get和set了!
