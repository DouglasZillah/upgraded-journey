# Nginx 一键安装脚本(附带部分官方文档说明)


本脚本参考 [Nginx官方文档 - installing-nginx-open-source](https://docs.nginx.com/nginx/admin-guide/installing-nginx/installing-nginx-open-source/)


参考了一些别人的安装教程


* 注册成为系统服务部分参考 [CentOS7使用systemctl添加自定义服务](https://www.jianshu.com/p/79059b06a121)
* Nginx其他模块参考  [nginx服务器安装及配置文件详解](https://segmentfault.com/a/1190000002797601)


脚本包含:
* `prebuilt` 包的 `yum` 安装
* `source` 包的编译安装
  * 通过 `wget`, `make`, `make install` 安装编译依赖
  * 通过 `yum` 升级编译依赖
* 包含自己常用的 `Nginx module`: `core`, `http_ssl_module`, `stream`, `stream_ssl_module`, `mail`, `mail_ssl`, `file-aio`, `http_v2_module`, `threads`
* 每一个步骤的相关注释
* 其他 `module` 功能的相关注释


编译 `Nginx` 需要的相关依赖: `pcre`, `zlib`, `openssl`


```shell

#!/bin/bash

# 参考 https://docs.nginx.com/nginx/admin-guide/installing-nginx/installing-nginx-open-source/
# 注册成为系统服务部分参考 https://www.jianshu.com/p/79059b06a121

echo -e "install prebuilt nginx from yum?[y/n]: \c"

read _INSTALL_FROM_YUM

if [[ "y" == "${_INSTALL_FROM_YUM}" ]]; then

    # Install the prerequisites:
    # 安装依赖
    yum -y install yum-utils

    # To set up the yum repository, create the file named /etc/yum.repos.d/nginx.repo with the following contents:
    # 要设置yum存储库, 请使用以下内容创建名为/etc/yum.repos.d/nginx.repo的文件:
    touch /etc/yum.repos.d/nginx.repo
    echo '[nginx-stable]' >> /etc/yum.repos.d/nginx.repo
    echo 'name=nginx stable repo' >> /etc/yum.repos.d/nginx.repo
    echo 'baseurl=http://nginx.org/packages/centos/$releasever/$basearch/' >> /etc/yum.repos.d/nginx.repo
    echo 'gpgcheck=1' >> /etc/yum.repos.d/nginx.repo
    echo 'enabled=1' >> /etc/yum.repos.d/nginx.repo
    echo 'gpgkey=https://nginx.org/keys/nginx_signing.key' >> /etc/yum.repos.d/nginx.repo
    echo '' >> /etc/yum.repos.d/nginx.repo
    echo '[nginx-mainline]' >> /etc/yum.repos.d/nginx.repo
    echo 'name=nginx mainline repo' >> /etc/yum.repos.d/nginx.repo
    echo 'baseurl=http://nginx.org/packages/mainline/centos/$releasever/$basearch/' >> /etc/yum.repos.d/nginx.repo
    echo 'gpgcheck=1' >> /etc/yum.repos.d/nginx.repo
    echo 'enabled=0' >> /etc/yum.repos.d/nginx.repo
    echo 'gpgkey=https://nginx.org/keys/nginx_signing.key' >> /etc/yum.repos.d/nginx.repo

    # To install nginx, run the following command:
    # 要安装nginx, 请运行以下命令:
    yum install nginx

    exit
fi


echo -e "make nginx as a system service(register to systemctl)?[y/n]: \c"

read _AS_A_SYSTEM_SERVICE

echo -e "need update PCRE, ZLIB, OPENSSL packages by yum?[y/n]: \c"

read _UPDATE_BY_YUM_FLAG

wget https://nginx.org/download/nginx-1.14.2.tar.gz

tar zxf nginx-1.14.2.tar.gz

mv nginx-1.14.2 nginx-1.14.2-src

cd nginx-1.14.2-src/

# ======================== 可选, 如果系统已有则不必安装 ========================


if [[ "y" == "${_UPDATE_BY_YUM_FLAG}" ]];then
    yum -y install make gcc install yum-utils pcre pcre-devel zlib zlib-devel openssl openssl-devel
else
    # To install PCRE – Supports regular expressions. Required by the NGINX Core and Rewrite modules.
    # 安装 PCRE – NGINX Core 和 Rewrite 模块需要
    wget ftp://ftp.csx.cam.ac.uk/pub/software/programming/pcre/pcre-8.42.tar.gz
    tar -zxf pcre-8.42.tar.gz
    cd pcre-8.42
    ./configure
    make
    make install
    cd ../

    # To install zlib – Supports header compression. Required by the NGINX Gzip module.
    # 安装 zlib – Gzip 模块需要
    wget http://zlib.net/zlib-1.2.11.tar.gz
    tar -zxf zlib-1.2.11.tar.gz
    cd zlib-1.2.11
    ./configure
    make
    make install
    cd ../

    # To install OpenSSL – Supports the HTTPS protocol. Required by the NGINX SSL module and others.
    # 安装 OpenSSL 模块 – SSL模块和其他模块需要
    wget http://www.openssl.org/source/openssl-1.1.1b.tar.gz
    tar -zxf openssl-1.1.1b.tar.gz
    mv openssl-1.1.1b openssl-1.1.1b-src
    mkdir openssl-1.1.1b
    cd openssl-1.1.1b-src
    # 直接使用 nginx 官方文档给出的编译代码在这里会报错
    # darwin 是 OSX 系统的架构, 在 CentOS 等 Linux/Unix 中是 linux-x86_64, 具体可以调用 ./Configure LIST命令查看
    # ./Configure darwin64-x86_64-cc --prefix=/usr
    ./Configure linux-x86_64 --prefix=$PWD
    make
    make install
    cd ../

fi

# ==================================== End =====================================

# ./configure 常用配置

# 基础编译配置
# --prefix=$PWD                           Directory for NGINX files, and the base location for all relative paths set by the other configure script options (excluding paths to libraries) and for the path to the nginx.conf configuration file. Default: /usr/local/nginx.
#                                         NGINX文件的目录, 以及由其他configure脚本选项（不包括库的路径）设置的所有相对路径的基本位置以及nginx.conf配置文件的路径。默认值: /usr/local/nginx。
# --sbin-path=$PWD                        Name of the NGINX executable file, which is used only during installation. Default: <prefix>/sbin/nginx.
#                                         NGINX可执行文件的名称, 仅在安装期间使用。默认值: <prefix> / sbin / nginx
# --conf-path=$PWD/nginx.conf             Name of the NGINX configuration file. You can, however, always override this value at startup by specifying a different file with the -c <FILENAME> option on the nginx command line. Default: <prefix>conf/nginx.conf.
#                                         NGINX配置文件的名称。但是, 您可以通过在nginx命令行上使用-c <FILENAME>选项指定其他文件来始终在启动时覆盖此值。默认值: <prefix>conf/nginx.conf
# --pid-path=$PWD/nginx.pid               Name of the nginx.pid file, which stores the process ID of the nginx master process. After installation, the path to the filename can be changed with the pid directive in the NGINX configuration file. Default: <prefix>/logs/nginx.pid.
#                                         nginx.pid文件的名称, 用于存储nginx主进程的进程ID。安装后, 可以使用NGINX配置文件中的pid指令更改文件名的路径。默认值: <prefix>/logs/nginx.pid
# --error-log-path=$PWD/logs/error.log    Name of the primary log file for errors, warnings, and diagnostic data. After installation, the filename can be changed with the the error_log directive in the NGINX configuration file. Default: <prefix>/logs/error.log.
#                                         错误, 警告和诊断数据的主日志文件的名称。安装后, 可以使用NGINX配置文件中的error_log指令更改文件名。默认值: <prefix>/logs/error.log
# --http-log-path=$PWD/logs/access.log    Name of the primary log file for requests to the HTTP server. After installation, the filename can always be changed with the access_log directive in the NGINX configuration file. Default: <prefix>/logs/access.log.
#                                         HTTP服务器请求的主日志文件的名称。安装后, 始终可以使用NGINX配置文件中的access_log指令更改文件名。默认值: <prefix>/logs/access.log
# --with-pcre=$PWD/pcre-8.42              Path to the source for the PCRE library, which is required for regular expressions support in the location directive and the Rewrite module.
#                                         PCRE库源代码的路径, 这是位置指令和Rewrite模块中正则表达式支持所必需的。
# --with-pcre-jit                         Builds the PCRE library with “just-in-time compilation” support (the pcre_jit directive).
#                                         使用“即时编译”支持（pcre_jit指令）构建PCRE库。
# --with-zlib=$PWD/zlib-1.2.11            Path to the source for the zlib library, which is required by the Gzip module.
#                                         zlib库的源代码路径, Gzip模块需要该路径。

# 默认编译的模块
#
# http_access_module                      Accepts or denies requests from specified client addresses.
# http_auth_basic_module                  Limits access to resources by validating the user name and password using the HTTP Basic Authentication protocol.
# http_autoindex_module                   Processes requests ending with the forward-slash character (/) and produces a directory listing.
# http_browser_module                     Creates variables whose values depend on the value of the User-Agent request header.
# http_charset_module                     Adds the specified character set to the Content-Type response header. Can convert data from one character set to another.
# http_empty_gif_module                   Emits a single-pixel transparent GIF.
# http_fastcgi_module                     Passes requests to a FastCGI server.
# http_geo_module                         Creates variables with values that depend on the client IP address.
# http_gzip_module                        Compresses responses using gzip, reducing the amount of transmitted data by half or more.
# http_limit_conn_module                  Limits the number of connections per a defined key, in particular, the number of connections from a single IP address.
# http_limit_req_module                   Limits the request processing rate per a defined key, in particular, the processing rate of requests coming from a single IP address.
# http_map_module                         Creates variables whose values depend on the values of other variables.
# http_memcached_module                   Passes requests to a memcached server.
# http_proxy_module                       Passes HTTP requests to another server.
# http_referer_module                     Blocks requests with invalid values in the Referer header.
# http_rewrite_module                     Changes the request URI using regular expressions and return redirects; conditionally selects configurations. Requires the PCRE library.
# http_scgi_module                        Passes requests to an SCGI server.
# http_ssi_module                         Processes SSI (Server Side Includes) commands in responses passing through it.
# http_split_clients_module               Creates variables suitable for A/B testing, also known as split testing.
# http_upstream_hash_module               Enables the generic Hash load-balancing method.
# http_upstream_ip_hash_module            Enables the IP Hash load-balancing method.
# http_upstream_keepalive_module          Enables keepalive connections.
# http_upstream_least_conn_module         Enables the Least Connections load-balancing method.
# http_upstream_zone_module               Enables shared memory zones.
# http_userid_module                      Sets cookies suitable for client identification.
# http_uwsgi_module                       Passes requests to a uwsgi server.

# 需要手动启动编译的模块

# --with-http_ssl_module                  Enables HTTPS support. Requires an SSL library such as OpenSSL.
#                                         启用HTTPS支持。需要一个SSL库, 如OpenSSL。
# --with-stream                           Enables the TCP and UDP proxy functionality. To compile as a separate dynamic module instead, change the option to --with-stream=dynamic.
#                                         启用TCP和UDP代理功能。要编译为单独的动态模块, 请将选项更改为--with-stream = dynamic。
# --with-stream_ssl_module                Provides support for a stream proxy server to work with the SSL/TLS protocol. Requires an SSL library such as OpenSSL.
#                                         为流代理服务器提供支持以使用SSL/TLS协议。需要一个SSL库, 如OpenSSL。
# --with-mail                             Enables mail proxy functionality. To compile as a separate dynamic module instead, change the option to --with-mail=dynamic.
#                                         启用邮件代理功能。要编译为单独的动态模块, 请将选项更改为--with-mail = dynamic。
# --with-mail_ssl_module                  Provides support for a mail proxy server to work with the SSL/TLS protocol. Requires an SSL library such as OpenSSL.
#                                         为邮件代理服务器提供支持以使用SSL/TLS协议。需要一个SSL库, 如OpenSSL。
# --with-file-aio                         Enables asynchronous I/O.
#                                         启用异步I/O.
# --with-http_v2_module                   Enable support for HTTP/2. See The HTTP/2 Module in NGINX on the NGINX blog for details.
#                                         启用对HTTP / 2的支持。
#  --with-threads                         Enables NGINX to use thread pools. For details, see Thread Pools in NGINX Boost Performance 9x! on the NGINX blog.
#                                         使NGINX能够使用线程池。

mkdir ../nginx-1.14.2/

_BASE_DIR="$PWD"

_BASE_DIR="${_BASE_DIR:0:( ${#_BASE_DIR} - 4 )}"

if ["y" == "${_UPDATE_BY_YUM_FLAG}" ]; then
    ./configure \
    --prefix=${_BASE_DIR} \
    --with-http_ssl_module \
    --with-stream \
    --with-stream_ssl_module \
    --with-mail \
    --with-mail_ssl_module \
    --with-file-aio \
    --with-http_v2_module \
    --with-threads
else
    ./configure \
    --prefix=${_BASE_DIR} \
    --with-pcre=$PWD/pcre-8.42 \
    --with-zlib=$PWD/zlib-1.2.11 \
    --with-openssl=$PWD/openssl-1.1.1b-src \
    --with-http_ssl_module \
    --with-stream \
    --with-stream_ssl_module \
    --with-mail \
    --with-mail_ssl_module \
    --with-file-aio \
    --with-http_v2_module \
    --with-threads
fi

make

make install

if [[ "y" == "${_AS_A_SYSTEM_SERVICE}" ]]; then
    # systemctl脚本目录: /usr/lib/systemd/
    # 系统服务目录: /usr/lib/systemd/system/
    # 用户服务目录: /usr/lib/systemd/user/
    # [Unit] 区块: 启动顺序与依赖关系

    # Description字段: 给出当前服务的简单描述。
    # Documentation字段: 给出文档位置。

    # After字段: 如果network.target或sshd-keygen.service需要启动, 那么sshd.service应该在它们之后启动。
    # Before字段: 定义sshd.service应该在哪些服务之前启动。
    # After和Before字段只涉及启动顺序, 不涉及依赖关系。

    # Wants字段: 表示sshd.service与sshd-keygen.service之间存在"弱依赖"关系, 即如果"sshd-keygen.service"启动失败或停止运行, 不影响sshd.service继续执行。
    # Requires字段则表示"强依赖"关系, 即如果该服务启动失败或异常退出, 那么sshd.service也必须退出。
    # Wants字段与Requires字段只涉及依赖关系, 与启动顺序无关, 默认情况下是同时启动的。

    # [Service] 区块: 启动行为, 区块中的启动、重启、停止命令全部要求使用绝对路径

    # Type字段定义启动类型。它可以设置的值如下: 
    # simple（默认值）: ExecStart字段启动的进程为主进程
    # forking: ExecStart字段将以fork()方式启动, 此时父进程将会退出, 子进程将成为主进程（后台运行）
    # oneshot: 类似于simple, 但只执行一次, Systemd 会等它执行完, 才启动其他服务
    # dbus: 类似于simple, 但会等待 D-Bus 信号后启动
    # notify: 类似于simple, 启动结束后会发出通知信号, 然后 Systemd 再启动其他服务
    # idle: 类似于simple, 但是要等到其他任务都执行完, 才会启动该服务。一种使用场合是为让该服务的输出, 不与其他服务的输出相混合

    # ExecStart字段: 定义启动进程时执行的命令
    # ExecReload字段: 重启服务时执行的命令
    # ExecStop字段: 停止服务时执行的命令
    # ExecStartPre字段: 启动服务之前执行的命令
    # ExecStartPost字段: 启动服务之后执行的命令
    # ExecStopPost字段: 停止服务之后执行的命令

    # [Install] 区块: 定义如何安装这个配置文件, 即怎样做到开机启动
    # WantedBy字段: 表示该服务所在的 Target
    # WantedBy=multi-user.target指的是: sshd 所在的 Target 是multi-user.target

    # Target的含义是服务组, 表示一组服务
    # 这个设置非常重要, 因为执行systemctl enable sshd.service命令时, sshd.service的一个符号链接, 就会放在/etc/systemd/system目录下面的multi-user.target.wants子目录之中
    # 一般来说, 常用的 Target 有两个:
    # multi-user.target: 表示多用户命令行状态;
    # graphical.target: 表示图形用户状态, 它依赖于multi-user.target。
    # Systemd 有默认的启动 Target: 默认的启动 Target 是multi-user.target
    # 使用 Target 的时候, systemctl list-dependencies命令和systemctl isolate命令也很有用。
    # 查看 multi-user.target 包含的所有服务: systemctl list-dependencies multi-user.target
    # 切换到另一个 target: systemctl isolate shutdown.target (shutdown.target 就是关机状态)

    touch /usr/lib/systemd/system/nginx.service

    echo "[Unit]" >> /usr/lib/systemd/system/nginx.service
    echo "Description=nginx - high performance web server" >> /usr/lib/systemd/system/nginx.service
    echo "Documentation=http://nginx.org/en/docs/" >> /usr/lib/systemd/system/nginx.service
    echo "After=network-online.target remote-fs.target nss-lookup.target" >> /usr/lib/systemd/system/nginx.service
    echo "Wants=network-online.target" >> /usr/lib/systemd/system/nginx.service
    echo "" >> /usr/lib/systemd/system/nginx.service

    echo "[Service]" >> /usr/lib/systemd/system/nginx.service
    echo "Type=forking" >> /usr/lib/systemd/system/nginx.service
    echo "PIDFile=${_BASE_DIR}/nginx.pid" >> /usr/lib/systemd/system/nginx.service
    echo "ExecStart=${_BASE_DIR}/sbin/nginx -c ${_BASE_DIR}/nginx.conf" >> /usr/lib/systemd/system/nginx.service
    echo "ExecReload=${_BASE_DIR}/sbin/nginx -s reload" >> /usr/lib/systemd/system/nginx.service
    echo "ExecStop=${_BASE_DIR}/sbin/nginx -s quit" >> /usr/lib/systemd/system/nginx.service
    # echo "ExecKill=/bin/kill -s TERM $MAINPID" >> /usr/lib/systemd/system/nginx.service
    echo "" >> /usr/lib/systemd/system/nginx.service

    echo "[Install]" >> /usr/lib/systemd/system/nginx.service
    echo "WantedBy=multi-user.target" >> /usr/lib/systemd/system/nginx.service
    systemctl daemon-reload
fi
```