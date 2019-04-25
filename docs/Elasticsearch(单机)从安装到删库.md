# ***Elasticsearch(单机, For SkyWalking)从安装到删库***

***TODO:*** 写此安装过程时还没有仔细研究elasticsearch的各项配置参数意义, 因此修改的配置详细解释还需补充完善 

## CentOS

### TAR 安装
```shell
# 进入对应目录
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.6.14.tar.gz
# 解压下载的TAR包
tar zxvf elasticsearch-5.6.14.tar.gz
groupadd elastic
useradd elastic
passwd elastic
    -> ******
chown -R elastic:elastic elasticsearch-5.6.14
cd elasticsearch-5.6.14/config/
vi jvm.options
# 修改最小虚拟机大小和最大虚拟机大小, 建议修改成同样大小, 避免jvm调整堆内存时导致程序暂停运行或不可预测的问题
-Xms2g -> -Xms512m
-Xmx2g -> -Xmx512m
# 退出vi编辑器
# 再修改elasticsearch.yml配置
vi elasticsearch.yml
# 修改以下属性, 对应SkyWalking中的配置
cluster.name: CollectorDBCluster
network.host: 0.0.0.0
thread_pool.bulk.queue_size: 1000
cd ../bin/
# 强迫症发作
rm -f *.bat
rm -f *.exe
su elastic
./elasticsearch
```
### RPM 安装
参考文档: 
* [官方文档(英文)](https://www.elastic.co/guide/en/elasticsearch/reference/5.6/rpm.html)
* [用RPM安装Elasticsearch到Linux系统服务](https://www.jianshu.com/p/3b0650b5c7bb)
```shell
# Download and install the public signing key
rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch
# We sign all of our packages with the Elasticsearch Signing Key (PGP key D88E42B4, available from https://pgp.mit.edu) with fingerprint: 4609 5ACC 8548 582C 1A26 99A9 D27D 666C D88E 42B4
cd /etc/yum.repos.d/
touch elasticsearch.repo
vi elasticsearch.repo
# 粘贴以下内容
[elasticsearch-5.x]
name=Elasticsearch repository for 5.x packages
baseurl=https://artifacts.elastic.co/packages/5.x/yum
gpgcheck=1
gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
enabled=1
autorefresh=1
type=rpm-md
# 退出vi编辑器
# 使用yum安装elasticsearch
yum install elasticsearch
# 通过命令查询系统使用SysV init 还是systemd
ps -p 1
# 如果显示是SysV init, 使用以下命令
chkconfig --add elasticsearch
service elasticsearch start
service elasticsearch stop
# 如果显示是systemd, 使用以下命令
/bin/systemctl daemon-reload
/bin/systemctl enable elasticsearch.service
systemctl start elasticsearch.service
systemctl stop elasticsearch.service
```

## Win10

相对CentOS的安装过程, Windows的安装十分简单粗暴。

下载 [elasticsearch-5.6.14.zip](https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.6.14.zip)

解压后修改一下elasticsearch.yml和jvm.options配置, 运行bin/elasticsearch5.6.14.exe即可

## 目前踩过的坑

### 1. 不允许root用户启动elasticsearch

```shell
java.lang.RuntimeException: can not run elasticsearch as root
        at org.elasticsearch.bootstrap.Bootstrap.initializeNatives(Bootstrap.java:106) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Bootstrap.setup(Bootstrap.java:195) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Bootstrap.init(Bootstrap.java:342) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:132) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.execute(Elasticsearch.java:123) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.cli.EnvironmentAwareCommand.execute(EnvironmentAwareCommand.java:70) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.cli.Command.mainWithoutErrorHandling(Command.java:134) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.cli.Command.main(Command.java:90) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:91) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:84) [elasticsearch-5.6.14.jar:5.6.14]
```
#### 原因

elasticsearch不允许使用root用户启动。

#### 解决方法

使用其他用户启动elasticsearch。

### 2. 获取系统路径空指针

```shell
[2018-12-26T15:28:40,711][ERROR][o.e.b.Bootstrap          ] Exception
java.lang.NullPointerException: null
        at sun.nio.fs.UnixFileSystem.getPath(UnixFileSystem.java:273) ~[?:1.8.0_11]
        at org.elasticsearch.common.io.PathUtils.get(PathUtils.java:60) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsProbe.readSysFsCgroupCpuAcctCpuAcctUsage(OsProbe.java:274) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsProbe.getCgroupCpuAcctUsageNanos(OsProbe.java:261) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsProbe.getCgroup(OsProbe.java:419) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsProbe.osStats(OsProbe.java:464) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsService.<init>(OsService.java:45) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.MonitorService.<init>(MonitorService.java:45) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.node.Node.<init>(Node.java:362) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.node.Node.<init>(Node.java:245) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Bootstrap$5.<init>(Bootstrap.java:233) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Bootstrap.setup(Bootstrap.java:233) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Bootstrap.init(Bootstrap.java:342) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:132) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.execute(Elasticsearch.java:123) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.cli.EnvironmentAwareCommand.execute(EnvironmentAwareCommand.java:70) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.cli.Command.mainWithoutErrorHandling(Command.java:134) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.cli.Command.main(Command.java:90) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:91) [elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:84) [elasticsearch-5.6.14.jar:5.6.14]
[2018-12-26T15:28:40,719][WARN ][o.e.b.ElasticsearchUncaughtExceptionHandler] [node-1] uncaught exception in thread [main]
org.elasticsearch.bootstrap.StartupException: java.lang.NullPointerException
        at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:136) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.execute(Elasticsearch.java:123) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.cli.EnvironmentAwareCommand.execute(EnvironmentAwareCommand.java:70) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.cli.Command.mainWithoutErrorHandling(Command.java:134) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.cli.Command.main(Command.java:90) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:91) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:84) ~[elasticsearch-5.6.14.jar:5.6.14]
Caused by: java.lang.NullPointerException
        at sun.nio.fs.UnixFileSystem.getPath(UnixFileSystem.java:273) ~[?:1.8.0_11]
        at org.elasticsearch.common.io.PathUtils.get(PathUtils.java:60) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsProbe.readSysFsCgroupCpuAcctCpuAcctUsage(OsProbe.java:274) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsProbe.getCgroupCpuAcctUsageNanos(OsProbe.java:261) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsProbe.getCgroup(OsProbe.java:419) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsProbe.osStats(OsProbe.java:464) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.os.OsService.<init>(OsService.java:45) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.monitor.MonitorService.<init>(MonitorService.java:45) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.node.Node.<init>(Node.java:362) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.node.Node.<init>(Node.java:245) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Bootstrap$5.<init>(Bootstrap.java:233) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Bootstrap.setup(Bootstrap.java:233) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Bootstrap.init(Bootstrap.java:342) ~[elasticsearch-5.6.14.jar:5.6.14]
        at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:132) ~[elasticsearch-5.6.14.jar:5.6.14]
        ... 6 more
```

#### 原因

可能原因是系统未实现cgroups。
> This is the case when the Linux kernel cpu scheduler is replaced by another scheduler, like BFS for example.
> > [http://ck.kolivas.org/patches/bfs/bfs-faq.txt 6](http://ck.kolivas.org/patches/bfs/bfs-faq.txt 6)
> >
> > > BFS does NOT implement CGROUPS.

#### 解决方法

参考信息1: [discuss - Why elasticsearch-5.2.0 throw NullPointerException when start](https://discuss.elastic.co/t/why-elasticsearch-5-2-0-throw-nullpointerexception-when-start/74739)

参考信息2: [GitHub - issue#27031](https://github.com/elastic/elasticsearch/issues/27031)

```shell
# 添加cgroup挂载点
sudo mount -t cgroup -o rw,nosuid,nodev,noexec,relatime,cpu,cpuacct cgroup /sys/fs/cgroup/cpu,cpuacct
```

### 3. max file descriptors [1024] for elasticsearch process is too low, increase to at least [65536]
```shell
[yyyy-MM-ddThh:mm:dd,mil][ERROR][o.e.b.Bootstrap          ] [node-1] node validation exception
[1] bootstrap checks failed
[1]: max file descriptors [65535] for elasticsearch process is too low, increase to at least [1024]
[yyyy-MM-ddThh:mm:dd,mil][INFO ][o.e.n.Node               ] [node-1] stopping ...
[yyyy-MM-ddThh:mm:dd,mil][INFO ][o.e.n.Node               ] [node-1] stopped
[yyyy-MM-ddThh:mm:dd,mil][INFO ][o.e.n.Node               ] [node-1] closing ...
[yyyy-MM-ddThh:mm:dd,mil][INFO ][o.e.n.Node               ] [node-1] closed
```

#### 原因

看不懂。谷歌翻译说是同时打开的文件句柄数量不够。

官方文档的解释如下:

> On Linux systems, ulimit can be used to change resource limits on a temporary basis. Limits usually need to be set as root before switching to the user that will run Elasticsearch. For example, to set the number of open file handles (ulimit -n) to 65,536, you can do the following:

#### 解决方法

**参考信息:** [elasticsearch Docs - Configuring system settings](https://www.elastic.co/guide/en/elasticsearch/reference/5.6/setting-system-settings.html)

官方文档的解决方案:

> ulimit -n 65536

但直接使用可能导致这个错误:

> [1]: max file descriptors [65535] for elasticsearch process is too low, increase to at least [65536]

此时可以: 

* 重启SSH客户端(如Xshell)(别人的博客上说直接重启即可, 没确认)

* 直接修改系统配置/etc/security/limits.conf

```shell
vi /etc/security/limits.conf
# End of file
root soft nofile 65536
root hard nofile 65536
# 把这里的65535改成65536再重启elasticsearch
* soft nofile 65535
* hard nofile 65535
```

### 4. 找不到JVM

在使用TAR安装时没报这个错。但使用RPM安装后可能会遇到此问题。

#### 原因

可能是使用RPM安装后，没有在默认位置找到jdk。 

#### 解决方法

```shell
vi /etc/sysconfig/elasticsearch
# 修改JAVA_HOME路径
service elasticsearch start
```

### 5. High disk watermark 警告

```shell
[yyyy-MM-ddThh:mm:SS,mil][WARN ][o.e.c.r.a.DiskThresholdMonitor] [node-1] high disk watermark [90%] exceeded on [your_instance_id][node-1][/usr/local/elasticsearch-5.6.14/data/nodes/0] free: 3gb[7.8%], shards will be relocated away from this node
[yyyy-MM-ddThh:mm:SS,mil][INFO ][o.e.c.r.a.DiskThresholdMonitor] [node-1] rerouting shards: [high disk watermark exceeded on one or more nodes]
```
#### 原因

硬盘容量不足。

#### 解决方法

参考信息: [discuss - High disk watermark in elastcisearch](https://discuss.elastic.co/t/high-disk-watermark-in-elastcisearch/2666)

以下解决方法任选一个:

* 清理一下日志文件
* 挂载一个新硬盘
* 找个新机器安装elasticsearch

