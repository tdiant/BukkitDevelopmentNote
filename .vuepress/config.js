module.exports = {
    title: 'Bukkit Development Note',
    description: 'A guide to develop Minecraft Server plugins based on Bukkit.',

    base: '/BukkitDevelopmentNote/',

    head: [
        // add jquert and fancybox
        ['script', { src: 'https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.slim.min.js' }],
        ['script', { src: 'https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.5.2/jquery.fancybox.min.js' }],
        ['link', { rel: 'stylesheet', type: 'text/css', href: 'https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.5.2/jquery.fancybox.min.css' }]
    ],

    themeConfig: {
        sidebarDepth: 2,
        nav: [
            { text: '首页', link: '/home' },
            { text: '贡献', link: '/contribute' },
            { text: '赞助', link: 'https://alpha.tdiant.net/donate.html' },
            { text: 'GitHub仓库', link: 'https://github.com/tdiant/BukkitDevelopmentNote' }
        ],
        sidebar: [
            ["/home.md","欢迎"],
            {
                title: "第一部分: 基本概念",
                collapsable: false,
                children: [
                    ["/unit/1-1.md","写在前面"],
                    ["/unit/1-2.md","MC的服务端介绍"],
                    ["/unit/1-3.md","代码中的MC世界"],
                    ["/unit/1-4.md","检索需要的信息"],
                    ["/unit/1-5.md","服务端与客户端"]
                ]
            },
            {
                title: "第二部分: 基础内容",
                collapsable: false,
                children: [
                    ["/unit/2-1.md","最简单的插件"],
                    ["/unit/2-2.md","事件的监听"],
                    ["/unit/2-3.md","配置API"],
                    ["/unit/2-4.md","命令执行器"]
                ]
            },
            {
                title: "第三部分: 进阶功能",
                collapsable: false,
                children: [
                    ["/unit/3-2.md","自定义事件"],
                    ["/unit/3-3.md","深入plugin.yml"],
                    ["/unit/3-4.md","配置API的序列化和遍历"],
                    ["/unit/3-5.md","多线程多任务框架"],
                    ["/unit/3-6.md","自定义合成表"],
                    ["/unit/3-7.md","粒子效果和音效播放"],
                    ["/unit/3-8.md","世界生成器"],
                    ["/unit/3-9.md","Title、Bar与计分板显示"],
                    ["/unit/3-10.md","经验和成就"],
                    ["/unit/3-11.md","插件系统基本玩法"]
                ]
            },
            {
                title: "第四部分: 常用依赖",
                collapsable: false,
                children: [
                    ["/unit/4-1.md","Vault"],
                    ["/unit/4-2.md","ProtocolLib"],
                    ["/unit/4-3.md","Bungeecord"],
                    ["/unit/4-4.md","PlaceholderAPI"],
                    ["/unit/4-5.md","VexView"]
                ]
            },
            {
                title: "第五部分: 底层部分",
                collapsable: false,
                children: [
                    ["/unit/5-1.md","认识NMS与OBC"],
                    ["/unit/5-2.md","自定义发包"],
                    ["/unit/5-3.md","NBT数据操作"]
                ]
            }
        ]
    }
}
