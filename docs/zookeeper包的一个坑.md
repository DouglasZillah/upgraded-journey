# 关于 zookeeper 包下载的一个坑

zookeeper 作为目前来说工作常用, 面试常考的`注册中心`和`分布式锁`来说已经变成一项必须要掌握的技能。所以在自己电脑上装一个 zookeeper, 平时没事的时候捣鼓一下是很有必要的。


**但是!**


zookeeper 的安装包下载是个大坑啊。


GitHub 上 zookeeper 项目 release 页的包是 **不可以** 直接下载下来用的。用 `Maven` 打包也行不通, `zkServer` 子项目配置文件读取一个 `git` 配置时错误。估计还要下载一个git才行。


## 错误的示范: 
1. 打开[GitHub - zookeeper - release页](https://github.com/apache/zookeeper/releases)
2. 下载合适的版本
3. 解压 `tar` 包
4. 复制并修改`zoo.cfg`配置文件
5. 运行 zookeeper `./bin/zkServer start`
6. 报错啦~>_<~
```shell
ZooKeeper JMX enabled by default
Using config: /data/woke/zookeeper-release-3.4.14/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
# 查看错误日志
less zookeeper.out
Error: Could not find or load main class org.apache.zookeeper.server.quorum.QuorumPeerMain
```

### 2019.04.25 更新
从 [GitHub - zookeeper - release页](https://github.com/apache/zookeeper/releases) 下载所需版本的 `tar`包后, 进入[Maven 中央仓库](https://repo1.maven.org/maven2/org/apache/zookeeper/zookeeper/)下载对应版本的jar包到解压后的目录中, 再运行 `bin/zkServer start` 似乎也可以


### 2019.04.29 更新
从别的地方又看到一个办法, 从这里[http://archive.apache.org/dist/zookeeper/](http://archive.apache.org/dist/zookeeper/)下载`tar`包


## 正确的打开方式: 
1. 打开[Apache ZooKeeper 官网](http://zookeeper.apache.org/), 点击 Apache ZooKeeper™ Releases 下的 `Download` 按钮
2. 找到合适的版本开始下载
```shell
# 此处直接从 CentOS 上下载 zookeeper tar 文件
wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.4.14/zookeeper-3.4.14.tar.gz
```
3. 解压
```shell
tar zxf zookeeper-3.4.14.tar.gz
```
4. 进入 zookeeper 目录
```shell
cd /path/to/zookeeper
```
5. 复制模板配置文件并修改文件名
```shell
cp conf/zoo_sample.cfg conf/zoo.cfg
```
6. 新建 zookeeper 工作目录
```shell
mkdir data
```
7. 修改配置
```shell
 vi conf/zoo.cfg
 ``` 
> ```cfg
> # The number of milliseconds of each tick
> tickTime=2000
> # The number of ticks that the initial
> # synchronization phase can take
> initLimit=10
> # The number of ticks that can pass between
> # sending a request and getting an acknowledgement
> syncLimit=5
> # the directory where the snapshot is stored.
> # do not use /tmp for storage, /tmp here is just
> # example sakes.
> # 此处配置应修改为自己的 zookeeper 路径位置
> # 注意, 如果 zookeeper 目录下没有 data 目录, 启动时会报错
> # dataDir=/tmp/zookeeper
> dataDir=/path/to/zookeeper/data
> # the port at which the clients will connect
> clientPort=2181
> # the maximum number of client connections.
> # increase this if you need to handle more clients
> #maxClientCnxns=60
> #
> # Be sure to read the maintenance section of the
> # administrator guide before turning on autopurge.
> #
> # http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
> #
> # The number of snapshots to retain in dataDir
> #autopurge.snapRetainCount=3
> # Purge task interval in hours
> # Set to "0" to disable auto purge feature
> #autopurge.purgeInterval=1
> ```
8. 启动 zookeeper
```shell
./bin/zkServer start
```


9. 检查日志
```shell
 2019-04-18 17:26:26,972 [myid:] - INFO  [main:QuorumPeerConfig@136] - Reading configuration from: /data/woke/zookeeper-3.4.14/bin/../conf/zoo.cfg
 2019-04-18 17:26:26,982 [myid:] - INFO  [main:DatadirCleanupManager@78] - autopurge.snapRetainCount set to 3
 2019-04-18 17:26:26,982 [myid:] - INFO  [main:DatadirCleanupManager@79] - autopurge.purgeInterval set to 0
 2019-04-18 17:26:26,982 [myid:] - INFO  [main:DatadirCleanupManager@101] - Purge task is not scheduled.
 2019-04-18 17:26:26,984 [myid:] - WARN  [main:QuorumPeerMain@116] - Either no config or no quorum defined in config, running  in standalone mode
 2019-04-18 17:26:27,007 [myid:] - INFO  [main:QuorumPeerConfig@136] - Reading configuration from: /data/woke/zookeeper-3.4.14/bin/../conf/zoo.cfg
 2019-04-18 17:26:27,008 [myid:] - INFO  [main:ZooKeeperServerMain@98] - Starting server
 2019-04-18 17:26:37,060 [myid:] - INFO  [main:Environment@100] - Server environment:zookeeper.version=3.4.14-4c25d480e66aadd371de8bd2fd8da255ac140bcf, built on 03/06/2019 16:18 GMT
 2019-04-18 17:26:37,061 [myid:] - INFO  [main:Environment@100] - Server environment:host.name=<NA>
 2019-04-18 17:26:37,061 [myid:] - INFO  [main:Environment@100] - Server environment:java.version=1.8.0_101
 2019-04-18 17:26:37,061 [myid:] - INFO  [main:Environment@100] - Server environment:java.vendor=Oracle Corporation
 2019-04-18 17:26:37,062 [myid:] - INFO  [main:Environment@100] - Server environment:java.home=/usr/local/jdk1.8.0_101/jre
 2019-04-18 17:26:37,062 [myid:] - INFO  [main:Environment@100] - Server environment:java.class.path=/data/woke/zookeeper-3.4.14/bin/../zookeeper-server/target/classes:/data/woke/zookeeper-3.4.14/bin/../build/classes:/data/woke/zookeeper-3.4.14/bin/../zookeeper-server/target/lib/*.jar:/data/woke/zookeeper-3.4.14/bin/../build/lib/*.jar:/data/woke/zookeeper-3.4.14/bin/../lib/slf4j-log4j12-1.7.25.jar:/data/woke/zookeeper-3.4.14/bin/../lib/slf4j-api-1.7.25.jar:/data/woke/zookeeper-3.4.14/bin/../lib/netty-3.10.6.Final.jar:/data/woke/zookeeper-3.4.14/bin/../lib/log4j-1.2.17.jar:/data/woke/zookeeper-3.4.14/bin/../lib/jline-0.9.94.jar:/data/woke/zookeeper-3.4.14/bin/../lib/audience-annotations-0.5.0.jar:/data/woke/zookeeper-3.4.14/bin/../zookeeper-3.4.14.jar:/data/woke/zookeeper-3.4.14/bin/../zookeeper-server/src/main/resources/lib/*.jar:/data/woke/zookeeper-3.4.14/bin/../conf:/usr/local/jdk1.8.0_101/jre/lib/ext:/usr/local/jdk1.8.0_101/lib/tools.jar
 2019-04-18 17:26:37,063 [myid:] - INFO  [main:Environment@100] - Server environment:java.library.path=/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
 2019-04-18 17:26:37,063 [myid:] - INFO  [main:Environment@100] - Server environment:java.io.tmpdir=/tmp
 2019-04-18 17:26:37,063 [myid:] - INFO  [main:Environment@100] - Server environment:java.compiler=<NA>
 2019-04-18 17:26:37,068 [myid:] - INFO  [main:Environment@100] - Server environment:os.name=Linux
 2019-04-18 17:26:37,069 [myid:] - INFO  [main:Environment@100] - Server environment:os.arch=amd64
 2019-04-18 17:26:37,069 [myid:] - INFO  [main:Environment@100] - Server environment:os.version=3.10.0-327.el7.x86_64
 2019-04-18 17:26:37,069 [myid:] - INFO  [main:Environment@100] - Server environment:user.name=root
 2019-04-18 17:26:37,070 [myid:] - INFO  [main:Environment@100] - Server environment:user.home=/root
 2019-04-18 17:26:37,070 [myid:] - INFO  [main:Environment@100] - Server environment:user.dir=/data/woke/zookeeper-3.4.14
 2019-04-18 17:26:37,095 [myid:] - INFO  [main:ZooKeeperServer@836] - tickTime set to 2000
 2019-04-18 17:26:37,095 [myid:] - INFO  [main:ZooKeeperServer@845] - minSessionTimeout set to -1
 2019-04-18 17:26:37,095 [myid:] - INFO  [main:ZooKeeperServer@854] - maxSessionTimeout set to -1
 2019-04-18 17:26:37,116 [myid:] - INFO  [main:ServerCnxnFactory@117] - Using org.apache.zookeeper.server.NIOServerCnxnFactory as server connection factory
 2019-04-18 17:26:37,125 [myid:] - INFO  [main:NIOServerCnxnFactory@89] - binding to port 0.0.0.0/0.0.0.0:2181
```

启动成功。
