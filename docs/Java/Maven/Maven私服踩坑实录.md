# Maven私服踩坑录


辛辛苦苦干了几个月，项目终于要发布第一个版本了，感觉自己牛逼哄哄的啊。


推送 `git`，打好 `tag`， 配置 `Jenkins`，立即构建，`Build Success`，老子横空出世。


打开 `Xshell`，连接服务器，


打开 `postman`，写好参数，Send！


```shell
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'jacksonUtil': Lookup method resolution failed; nested exception is java.lang.IllegalStateException: Failed to introspect Class [com.nsw.report.util.baidutongji.JacksonUtil] from ClassLoader [org.springframework.boot.loader.LaunchedURLClassLoader@20ad9418]
        at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.determineCandidateConstructors(AutowiredAnnotationBeanPostProcessor.java:265) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.determineConstructorsFromBeanPostProcessors(AbstractAutowireCapableBeanFactory.java:1236) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1151) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:538) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:498) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:320) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:318) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:846) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:863) ~[spring-context-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:546) ~[spring-context-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:142) ~[spring-boot-2.1.1.RELEASE.jar!/:2.1.1.RELEASE]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:775) [spring-boot-2.1.1.RELEASE.jar!/:2.1.1.RELEASE]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:397) [spring-boot-2.1.1.RELEASE.jar!/:2.1.1.RELEASE]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:316) [spring-boot-2.1.1.RELEASE.jar!/:2.1.1.RELEASE]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1260) [spring-boot-2.1.1.RELEASE.jar!/:2.1.1.RELEASE]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1248) [spring-boot-2.1.1.RELEASE.jar!/:2.1.1.RELEASE]
        at com.nsw.report.ReportServiceApplication.main(ReportServiceApplication.java:14) [classes!/:na]
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_101]
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_101]
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_101]
        at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_101]
        at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:48) [flow-report-1.1-SNAPSHOT.jar:na]
        at org.springframework.boot.loader.Launcher.launch(Launcher.java:87) [flow-report-1.1-SNAPSHOT.jar:na]
        at org.springframework.boot.loader.Launcher.launch(Launcher.java:50) [flow-report-1.1-SNAPSHOT.jar:na]
        at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:51) [flow-report-1.1-SNAPSHOT.jar:na]
Caused by: java.lang.IllegalStateException: Failed to introspect Class [com.nsw.report.util.baidutongji.JacksonUtil] from ClassLoader [org.springframework.boot.loader.LaunchedURLClassLoader@20ad9418]
        at org.springframework.util.ReflectionUtils.getDeclaredMethods(ReflectionUtils.java:686) ~[spring-core-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.util.ReflectionUtils.doWithMethods(ReflectionUtils.java:583) ~[spring-core-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.util.ReflectionUtils.doWithMethods(ReflectionUtils.java:568) ~[spring-core-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.determineCandidateConstructors(AutowiredAnnotationBeanPostProcessor.java:248) ~[spring-beans-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        ... 26 common frames omitted
Caused by: java.lang.NoClassDefFoundError: org/codehaus/jackson/JsonGenerationException
        at java.lang.Class.getDeclaredMethods0(Native Method) ~[na:1.8.0_101]
        at java.lang.Class.privateGetDeclaredMethods(Class.java:2701) ~[na:1.8.0_101]
        at java.lang.Class.getDeclaredMethods(Class.java:1975) ~[na:1.8.0_101]
        at org.springframework.util.ReflectionUtils.getDeclaredMethods(ReflectionUtils.java:668) ~[spring-core-5.1.3.RELEASE.jar!/:5.1.3.RELEASE]
        ... 29 common frames omitted
Caused by: java.lang.ClassNotFoundException: org.codehaus.jackson.JsonGenerationException
        at java.net.URLClassLoader.findClass(URLClassLoader.java:381) ~[na:1.8.0_101]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:424) ~[na:1.8.0_101]
        at org.springframework.boot.loader.LaunchedURLClassLoader.loadClass(LaunchedURLClassLoader.java:93) ~[flow-report-1.1-SNAPSHOT.jar:na]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:357) ~[na:1.8.0_101]
        ... 33 common frames omitted

```


Holy****!


什么鬼, 我本机跑得好好的怎么服务器一部署就挂了?


运维老哥你这环境有问题吧= =


对面的同事投过来一个鄙夷的眼神: 小老弟你是没配置 Maven 私服吧。


想了一下似乎是这样, 之前 `Jenkins` 打包失败过, 当时并没有多想，直接传了个本地 `JAR` 到项目根目录, 然后直接打包运行, 如果说只是本地应付一下也就算了, 实际上并不规范, 而且容易引发潜在的问题。


那么实际上应该是怎么做呢?


首先, 我们要配置本机的 Maven, 把镜像指向公司的 Maven 仓库。


```xml
<?xml version="1.0" encoding="UTF-8"?>

<!--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

<!--
 | This is the configuration file for Maven. It can be specified at two levels:
 |
 |  1. User Level. This settings.xml file provides configuration for a single user,
 |                 and is normally provided in ${user.home}/.m2/settings.xml.
 |
 |                 NOTE: This location can be overridden with the CLI option:
 |
 |                 -s /path/to/user/settings.xml
 |
 |  2. Global Level. This settings.xml file provides configuration for all Maven
 |                 users on a machine (assuming they're all using the same Maven
 |                 installation). It's normally provided in
 |                 ${maven.home}/conf/settings.xml.
 |
 |                 NOTE: This location can be overridden with the CLI option:
 |
 |                 -gs /path/to/global/settings.xml
 |
 | The sections in this sample file are intended to give you a running start at
 | getting the most out of your Maven installation. Where appropriate, the default
 | values (values used when the setting is not specified) are provided.
 |
 |-->
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <!-- localRepository
   | The path to the local repository maven will use to store artifacts.
   |
   | Default: ${user.home}/.m2/repository
  <localRepository>/path/to/local/repo</localRepository>
  -->

    
    <localRepository>${user.home}/.m2/repository</localRepository>      
    
		  
    <servers>
        <server>
            <id>center</id>
            <username>root</username>
            <password>root</password>
        </server>
    </servers>

    <mirrors>
        <!-- osc镜像 -->
      <!--   <mirror>
            镜像所有远程仓库，但不包括指定的仓库
            <id>mirror-osc</id>
            <mirrorOf>external:*,!repo-osc-thirdparty,!repo-nsw</mirrorOf>
            <url</url>
        </mirror>  -->

		<mirror>
			<id>center</id>
			<mirrorOf>*</mirrorOf>
			<url>http://ip:port/content/groups/public/</url>
		</mirror>
    </mirrors>

    <profiles>
        <profile>
            <id>dev</id>
			<repositories>
				<repository>
					<id>nexus</id>
					<url>http://ip:port/nexus/content/groups/public</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>true</enabled>
						<updatePolicy>always</updatePolicy>
						<checksumPolicy>warn</checksumPolicy>
					</snapshots>
				</repository>
			</repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>center</id>
                    <url>http://ip:port/nexus/content/groups/public</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                        <updatePolicy>always</updatePolicy>
                        <checksumPolicy>warn</checksumPolicy>
                    </snapshots>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles>

    <activeProfiles>
        <activeProfile>dev</activeProfile>
        <!--<activeProfile>profile-nsw</activeProfile>-->
    </activeProfiles>
	
</settings>

```


这样拉取依赖的时候如果私服没有, 就会从中央仓库中拉取一份到私服仓库, 然后再从私服中拉取到本地。


## 另一种方案


看到网上还有说从本地上传的。


参考方法: [上传jar包到nexus私服 - 蛙牛的个人页面 - 开源中国](https://my.oschina.net/lujianing/blog/297128)


作为一个备选方案。


## 方案三


通过命令的方式上传:
```shell
mvn deploy:deploy-file
    -Dmaven.test.skip=true
    -Dfile=C:\files\jackson-all-1.8.5.jar
    -DgroupId=org.codehaus.jackson
    -DartifactId=jackson-all
    -Dversion=1.8.5
    -Dpackaging=jar
    -DrepositoryId=center
    -Durl=http://ip:port/nexus/content/groups/public/
```
用这种方式需要注意, `-Dfile` 不能指向本地仓库, 否则会报错