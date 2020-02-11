# 世界生成器

> 本章编写参考了如下内容, 这篇文章对于插件开发而言十分重要:  
> https://www.mcbbs.net/thread-811614-1-1.html

> [如果你对Minecraft 1.13中世界生成机制大改动感兴趣, 可以点击这里.](https://www.mcbbs.net/thread-846195-1-1.html)     
>  并且, [对于Minecraft 1.13之前版本的世界生成阐述, 可以见此文](https://www.zhihu.com/question/20754279)  
>   
> *在Bukkit中, 截止到目前, BukkitAPI仍沿用旧有规则的API.*  
> *这意味着本文内容截止目前对于新版本的插件开发仍然有效.*  

本文中使用了`Material.GRASS_BLOCK`, 这是1.13版本的新用法.   
在旧版API中, 应该使用`Material.GRASS`.  

# 简述世界生成

Minecraft中, 一个世界(World)按照一定的大小被分为多个区块(Chunk).  
MC会自动地按照一定的规则卸载无人Chunk, 在需要的时候加载所需的Chunk到内存, 以此来保证一个World被加载到内存, 这样不至于整个World都需要加载到内存以备调用.  
世界的生成同样以Chunk为单位.

Minecraft游戏中, 世界生成分为两个阶段, 分别为 Generation 与 Population.  

Minecraft生成一个World, 首先进入 Generation 阶段. 这一阶段主要是绘制地形等.  
1. Minecraft首先会获取该Chunk中包含的所有生物群系. 然后会根据特定的生物群系绘制基本的地形. 地形的绘制依靠了一些特殊的算法, 游戏通常会以高度63作为水平面, 通过这些特殊算法绘制基本的地形. 绘制完毕后, 整个世界只有空气、水和石头.  
2. 接着会在高度0-5范围内生成基岩, 并逐个对各个生物群系添加特有的方块. 例如, 对平原添加草方块和泥土, 对沙漠添加沙子和沙石等.  
3. 再然后会生成特殊地形. 这里的特殊地形指的是涉及到多个区块的大型地形, 例如规模很大的洞穴、村庄、矿井等.  
4. 最后会进一步处理, 做最后的准备收尾工作, 至此Generation阶段完毕.  

Generation阶段完成, 意味着该世界的整体结构已经定型. 但是这个世界上还缺少“点缀”. 这个世界上仍然没有树、生物、沼泽上的荷叶、水边的甘蔗等. 此时进入 Population 阶段.  
1. 首先会对该世界的实体进行完善, 并生成各种各样的特殊的方块(指的是箱子等方块实体, 这些方块与其它方块相比复杂许多).  
2. 然后会生成小型地形. 比如一些地表小水坑、地表岩浆池、地下地牢等.  
3. 然后会在地下按照一定的规则生成矿物.  
4. 最后增加地面点缀, 生成水边的甘蔗、沼泽上的荷叶、地面大蘑菇和树木等物, 并增加一些生物群系特定物, 生成一些基础生物(比如牛、鸡、羊等).  

待 Population 阶段结束后, 该Chunk的数据便会存储起来, 显示出来.  

# 干涉Population

Bukkit中, 在世界初始化前会触发`WorldInitEvent`事件. 监听该事件, 我们可以对该世界生成的 Population 阶段进行干涉.  

在下面的案例中, 我们将在Chunk的 Population 阶段, 在世界的草方块上人为的添加许多钻石块(DIAMOND_BLOCK).  

```java
public class WorldListener implements Listener {
    @EventHandler
    public void onWorldInit(WorldInitEvent e){
        if(e.getWorld().getName().equals("World"))
            e.getWorld().getPopulators().add(new RuaPopulator());
    }
}

class RuaPopulator extends BlockPopulator {
    @Override
    public void populate(World w, Random r, Chunk c){
        final int maxn = 16; //一个区块的X或Y范围是0-16
        for(int i=0; i<12; i++){ //这里打算一个区块生成12个
            int x = r.nextInt(maxn), z = r.nextInt(maxn);
            for (int y = 125; y > 0; y--) {
                if (c.getBlock(x, y, z).getType() == Material.GRASS_BLOCK && c.getBlock(x, y + 1, z).getType() == Material.AIR) {
                    c.getBlock(x, y + 1, z).setType(Material.DIAMOND_BLOCK);
                    break;
                }
            }
        }
    }
}

```

最终效果如下:  

![](https://i.loli.net/2020/02/08/AkEZ7VuOv8wWjKm.jpg)

可以发现, 生成的world中, 按照我们的设定, 在地表草方块上零散的分布了钻石块.  
这说明在Bukkit中, 你可以创建一个BlockPopulator对象, 在世界初始化时添加为某一World的Populator, 依此来干涉Population阶段.  
*Bukkit中的Populator只有BlockPopulator一种.*   
*但是你可以以此类推, 通过这种方式实现在地面随机生成某种建筑等其他效果.*

值得注意的是, 在自定义的Populator中, populate方法的参数中有一个传入的Random对象.  
这是为了让随机数的生成符合World对应的种子. 在需要生成随机数的时候, 应尽可能使用方法参数中的Random对象.  

# 控制Generation

通过控制一个世界的Generation, 我们可以控制世界的大体地形.  
下面我们将在插件加载时, 生成一个新的世界`RuaWorld`, 这个世界是一个超平坦世界, 第一第二层为基岩, 第三层为草方块.  

```java
public class Main extends JavaPlugin {
    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(new WorldListener(),this);
        Bukkit.createWorld(new WorldCreator("RuaWorld").generator(new RuaChunkGenerator()));
    }

    public void onDisable(){
        //
    }
}

class RuaChunkGenerator extends ChunkGenerator {
    @Override
    public ChunkData generateChunkData(World w, Random r, int x, int z, BiomeGrid b) {
        ChunkData chunkData = createChunkData(w); //创建区块数据

        //下面这行方法调用参数中, 前三个参数代表一个XYZ对, 后面又是一个XYZ对.
        //这两个XYZ对是选区的意思, 你可以结合Residence插件圈地、WorldEdit选区的思路思考.
        //提醒: 一个Chunk的X、Z取值是0-16, Y取值是0-255.
        chunkData.setRegion(0, 0, 0, 16, 2, 16, Material.BEDROCK);  //填充基岩
        chunkData.setRegion(0, 2, 0, 16, 3, 16, Material.GRASS_BLOCK); //填充草方块
        
        //将整个区块都设置为平原生物群系(PLAINS)
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                b.setBiome(i, j, Biome.PLAINS);
            }
        }
        return chunkData;
    }
}
```

![](https://i.loli.net/2020/02/08/yTaJ1z7A2j4dkB6.jpg)  

![](https://i.loli.net/2020/02/08/6wiDNdl8y3AmJFh.jpg)

我们进入`RuaWorld`世界, 可以发现世界按照我们所需要的地形生成了.  

