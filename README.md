

MvpAutoCodePlus
=================

[![Downloads][downloads-img]][plugin]

### JetBrains IDEA/Android Studio MVP模版代码生成插件

![screenshots](./images/mvp.gif)

特征
----
- 根据指定的父接口生成MVP Contract接口类.
- 可选的根据生成的MVP Contract和指定的父类生成MVP实现类.
  - 支持Activity
  - 支持Fragment
  - 支持Presenter
  - 支持Model
- 支持Java和Kotlin语言

支持的 IDE:
- Android Studio
- IntelliJ IDEA
- IntelliJ IDEA Community Edition

安装
----
- **使用 IDE 内置插件系统安装:**
  - <kbd>File</kbd> > <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>搜索并找到"MvpAutoCodePlus"</kbd> > <kbd>Install Plugin</kbd>

- **手动安装:**
  - 下载[`最新发布的插件包`][latest-release] > <kbd>File</kbd> ><kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>

重启**IDE**.

使用
----

1. 配置父接口:

   -  <kbd>File</kbd> > <kbd>Preferences(Settings)</kbd> > <kbd>Other Settings</kbd> > <kbd>MvpAutoCodePlus</kbd> > 

     ![settings](./images/settings.png)

2. 生成:

   ![use1](./images/use1.png)

   - 右击要生成的目标包,<kbd>New</kbd> > <kbd>Generate Mvp Code</kbd> (或选中包,按Alt+Insert),生成contract包后可以选中contract包或contract的父包.

   - 输入生成的代码主名,比如要做Login功能,就输入Login,生成是ILoginContract,LoginActivity,LoginPresenter,LoginModel

   - 选择代码实现方式,可选Java或Kotlin

   - 选择View的实现方式,Activity或Fragment,如果配置了多个的话,可选择其中之一,不想生成的项去掉前面的复选框.(如果使用了泛型的话,会导致其他类中该类找不到的问题,比如P的实现类中有泛型M,但是没有勾选生成M的实现类,P的实现类中就会找不到)

   - 点击Ok,稍等一会儿,代码就生成了,生成的包结构如下:

     ![use2](./images/use2.png)

## 关于父接口

只使用了我自己目前使用的接口来开发测试,欢迎各位使用自己的接口进行测试.如需要[`我使用的接口`][my_interface] ,请下载后放入自己的项目中.

## 存在问题

- 父接口和父类的输入比较麻烦,还没有找到能在设置界面选择项目中的class的方法,好在只用设置一次就可保存了,项目中也不会经常变动.
- 其他未知问题,此插件是作者首次开发插件,完全从0开始,官方的文档不是很详细,英语水平更是不堪,国内的参考资料也不多,做这个纯粹是忽然想到了,就开始做了.难免有未尽之处,欢迎各位star,issue.


[完整的更新历史记录](./CHANGELOG.md)


[latest-release]: https://plugins.jetbrains.com/plugin/10907-mvpautocodeplus
[downloads-img]: https://img.shields.io/jetbrains/plugin/d/8579.svg?style=flat-square
[plugin]: https://plugins.jetbrains.com/plugin/10907-mvpautocodeplus
[my_interface]:https://github.com/longforus/MVPExample
