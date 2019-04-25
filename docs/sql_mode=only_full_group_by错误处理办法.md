# ***sql_mode=only_full_group_by错误处理办法***

### 一:修正版(不影响数据库最佳方法)

1:5.7官方推荐利用`ANY_VALUE()`这个函数

##### MYSQL 5.7 官方文档:

##### https://dev.mysql.com/doc/refman/5.7/en/miscellaneous-functions.html#function_any-value

SQL示例: (原理:把非聚合的列变成聚合)

```
SELECT 
ANY_VALUE(id)as id,
ANY_VALUE(uid) as uid ,
ANY_VALUE(username) as username,ANY_VALUE(title) as title,
ANY_VALUE(author) as author,
ANY_VALUE(thumb) as thumb,
ANY_VALUE(description) as description,
ANY_VALUE(content) as content,
ANY_VALUE(linkurl) as linkurl,ANY_VALUE(url) as url,
ANY_VALUE(group_id) as group_id,ANY_VALUE(inputtime) as inputtime, 
count(id) as count 
FROM `news` 
GROUP BY `group_id` 
ORDER BY ANY_VALUE(inputtime） 
DESC LIMIT 20
```

2:不去ONLY_FULL_GROUP_BY, 时 select字段必须都在group by分组条件内（含有函数的字段除外）。（如果遇到order by也出现这个问题，同理，order by字段也都要在group by内）。

### 二:改MYSQL 5.7 SQL_MODE约束

### 参考文章https://blog.csdn.net/yalishadaa/article/details/72861737

某些使用group by的sql语句会因为sql_mode=only full group by问题导致错误，所以需要修改MySQL数据库配置

由于参考文章所说的my.ini和my-default.ini并没有找到，所以采用了第二种办法，修改sql_mode

1）连接数据库

```bash
mysql -hlocalhost -uroot -p
```

2）输入密码

3）查看数据库版本：

```mysql
SELECT VERSION();
```

4）查看GLOBAL .sql_mode配置：SELECT @@GLOBAL.sql_mode;

```mysql
+----------------------------------------------------------------------------------------------+
|                                   @@GLOBAL.sql_mode                                          |
+----------------------------------------------------------------------------------------------+
|ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION |
+----------------------------------------------------------------------------------------------+
1 row in set (0.00 sec)

```

5）查看SESSION .sql_mode配置：SELECT @@ SESSION.sql_mode;

```mysql
+----------------------------------------------------------------------------------------------+
|                                   @@GLOBAL.sql_mode                                          |
+----------------------------------------------------------------------------------------------+
|ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION |
+----------------------------------------------------------------------------------------------+
1 row in set (0.00 sec)

```

6）修改sql_mode配置：

```mysql
SET sql_mode = 
‘STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION’;
```

7）修改GLOBAL .sql_mode配置：

```mysql
SET @@GLOBAL.sql_mode = 
‘STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION’;
```

