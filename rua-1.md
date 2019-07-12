# Conversation API

# 了解 Conversation API

在实际开发插件时，插件可能会在聊天区与玩家进行“对话”.  

例如在`QuickShop`插件里，玩家手中拿着需要出售的物品，用左键击打箱子，在聊天区域就会提示玩家“请输入出售价格”，玩家只需要在聊天区域直接发送一个数字 `1` ，就可以被`QuickShop`插件识别为玩家创建了一个售价`1`的出售箱.

这一功能乍一听，你可能有这样的思路:  
1. 分析：玩家在聊天区域直接发送1，这没有借助指令等，应当考虑使用监听**PlayerChatEvent**的方式实现.  
2. 实现：监听`PlayerChatEvent`.
3. 做一个**HashMap<Player,Boolean>**用来存储玩家**是否在输入出售价格的状态下**，如果是，那么就截获玩家输入的数字，从而进行相应处理.
4. 再去监听其他事件，用来判断玩家是否击打方块，如果击打，记录玩家手中物品，并且把玩家设置为**出售价格输入状态**.

在这里我们提到了**出售价格输入状态**. 如果QuickShop确实是这样实现的这一功能，那么在玩家聊天区域内，应当有这样的系统与玩家之间的聊天记录:  

```
请在聊天区域内输入商品定价
1
箱子商店创建成功！
```

显然，插件与玩家发生了一次“对话”. 我们为这个对话做了一个“出售价格输入状态”的HashMap.  
实际上，BukkitAPI中提供了现成的API，我们根本就不需要去做这个HashMap，而是可以直接利用这一API轻而易举完成这个“对话”.

# 认识Conversation API
在JavaDoc上，查看`org.bukkit.conversations`包. 在这个包内，有如下关键部分：

**1. Conversable接口**  
如果需要让系统与玩家进行“对话”，那么你需要验证`Player`类是否实现了`Conversable`接口. 只有实现了这一接口才可以使用这一API.  

目前，BukkitAPI中只有`ConsoleCommandSender`与`Player`实现了这一接口.

**2. Prompt接口**  
我们可以认为每一个“对话”就是一个对象. 这一对象的类需要实现Prompt接口.  

BukkitAPI中已经提供了一个`ValidatingPrompt`抽象类. 在下面的示范中，我们将使用这一抽象类创建一个对话类，从而创建对话对象，实现对话功能.  
BukkitAPI不只提供了这一种抽象Prompt类，你可以根据自己的需要选择合适的抽象类，或者自己编写所需的类.

# 使用Conversation API

> 开发实例：实现一个简单的玩家对话  
> 对话内容大致如下：  
> 
> 系统：欢迎你！请问你最喜欢的数字是多少？  
> *如果玩家输入的是数字*  
> 玩家：【输入一个数字】  
> 系统：我也喜欢数字【玩家输入的数字】！祝你愉快！  
> *如果玩家输入的是其他内容*  
> 玩家：【其他内容】  
> 系统：看来是不愿意说呢。祝你愉快！  

## 创建对话类

使用`ValidatingPrompt`创建对话类.

```java
private class NumberAskingPrompt extends ValidatingPrompt {
    public String getPromptText(ConversationContext context) { //返回在对话开始时向玩家发送的“开场白”
        return "欢迎你！请问你最喜欢的数字是多少";
    }

    protected boolean isInputValid(ConversationContext context, String input){ //判断输入是否正确. 这里我们认为所有输入都正确
        return true;
    }

    protected Prompt acceptValidatedInput(ConversationContext context, String input) {//处理玩家输入
        //判断是否为数字
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        if(pattern.matcher(input).matches()){
            context.getForWhom().sendRawMessage("我也喜欢数字"+input+"！祝你愉快！");
        } else {
            context.getForWhom().sendRawMessage("看来是不愿意说呢。祝你愉快！");
        }

        return Prompt.END_OF_CONVERSATION; //结束该对话
    }
}

```

使用`ConversationFactory`构造对话对象（Conversation）并开始对话.

```java
ConversationFactory cf = new ConversationFactory(MyPlugin.plugin); //这里需要的是插件JavaPlugin对象
cf.withFirstPrompt(new NumberAskingPrompt());

//下面的代码可以放在PlayerJoinEvent的监听代码中：
Player p = 玩家对象;
Conversation c = cf.buildConversation(p); 
c.begin(); //开始对话
```

Conversation API还有其他更多更有意思的玩法，可以在JavaDoc中查看！
