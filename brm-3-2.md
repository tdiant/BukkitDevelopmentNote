# VaultAPI

# Vault插件
服务器里经常有服主会安装经济插件，实现/money功能. 此功能`Essentials`插件可以提供，还可以通过下载其他插件`CraftConomy`、`iConomy`等等实现这一功能.  

如果你想写个插件，给玩家加钱或者扣钱应该怎么办？  
最简单的想法就是看看你所用的经济插件有没有API，通过你所用的经济插件作者给的API，调用API实现.  
但如果你想做一个插件，兼容大多数的经济插件应该怎么办？难不成需要对所有经济插件的API一个个手动做兼容？那是不可能的。  

但是大多数的经济插件都对Vault插件做了支持！  
Vault插件本身无作用，但是它可以为开发者提供经济API，你可以通过Vault实现对几乎所有经济插件的兼容。  

*Vault插件除了有经济API以外，还有有关权限管理的API和玩家聊天相关的API（主要是设置聊天前缀后缀），但实际开发中不常使用，感兴趣可以进行了解，本章将不赘述。*

> Vault的JavaDoc(非官方):  
> https://pluginwiki.github.io/VaultAPI/  

# 经济API的使用

## 准备工作

首先在`plugin.yml`中将`vault`加入`depend`. 在主类中定义这样的属性:  
```java
public Economy economy = null;
```

onEnable方法内前部加入这样的代码检查:  
```java
        //初始化Vault Economy API
        //可以根据实际需要进行修改
        public RegisteredServiceProvider<Economy> vaultEcoAPI = 
            getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (vaultEcoAPI != null || (economy = vaultEcoAPI.getProvider()) == null) {
            getLogger().info("Vault尚未配置正确！请检查Vault插件！");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
```

## 简单的操作方法
如果你有一个Player对象或玩家的名称（官方不推荐使用名称），你可以进行如下操作：

```java
economy.hasAccount(player);                    //检查玩家在经济插件中是否有账户（一般来说不判断，直接给钱应该没问题，扣钱的话如果没账户，部分老插件会报错）
economy.getBalance(player);                    //获取玩家player余额
economy.has(player, 233.0);                    //检查玩家player账户里有没有233的余额
economy.withdrawPlayer(player, 233.0);         //给玩家player扣233
economy.depositPlayer(player, 666.0);          //给玩家player加666
```
