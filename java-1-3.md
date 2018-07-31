# Java中的基本数据类型    
Java中有三种基本数据类型, 分别为“数字型”、“字符型”和布尔值.

数字型分为整数类型和浮点类型.  
整数类型分别为byte、short、int和long, 浮点类型分别为float和double.

布尔值指的是boolean, 字符型指char. 

# 变量与基本操作    
## 使用变量    
在编写Java程序时, 通常需要定义变量. 变量可以临时存储数据信息.     
```    
// Demo.java 文件    
public class Demo {    
   public static void main(String[] args){    
      int x=666;    
      System.out.println(x);    
   }    
}    
```    
程序运行后, 会在控制台输出信息如下：    
```    
666    
```    
在程序的第三行, 我们定义了一个int类型的变量, 名字叫做x, 初始值为666. 既然是变量, 那么他可以在程序运行时, 在变量的有效范围内, 随时更改它的数值.     
    
```    
// Demo2.java 文件    
public class Demo2 {    
   public static void main(String[] args){    
      int x=666;    
      System.out.println(x);    
      x = 777;    
      System.out.println(x);    
   }    
}    
```    
    
程序会在控制台中输出如下信息：    
```    
666    
777    
```    
    
“=”符号是赋值符号, 如果想把语句x = 777;写为777 = x;是错误的, 因为“=”符号与数学上的“=”不一样, 赋值符号左面放被赋值的变量名, 右面放需要赋的值.     
两个变量之间也可以相互赋值, 例如这样：    
    
```    
int x = 1, y = 2;    
x = y;    
System.out.println(x);    
```    
    
程序运行后, 在控制台中输出如下信息：    
```    
2    
```    
    
long类型的变量可以存储的最大数字要比int类型更大.     
Long.MAX_VAULE代表Long类型可以存储的最大数字, Integer.MAX_VAULE代表int类型可以存储的最大数字. 这也就是说, 在数学上, Long.MAX_VAULE代表的数字要比Integer.MAX_VAULE要大.     
long类型的变量与int类型的之间相互赋值, 如果试图把long类型变量内的值赋到int类型的变量当中, 会出现问题.     
```    
long a = 2;    
int b = 3;    
b = a;    
System.out.println(b);    
```    
    
程序会出现问题, 导致程序无法编译.     
```    
long cannot be cast to int.    
```    
    
## 隐性转换    
```    
long a = 2;    
int b = 3;    
b = a;    
System.out.println(b);    
```    
正如我们刚才所见, 这个程序在刚才的运行中, 出现了问题.     
那如果我们想要将int类型变量内的值赋值到long类型的变量当中呢？    
```    
long a = 2;    
int b = 3;    
a = b;    
System.out.println(a);    
```    
程序运行后, 会在控制台输出信息如下：    
```    
3    
```    
这说明这样做是可行的.     
实质上, 在这个过程当中进行了隐性转换. 如上面的程序, 变量b内的数据是int类型的3, 这个数据在赋值入变量a当中时, 会被隐形转换为long类型的3, 然后被赋值入long类型的a.     
隐性转换的规则可以理解为“从低等级向高等级”. 我们可以假想为long类型等级比int类型高. 如果这样假想, 那么常见的四个整数类型可以按照这样的方式从低到高排序：    
byte < short < int < long    
