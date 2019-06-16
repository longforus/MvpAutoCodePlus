

MvpAutoCodePlus
=================

[![Downloads][downloads-img]][plugin]

[Machine translation of English documents](./README_EN.md)

### JetBrains IDEA/Android Studio MVP模版代码生成插件

![screenshots](./images/mvp.gif)

特征
----
- 根据指定的父接口生成MVP Contract接口类.
- 可选的根据生成的MVP Contract和指定的父类生成MVP实现类,并添加抽象方法默认实现.
  - 支持Activity
  - 支持Fragment
  - 支持Presenter
  - 支持Model
- 支持Java和Kotlin语言

支持的 IDE:
- Android Studio(从3.1(173.3727—173.* )开始支持)
- IntelliJ IDEA
- IntelliJ IDEA Community Edition

安装
----

- **使用 IDE 内置插件系统安装:**
  - <kbd>File</kbd> > <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>搜索并找到"MvpAutoCodePlus"</kbd> > <kbd>Install Plugin</kbd>

   ![install](./images/install1.png)

- **手动安装:**

  - 下载[`最新发布的插件包`][latest-release] > <kbd>File</kbd> ><kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>

重启**IDE**.

使用
----

1. 配置父接口:

   - <kbd>File</kbd> > <kbd>Preferences(Settings)</kbd> > <kbd>Other Settings</kbd> > <kbd>MvpAutoCodePlus</kbd> > 

    ![settings](./images/settings.png)

    基于[`这一套Java接口`][my_interface_java]的配置示例:

    ![settings1](./images/settings1.png)

     项目内的Class已经在V1.0beta2支持选择,但是如果有泛型限定的话,泛型还是需要手动添加,比如选择的类签名为:**com.longforus.base.java.BasePresenterJv<V extends IView,M extends IModel>**

     ![use3](./images/use3.png)

     需要手动添加后面的泛型限定<V,M>:

     ![use4](./images/use4.png)

     现在还支持全局和当前项目模式,全局模式中配置的接口可在所有项目中使用,当前项目中配置的接口只在当前项目中起效,方便有多个不相同工程时的无缝切换.

2. 生成:

   ![use1](./images/use1.png)

   - 右击要生成的目标包,<kbd>New</kbd> > <kbd>Generate Mvp Code</kbd> (或选中包,按Alt+Insert),生成contract包后可以选中contract包或contract的父包.

   - 输入生成的代码主名,比如要做Login功能,就输入Login,生成结果是ILoginContract,LoginActivity,LoginPresenter,LoginModel

   - 从1.4开始如果P的超接口以ViewModel结尾，那么生成的结果是ILoginContract,LoginActivity,LoginViewModel,LoginModel

   - 选择代码实现方式,可选Java或Kotlin

   - 选择View的实现方式,Activity或Fragment,如果配置了多个的话,可选择其中之一,不想生成的项去掉前面的复选框.如果没有输入P和M实现类的超类,那么生成的P和M的实现类只会实现对应的接口.

   - 从1.1开始支持不生成Model接口和对应的实现类,更加灵活.

   - 点击Ok,稍等一会儿,代码就生成了,生成的包结构如下:
   
     ![use2](./images/use2.png)

## 关于父接口

只使用了我自己目前使用的接口来开发测试,欢迎各位使用自己的接口进行测试.如需要[`我使用的接口`][my_interface] ,请下载后放入自己的项目中.

## 存在问题

- 父接口和父类如果有泛型限定在选择后还需要手动输入泛型,好在只用设置一次就可保存了,项目中也不会经常变动.
- 还没有研究生成View对应的布局文件,要是也能生成默认的,会更方便一些.
- 其他未知问题,此插件是作者首次开发插件,完全从0开始,官方的文档不是很详细,英语水平更是不堪,国内的参考资料也不多,做这个纯粹是忽然想到了,就开始做了.难免有未尽之处,欢迎各位star,issue.

[完整的更新历史记录](./CHANGELOG.md)

[`鸿洋公众号插件广告文`][ad_link] ,感谢大神给予机会.插件的开发,推荐大家查看鸿洋的开发教程,要是插件不符合你的要求.可以issue,也可以fork自行修改.感谢你的关注.

[latest-release]: https://plugins.jetbrains.com/plugin/10907-mvpautocodeplus
[downloads-img]: https://img.shields.io/jetbrains/plugin/d/8579.svg?style=flat-square
[plugin]: https://plugins.jetbrains.com/plugin/10907-mvpautocodeplus
[my_interface]:https://github.com/longforus/MVPExample
[my_interface_java]:https://github.com/longforus/MVPExample/tree/master/app/src/main/java/com/longforus/base/java
[ad_link]:https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&amp;amp;amp;amp;mid=2650825783&amp;amp;amp;amp;idx=1&amp;amp;amp;amp;sn=0b0c2c58a729e1d9122ce9c09e31637f&amp;amp;amp;amp;chksm=80b7b0a9b7c039bfa92deb5c8fe51f5347ebdccf0be70078ffa047e7316baf5679a89fc788ac&amp;amp;amp;amp;mpshare=1&amp;amp;amp;amp;scene=23&amp;amp;amp;amp;srcid=0711IpaZwE1iGToWw6e7fix8#rd
