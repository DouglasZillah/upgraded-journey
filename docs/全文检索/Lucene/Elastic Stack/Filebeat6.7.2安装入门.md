# Filebeat 6.7.2 安装入门


Elastic Stack 系列的工具升级得太快了， 根本学不动。趁着刚把公司公司的 ELK 搭起来， 还有点手感， 赶紧写个文档记录一下。


此文档的配置并未在生产环境运行， 请勿直接用于生产。


本次安装使用 `tar` 方式安装， 之后可能会测试一下 `yum` 的安装方式。


ELK 部分工具安装文档
* [Elasticsearch(单机)从安装到删库](Elasticsearch(单机)从安装到删库.md)
* [Logstash 6.7.2 安装入门](Logstash6.7.2安装入门.md)
* [Kibana 6.7.2 安装入门](Kibana6.7.2安装入门.md)


```shell
wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-6.7.2-linux-x86_64.tar.gz

```