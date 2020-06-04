# 判断一个 Java 进程是被什么杀掉的

## Java 进程可能会被谁杀掉

### 1. Linux内核

Linux 内核有一种 `Out of Memory Killer`机制。

当系统内存使用逼近内存的极限时，`OOM Killer`会选择性的杀死某些进程，以确保内核能够正常运行，不会被进程占用所有的内存导致系统崩溃。

而检查这个机制是否被触发，可以通过检查`/var/logs/messages`文件来确定，或通过`dmesg`命令查看

```shell
grep -i 'out of memory' /var/logs/messages
dmesg | grep -i 'java'
```

### 2. JVM

Java进程在启动的时候，可以选择在 JVM OOM 的时候，把内存给 dump 出来

```
.hprof
```

### 3. 线程崩溃

```
hs_err_pid*.log
```

## 当 Java 进程异常退出但没有任何日志的时候

> 这个时候就需要一点反向思维了。

在某一次的生产事故中，我就遭遇过这种状况，明明打开了所有能输出日志的配置，可是就是没有打印出异常。


这时候怎么办呢？

**答案是：需要一些反向思维。**

我们可以先把`-Xms`配置和`-Xmx`配置调整到一个极小值，拉起这个Java进程，让他以最快的速度被杀掉，这时候首先可以排除是否是因为系统权限的原因无法打印日志。


等到打印了日志以后，再根据上述的方法去慢慢调整 `JVM` 的配置，一步步确定原因。

此外，还可以使用`jmap -heap pid`的方法去检查，是否存在内存泄漏。

举个例子：
```shell
[zillah@localhost ~]# jmap -heap 12966
Attaching to process ID 12966, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.231-b11

using parallel threads in the new generation.
using thread-local object allocation.
Concurrent Mark-Sweep GC

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 2818572288 (2688.0MB)
   NewSize                  = 1006632960 (960.0MB)
   MaxNewSize               = 1006632960 (960.0MB)
   OldSize                  = 1811939328 (1728.0MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 536870912 (512.0MB)
   CompressedClassSpaceSize = 528482304 (504.0MB)
   MaxMetaspaceSize         = 536870912 (512.0MB)
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
New Generation (Eden + 1 Survivor Space):
   capacity = 905969664 (864.0MB)
   used     = 62231392 (59.348480224609375MB)
   free     = 843738272 (804.6515197753906MB)
   6.869037063033493% used
Eden Space:
   capacity = 805306368 (768.0MB)
   used     = 51036712 (48.672401428222656MB)
   free     = 754269656 (719.3275985717773MB)
   6.337552269299825% used
From Space:
   capacity = 100663296 (96.0MB)
   used     = 11194680 (10.676078796386719MB)
   free     = 89468616 (85.32392120361328MB)
   11.120915412902832% used
To Space:
   capacity = 100663296 (96.0MB)
   used     = 0 (0.0MB)
   free     = 100663296 (96.0MB)
   0.0% used
concurrent mark-sweep generation:
   capacity = 1811939328 (1728.0MB)
   used     = 5864400 (5.5927276611328125MB)
   free     = 1806074928 (1722.4072723388672MB)
   0.3236532211303711% used

10598 interned Strings occupying 853968 bytes.
```

通过这个命令，大致可以判断，这个进程没有内存泄露的问题存在。



> p.s. 事实上，工作中，经常需要这种反向思维，尤其是在单向思维无法找到解决问题的关键的时候。这点是我在这次生产事故中学到的很重要的一点。
>
> 知易行难，只能在平时多提醒自己，不要只朝着一个方向看问题，换个方向，也许豁然开朗。
