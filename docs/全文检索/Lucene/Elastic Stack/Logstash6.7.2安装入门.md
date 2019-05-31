# Logstash 6.7.2 安装入门


Elastic Stack 系列的工具升级得太快了， 根本学不动。趁着刚把公司公司的 ELK 搭起来， 还有点手感， 赶紧写个文档记录一下。


此文档的配置并未在生产环境运行， 请勿直接用于生产。


ELK 部分工具安装文档
* [Elasticsearch(单机)从安装到删库](Elasticsearch(单机)从安装到删库.md)
* [Filebeat 6.7.2 安装入门](Filebeat6.7.2安装入门.md)
* [Kibana 6.7.2 安装入门](Kibana6.7.2安装入门.md)


```shell
wget -P /usr/local/ https://artifacts.elastic.co/downloads/logstash/logstash-6.7.2.tar.gz
tar zxf /usr/local/logstash-6.7.2.tar.gz
cp /usr/local/logstash-6.7.2/config/logstash-sample.conf /usr/local/logstash-6.7.2/config/logstash.conf
vim /usr/local/logstash-6.7.2/config/logstash.conf
# 修改一下 logstash 
```