# 认识HelloWorld  
```  
// HelloWorldApp.java 文件  
public class HelloWorldApp {  
   public static void main (String[] args){  
      System.out.println("Hello World!");  
   }  
}  
```  
将上面的程序编译, 在控制台中运行. 在控制台中, 程序将输出：  
```  
Hello World!  
```  
  
让我们一行行来看我们写的“Hello World”吧！  
  
①第一行创建了一个名为“HelloWorldApp的类”.   
其中public与class被称为Java的关键字, class是用于创建类的关键字.   
②第二行定义了main方法.   
这是程序的入口, 程序从这里开始运行. 不管怎样, 定义main方法必须这么写.   
③第三行是一条输出语句, 可以在控制台输出信息.   

**在Java中, 标点符号需用英文, 且严格区分大小写.**   

# 括号互相匹配  
仔细观察, 我们还可以看到, Java代码中的括号其实互相匹配.   

例如, 第一行的大括号和最后一行匹配, 第二行的大括号也对应倒数第二行.   
在Java中, 大括号定义了一个代码块. 其实, Java中小括号、中括号、双引号也是相互对应的, 比如第二行中的中括号, 例如第三行中的双引号.   

# Java中的名称及关键字  
`HelloWorldApp`是我们自己取的, 他是我们定义的类的名字, 也就是“类名”.   

当然, 你可以把类名改成你想要的!  
但是, 有两点需要注意:
1. 在给`HelloWorldApp`类更换名称时, 文件名也得改成 新类名.java. 后面你会知道原因.  
2. 需要遵守命名规范.   

Java中所有的取名都有一些规范, 有一些必须遵守, 有一些不遵守程序一样可以运行, 但是**这是程序员心中极高尚的信仰, 信仰的光辉引领着我们要去遵守**. 具体的命名规则如下:  
1. 大小写: 类名首字母大写、方法名首字母小写, 变量常量等首字母小写, 包名所有字母都小写.   
2. 仅允许使用字母、数字、下划线, 您还可以使用美元符号 ($), 但无论怎么取名, 第一个字符绝不能是数字. 其实中文字符是可以的, 但是不符合命名规范.   
3. 不允许与Java关键字重复, 例如“public”、“class”.   
4. Java严格区分大小写, 因此“Good”和“good”不是一个名字.   

Java中的关键字共50个, 分别如下：  

| 首字母        |
| --------   | -----   |
|A | abstract | assert |
|B | **boolean** | **break** | **byte*  |
|C | **case** | **catch** | char | **class** | const | **continue**  |
|D | **default** | do | **double**  |
|E | **else** | **enum** | **extends**  |
|F | **final** | **finally** | float | **for**  |
|G | goto  |
|I | **if** | **implements** | **import** | **instanceof** | **int** | **interface**  |
|L | **long**  |
|N | native | **new**  |
|P | **package** | **private** | protected | **public**  |
|R | **return**  |
|S | strictfp | **short** | **static** | **super** | **switch** | synchronized  |
|T | **this** | throw | throws | transient | **try**  |
|V | **void** | volatile  |
|W | **while** | 

*表中收录的 goto 和 const 是Java中的保留关键字. 这些关键字没有任何用途且无法使用, 但是它们仍是关键字. *  
*若您学习了其他编程语言, 想要使用const, 请使用final代替. Java中没有goto的概念. *  