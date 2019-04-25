# ***Linux硬盘挂载从入门到跑路：***



## 关于文件系统

centos6文件系统是ext4，因为设计较早，对于现今动辄上T的海量数据处理，性能较低。
centos7文件系统是xfs，适用于海量数据。这两种文件系统都是日志文件系统。
使用该文件系统的磁盘，空间包括两部分：日志空间和存储空间。
写入的数据是先暂存在日志空间，然后刷入存储空间，这样有利于恢复数据。
另外，xfs文件系统还支持将一块儿固态硬盘用作单独的日志空间盘，数据先写入固态硬盘，然后再刷入硬盘。
对于操作系统来说，数据写入了日志空间盘，就算完成了I/O，因此这种方式提高了系统性能。



## 使用命令

| 使用命令             |                                    |
| -------------------- | ---------------------------------- |
| fdisk -l             | 查看所有硬盘扇区大小，分区详情     |
| lsblk -af            | 查看所有硬盘及分区文件系统，挂载点 |
| mount /dev/vdb /disk | 为分区制定挂载点                   |
| df -h                | 显示已挂载的文件系统的磁盘使用情况 |



## 删库流程
以/dev/vdb为例。
vdb是一块尚未挂载和格式化的空磁盘，没有文件系统。
### fdisk -l 查看硬盘使用情况
```shell
[root@iZwz9avipe1jdw1wbokzc5Z dev]# fdisk -l

Disk /dev/vda: 107.4 GB, 107374182400 bytes, 209715200 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x000d2717

   Device Boot      Start         End      Blocks   Id  System
/dev/vda1   *        2048   209713151   104855552   83  Linux

Disk /dev/vdb: 42.9 GB, 42949672960 bytes, 83886080 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes

[root@iZwz9avipe1jdw1wbokzc5Z dev]# fdisk -l /dev/vdb 

Disk /dev/vdb: 42.9 GB, 42949672960 bytes, 83886080 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes


```




### fdisk分区

```shell
[root@iZwz9avipe1jdw1wbokzc5Z dev]# fdisk /dev/vdb
Welcome to fdisk (util-linux 2.23.2).

Changes will remain in memory only, until you decide to write them.
Be careful before using the write command.


Command (m for help): m
Command action
   a   toggle a bootable flag
   b   edit bsd disklabel
   c   toggle the dos compatibility flag
   d   delete a partition
   g   create a new empty GPT partition table
   G   create an IRIX (SGI) partition table
   l   list known partition types
   m   print this menu
   n   add a new partition
   o   create a new empty DOS partition table
   p   print the partition table
   q   quit without saving changes
   s   create a new empty Sun disklabel
   t   change a partition's system id
   u   change display/entry units
   v   verify the partition table
   w   write table to disk and exit
   x   extra functionality (experts only)

Command (m for help): d
Selected partition 1
Partition 1 is deleted

Command (m for help): m
Command action
   a   toggle a bootable flag
   b   edit bsd disklabel
   c   toggle the dos compatibility flag
   d   delete a partition
   g   create a new empty GPT partition table
   G   create an IRIX (SGI) partition table
   l   list known partition types
   m   print this menu
   n   add a new partition
   o   create a new empty DOS partition table
   p   print the partition table
   q   quit without saving changes
   s   create a new empty Sun disklabel
   t   change a partition's system id
   u   change display/entry units
   v   verify the partition table
   w   write table to disk and exit
   x   extra functionality (experts only)

Command (m for help): p

Disk /dev/vdb: 42.9 GB, 42949672960 bytes, 83886080 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x277fe012

   Device Boot      Start         End      Blocks   Id  System

Command (m for help): n
Partition type:
   p   primary (0 primary, 0 extended, 4 free)
   e   extended
Select (default p): 
Using default response p
Partition number (1-4, default 1): 
First sector (2048-83886079, default 2048): 
Using default value 2048
Last sector, +sectors or +size{K,M,G} (2048-83886079, default 83886079): 
Using default value 83886079
Partition 1 of type Linux and of size 40 GiB is set

Command (m for help): p

Disk /dev/vdb: 42.9 GB, 42949672960 bytes, 83886080 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x277fe012

   Device Boot      Start         End      Blocks   Id  System
/dev/vdb1            2048    83886079    41942016   83  Linux

Command (m for help): w
The partition table has been altered!

```

### 硬盘格式化

```shell
[root@iZwz9avipe1jdw1wbokzc5Z /]# lsblk -af
NAME   FSTYPE LABEL UUID                                 MOUNTPOINT
vda                                                      
└─vda1 ext4         976105f5-f402-456c-aadd-50de49ff88f9 /
vdb                                                      
└─vdb1                                                   
[root@iZwz9avipe1jdw1wbokzc5Z /]# fdisk -l

Disk /dev/vda: 107.4 GB, 107374182400 bytes, 209715200 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x000d2717

   Device Boot      Start         End      Blocks   Id  System
/dev/vda1   *        2048   209713151   104855552   83  Linux

Disk /dev/vdb: 42.9 GB, 42949672960 bytes, 83886080 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x277fe012

   Device Boot      Start         End      Blocks   Id  System
/dev/vdb1            2048    83886079    41942016   83  Linux
[root@iZwz9avipe1jdw1wbokzc5Z /]# lsblk -af
NAME   FSTYPE LABEL UUID                                 MOUNTPOINT
vda                                                      
└─vda1 ext4         976105f5-f402-456c-aadd-50de49ff88f9 /
vdb                                                      
└─vdb1 
[root@iZwz9avipe1jdw1wbokzc5Z /]# mkfs.xfs /dev/vdb1
meta-data=/dev/vdb1              isize=512    agcount=4, agsize=2621376 blks
         =                       sectsz=512   attr=2, projid32bit=1
         =                       crc=1        finobt=0, sparse=0
data     =                       bsize=4096   blocks=10485504, imaxpct=25
         =                       sunit=0      swidth=0 blks
naming   =version 2              bsize=4096   ascii-ci=0 ftype=1
log      =internal log           bsize=4096   blocks=5119, version=2
         =                       sectsz=512   sunit=0 blks, lazy-count=1
realtime =none                   extsz=4096   blocks=0, rtextents=0
[root@iZwz9avipe1jdw1wbokzc5Z /]# fdisk -l

Disk /dev/vda: 107.4 GB, 107374182400 bytes, 209715200 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x000d2717

   Device Boot      Start         End      Blocks   Id  System
/dev/vda1   *        2048   209713151   104855552   83  Linux

Disk /dev/vdb: 42.9 GB, 42949672960 bytes, 83886080 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x277fe012

   Device Boot      Start         End      Blocks   Id  System
/dev/vdb1            2048    83886079    41942016   83  Linux
[root@iZwz9avipe1jdw1wbokzc5Z /]# lsblk -af
NAME   FSTYPE LABEL UUID                                 MOUNTPOINT
vda                                                      
└─vda1 ext4         976105f5-f402-456c-aadd-50de49ff88f9 /
vdb                                                      
└─vdb1 xfs          f54afeb9-6b0f-443f-bd58-c4cfa44acfe0 


```

### 挂载硬盘

```shell
[root@iZwz9avipe1jdw1wbokzc5Z /]# mount /dev/vdb1 /disk/
[root@iZwz9avipe1jdw1wbokzc5Z /]# lsblk -af
NAME   FSTYPE LABEL UUID                                 MOUNTPOINT
vda                                                      
└─vda1 ext4         976105f5-f402-456c-aadd-50de49ff88f9 /
vdb                                                      
└─vdb1 xfs          f54afeb9-6b0f-443f-bd58-c4cfa44acfe0 /disk
[root@iZwz9avipe1jdw1wbokzc5Z /]# lsblk
NAME   MAJ:MIN RM  SIZE RO TYPE MOUNTPOINT
vda    253:0    0  100G  0 disk 
└─vda1 253:1    0  100G  0 part /
vdb    253:16   0   40G  0 disk 
└─vdb1 253:17   0   40G  0 part /disk
[root@iZwz9avipe1jdw1wbokzc5Z /]# fdisk -l

Disk /dev/vda: 107.4 GB, 107374182400 bytes, 209715200 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x000d2717

   Device Boot      Start         End      Blocks   Id  System
/dev/vda1   *        2048   209713151   104855552   83  Linux

Disk /dev/vdb: 42.9 GB, 42949672960 bytes, 83886080 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x277fe012

   Device Boot      Start         End      Blocks   Id  System
/dev/vdb1            2048    83886079    41942016   83  Linux
[root@iZwz9avipe1jdw1wbokzc5Z /]# lsblk -af
NAME   FSTYPE LABEL UUID                                 MOUNTPOINT
vda                                                      
└─vda1 ext4         976105f5-f402-456c-aadd-50de49ff88f9 /
vdb                                                      
└─vdb1 xfs          f54afeb9-6b0f-443f-bd58-c4cfa44acfe0 /disk

```

