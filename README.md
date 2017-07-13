# Sensor
## 搭建环境
* JDK >= 1.8
* 服务器 Tomcat
* 数据库 MySQL
## 源码使用说明：
* 使用IDEA编译器，File → New → Project from Existing Sources 选择文件目录下的pom.xml文件，然后next next next finish
* 修改com.yyn.config中的TDBConfig文件路径，其中root用来存放提交的TDB文件，file用来存储规则文件和提交的owl文件
* 运行doc文件夹中的sql文件创建数据表

file文件夹中存放了wot.owl文件，用于刻画了物联网情景的本体。网站内置了对设备的添加，编辑删除，以及sparql和rule文件的运行环境。要实现添加设备及设备控制等功能，需要引用wot.owl本体中的Sensor和Actuator等概念，设备的属性需要继承WotProperty实体。

## 项目使用说明
* 注册，登录
* 添加本体，上传owl文件，并输入本体名称，本体描述以及本体的URL地址
![输入图片说明](https://git.oschina.net/uploads/images/2017/0709/152937_2ca4a900_773725.png "在这里输入图片标题")
* 上传本体之后可以在本体编辑界面看到上传的本体，可以对本体进行编辑
![输入图片说明](https://git.oschina.net/uploads/images/2017/0709/160340_a909f59c_773725.png "在这里输入图片标题")
* 本体展示功能，展示owl文件中的所有前缀，概念以及概念的继承关系
![输入图片说明](https://git.oschina.net/uploads/images/2017/0709/170922_4db71ed9_773725.png "在这里输入图片标题")
* 拓展本体功能，对本体有新的编辑修改，可以通过拓展的方式来合并新的本体和旧的本体。
![输入图片说明](https://git.oschina.net/uploads/images/2017/0709/171119_4d5ea0ce_773725.png "在这里输入图片标题")
* 添加设备功能，可以读取本体中继承WotProperty的概念作为设备属性，通过属性来描述设备。
![输入图片说明](https://git.oschina.net/uploads/images/2017/0709/171242_cab54a08_773725.png "在这里输入图片标题")
* 我的设备列表，展示添加的设备，对设备进行删除
![输入图片说明](https://git.oschina.net/uploads/images/2017/0709/171354_8aabb114_773725.png "在这里输入图片标题")
* 配置规则功能，添加sparql或者rule语句来本体运行规则。规则一旦制定，后台会每隔20秒执行一次规则，同时输出规则运行后的owl文件，请到TDBConfig中设置的文件路径中查看
![输入图片说明](https://git.oschina.net/uploads/images/2017/0709/171652_dd2ac52f_773725.png "在这里输入图片标题")
* 传感属性阈值配置，传感器检测的属性有正常状态和异常状态，在阈值配置中，设置属性正常状态的上下阈值。
![输入图片说明](https://git.oschina.net/uploads/images/2017/0709/184048_0555fd6c_773725.png "在这里输入图片标题")
