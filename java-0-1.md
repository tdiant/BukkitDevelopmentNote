# 环境搭建
## 查看电脑系统位数
电脑里的操作系统也有位数之分. 在准备开发环境之前需要认清自己电脑的操作系统位数.  

![](https://miao.su/images/2018/07/31/p15fcf9.png)

## 配置JDK
JDK是Java应用开发工具包. 全称为Java Development Kit.     
为了开发Java应用, 我们需要安装JDK.  
在官方给我们的JDK安装包中, 还会安装Java应用的运行环境, 即JRE.  

首先需要打开JDK下载地址, 用浏览器打开这个链接:  
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

![](https://miao.su/images/2018/07/31/p246c4d.png)

下载完毕后, 右键用管理员模式打开(XP直接双击), 进行安装.  
![](https://miao.su/images/2018/07/31/p4a4d3c.png)

## 配置IDE(Eclipse)
打开下面的链接:
http://www.eclipse.org/downloads/packages/

![](https://miao.su/images/2018/07/31/p3c1c5b.png)

下载完毕后, 双击打开, 解压缩出来.

![](https://miao.su/images/2018/07/31/p5e12c6.png)

解压缩完毕后, 即可双击`eclipse.exe`文件打开Eclipse.  
在接下来的内容中, 将使用Eclipse作为IDE进行开发.

![](https://miao.su/images/2018/07/31/p6c53ce.png)

您还可以参考网络教程将Eclipse配置为中文版. 本文中将继续使用英文版.  

# 准备Bukkit开发
Bukkit开发需要准备开服使用的Jar文件.  

原则上开发哪一MC版本的插件即需要使用哪一版本的Bukkit的Jar文件.  
但事实上, 如果插件代码里使用的BukkitAPI内容是各个Bukkit版本都有的, 理论该插件可以支持所有版本Bukkit.

Bukkit插件可以使用Spigot的开服Jar文件编写出来. 不推荐使用Cauldron等.

如果你需要下载某一版本的开服文件, 可在这里下载:
http://tcpr.ca/
