# 了解IDE
如果你购买了其他Java书籍, 你可能会认识到可以在命令行用`java`和`javac`指令制作Java应用.  

事实上这是一个可行的方法, 你完全可以借助记事本和这两个指令制作出任何Java应用.  
但这是不可行的, 因为这样你写错了代码没有提示, 并且难以调试出BUG, 编写大型项目你更是会吃大亏.

IDE为我们提供了方便的Java开发工具等, 我们可以在IDE里轻松完成开发.

# 使用Eclipse
*下面的内容如果有不明白的地方可以先照做, 在后面的内容中找到答案.*

打开Eclipse.

![](https://miao.su/images/2018/07/31/p9a9505.png)

让我们创建一个类!

![](https://miao.su/images/2018/07/31/p10b35dd.png)

双击HelloWorldApp.java后, 在输入区域内编写代码即可.

![](https://miao.su/images/2018/07/31/p11f0537.png)

在上面的图中可以发现, 在IDE中写代码, IDE会给我们自动提示, 可以起到很好的引导作用.

```
public class HelloWorldApp {
	public static void main(String[] args) {
		System.out.println("Hello World!");
	}
}
```

编写完毕后, 保存(**记住保存的快捷键是Ctrl+S, 非常常用**)后即可运行.

![](https://miao.su/images/2018/07/31/p12c4891.png)

点击这个按钮后, 即可发现程序输出字符串`Hello World!`

![](https://miao.su/images/2018/07/31/p136e0ba.png)
