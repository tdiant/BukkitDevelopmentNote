<p style="font-size:24px;">深入plugin.yml</p>

# 了解plugin.yml
`plugin.yml`文件是Bukkit及其衍生服务端识别插件的重要文件.  

在服务端加载插件时, 服务端加载完毕Jar文件后做的第一件事就是读取该Jar文件的`plugin.yml`文件.  
如果把任一可正常工作的插件的Jar文件用相应的ZIP压缩软件打开, 删除`plugin.yml`文件后再启动服务端, 会抛出错误.  
```  
Could not load 'plugins\[YOUR_PLUGIN].jar' in folder 'plugins'  
org.bukkit.plugin.InvalidDescriptionException: Invalid plugin.yml  
```
可发现, 服务端将会因为没有`plugin.yml`文件而抛出`InvalidDescriptionException`错误.  

<br><br>
在`plugin.yml`文件中, 目前我们已知的有`name`、`version`、`main`、`author`四个项目可以设置.  
事实上, `plugin.yml`文件中还有许多可以设置的项目, 部分项目是本节的内容, 其余可以在spigotmc的官方文档中查阅到.  
> 目前(2018.7.28)BukkitAPI主要由SpigotMC维护, 因此大量的BukkitAPI文档都在 SpigotMC 网站上.  
> 有关plugin.yml文件的官方文档在这里:  
> https://www.spigotmc.org/wiki/plugin-yml/  

# 必要设置项
`plugin.yml`文件中, `name`、`main`、`version`三项必须存在.  
*这也意味着, 前面的实例中, 我们使用的`plugin.yml`文件, 删去`author`键仍可被服务端正常加载.*  

不妨来认识一下这三个设置项.

## name
顾名思义, 它定义了插件的名称.  

对于名称, 官方WIKI中给出了严格的要求, 即只能由 **英文小写或大写字符、阿拉伯数字或下划线** 构成. 决不能出现中文字符、空格等.  
在后续生成插件配置文件夹时, 该项设置的插件名将会是插件配置文件夹的名称.  

起名的时候应该注意, 尽可能起一个“个性”的名称, 防止与其他插件重名.

## version
指插件的版本号.  
该键理论上可以在后面填写任意String内容. 但是官方WIKI要求尽可能使用X.X.X格式的版本号表示(例如: 2.3.3).  
关于版本号规则，可以参考[语义化版本](https://semver.org/lang/zh-CN/)

## main
指插件的主类名.  

在插件中, 主类有且只有一个, 且需要继承`JavaPlugin`类. 主类是插件的“入口”, 这里的`main`即意在说明主类的名称.  
这里需填写主类的全名, 也就是精确到主类所在的具体包. 说白了就是不只是需要把主类名带上, 还要把包名带上.

# 可选设置项
`plugin.yml`文件只需要存在必要设置项的三个键即可.  
下面的键可选, 可有可无. 但有一些在一些特定的情况下必须要有.

## 依赖
有时候你的插件可能需要调用`Vault`（用来获取玩家货币余额）或其他的插件, 即依赖其他插件.   
这时候需要在`plugin.yml`文件中进行设置告知服务端, 从而保证所依赖的插件在本插件之前被加载.   

你可以在`plugin.yml`文件中加入`depend`键或`softdepend`键来控制依赖.  

`depend`键或`softdepend`键接的值必须是数组. 例如这样:
```yml
depend: [Vault, WorldEdit]
softdepend: [Essentials]
```
两个键设置的内容区别如下：  
1. depend: 插件强制要求的依赖. 如果没有这个插件, 该插件将无法正常工作, Bukkit此时会抛出相应错误.  
2. softdepend: 插件不强制要求的插件. 如果服务端内没有这个插件, 插件仍可正常工作.

后面设置的数组内的内容都是所依赖插件的名称, 此处名称应与所依赖的插件的`plugin.yml`文件的`name`键的值相同.

## loadbefore
`depend`与`softdepend`可以实现插件在某个插件之后加载. 但也许有时你的插件可能需要实现在某个插件之前被加载.  
此时你可以使用`loadbefore`设置, 用法类似. 例如:
```yml
loadbefore: [Essentials, WorldEdit]
```

在上面的例子中, 可保证插件在WorldEdit与Essentials插件之前被加载.

## commands
如果你的插件定义了新指令, 你第一步就需要设置该项告知服务端.  
此处仅做示范:
```yml
commands:
  test:
    description: "Hello World!"
```
这可以告知服务端注册了指令`test`, 并且描述为`Hello World!`字符串, 该描述字符串将会在`/help`指令中被显示.  

## author与authors
此处不再赘述其作用.  如果你想表示多名作者, 你可以设置`authors`项, 值需为一个数组.
```yml
authors: [tdiant, Seraph_JACK]
```
如果同时存在`author`与`authors`, 将忽略`author`.

## 
