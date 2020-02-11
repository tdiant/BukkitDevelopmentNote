# 粒子效果及音效播放

# 几何初步

几何基础知识是做特效的基础内容，应当了解。  
**作者高中数学没及过格，这里内容仅供参考。**  
**若您了解相关内容或接受了高中数学的有关学习，您可以跳过这部分内容。**  
*不建议跳过这部分内容。*  

限于篇幅，若需要查看请点击下方链接:  
[几何初步](brm-2-7-math.md)


# 粒子效果

客户端正常配置时，若对草方块上使用骨粉，草方块上会长出草丛，同时还会生成绿色的颗粒动画. 这样的动画效果就是Minecraft中的粒子效果.  

## 播放粒子效果
如果想在某一个`Location`对象所对应的位置播放粒子效果，对于不同的Minecraft版本有不同的方案：  

### PlayEffect
可以利用World类的`PlayEffect`方法:  
*对于Effect，BukkitAPI在后续的更改中，其中的枚举几乎都或多或少有些许改动。开发时应小心。*  

```java
Location loc = 某一Location对象;
loc.getWorld.playEffect(loc, Effect.HAPPY_VILLAGER, 1); //播放的是绿色的闪光星星⭐效果
```

`PlayEffect`方法在较早的BukkitAPI版本中即被加入. 在使用这一方法时需要与`Effect`打交道.  
`Effect`是效果枚举. 值得注意的是，这其中既包含动效(Effect.Type.VISUAL)，也包含声效（Effect.Type.SOUND）.  

***作为一个老旧的API，在实际开发当中，这一方法并不常用. 其中的常见枚举（例如这里使用的HAPPY_VILLAGER）在新的API中被标记废除.***

### spawnParticle
在新版的API中加入了`spawnParticle`方法. 目前开发插件常用这一方法来播放粒子效果.  

新版的BukkitAPI有意将`Sound`与`Visual`这两个概念分隔开，对于粒子效果，在使用`spawnParticle`方法时，取`Effect`而代之的是`Particle`枚举.

*spawnParticle的用法较多，在此略去大篇幅对各个方法与参数的介绍，可以查阅JavaDoc，其中有十分简单易懂的注释.*  
*BukkitAPI后续更新中，枚举或多或少都有变动，应当注意！*

## 播放所需的形状

> 开发实例: 在玩家脚底播放一圈半径为1的粒子效果

**分析**  
1. 几何角度考虑  

以玩家脚底处为原点，建立平面直角坐标系. 如下图所示:  
![](https://i.loli.net/2019/07/12/5d2893acab56b12879.jpg)

*绿色部分为粒子效果*

由圆的定义知，所绘制的粒子为到原点的点集.

2. 实现
播放想要的形状就是逐次的在所需播放的坐标处播放粒子效果.

*这里将不解释什么是弧度制，而是做强制要求，只要算角度都必须用这样的方法变为弧度制，有兴趣可以在网上查阅*  

```java
Location loc = p.getLocation().clone();
for(int t=0;t<360;t++){ //这里的t表示旋转角，从0到360度遍历一遍就是转了一圈
    double r = Math.toRadians(t); //角度制变弧度制
    //在这里，我们使用三角函数依次计算出了对应点的坐标.
    //建议作图体会这样计算的原理.
    double x = Math.cos(r);
    double y = Math.sin(r);
    //在刚开始时，loc是坐标系原点（也就是玩家所在的位置）
    //这里我们的add将其变为了我们想要播放粒子的坐标位置
    //后面我们又subtract（减）将其又变为了坐标原点
    loc.add(x,0,y);
    loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,loc,1,null);
    loc.subtract(x, 0, y);
}
```

这样我们就完成了这一效果.

依此，可以大致概括出实现粒子效果的基本步骤:  
1. 分析: 从数学角度分析, 思考怎么才能获得所需形状中所有的点；从代码角度分析，思考怎样才能依此获得这些点的坐标值
2. 实现：利用恰当的方法播放粒子效果

# 音效播放
由于`Effect`既包含动效，也包含声效，这意味着使用与上面`PlayerEffect`方法一样的方法，我们也可以播放音效.

在新API中提供了`playSound`方法并且加入了`Sound`枚举. 目前常用这一方法. 这一方法是World也同样是Player类的方法, 具体使用哪一方法，取决于你希望对谁播放.

*BukkitAPI后续更新中，枚举或多或少都有变动，应当注意！*
