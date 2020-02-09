Restday
====

## Overview

Restday is a library for easily creating REST-SERVER to be installed in servlet environment like Tomcat.

## Description

-   Tomcat のようなサーブレット環境にインストールするライブラリです
-   Library to be installed in servlet environment like Tomcat.
   
-   RDB の操作を設定ファイルで記述します。
-   Describe the operation of RDB in the configuration file.

-   設定ファイルで実現出来ないロジックは、Javaでカスタマイズしてください。
-   Please customize with Java, when it cannot be realized with a configuration file.


-   但し、Javaカスタマイズ機能は、まだ限定的です。
-    However, the Java customization function is still limited.

-   省資源です
-   Small footprint.   

-   今のところMySQLのみで動きます
-   Currently only works with MySQL

## Demo

-   Here is an example configuration file

-  No1

```JSON
  "GET" : {
    "validate" : [ 
          { "pkey1" : [  "int" ] }
            ],
    "sql" : "select * from master1 where  master1_sqno = ?pkey1"
  },
```


    http://localhost:8080/restday/v1/master1/123


このライブラリは、pkey1=123とし、上記SQLを実行し、結果をJSONで返します

This library executes the above SQL with pkey1 = 123 and returns the result in JSON

---

- No2

```JSON
  "POST" : {
    "validate" : [ 
          { "bb" : [  "string"  ] },
          { "aa" : [  "int", "min" , 0, "max" , 100000 ] },
          { "remote_addr" : [  "string"  ] },
          { "current_time" : [  "Timestamp", "yyyy-MM-dd HH:mm:ss.S" ] }
            ],
    "sql" : "insert into master1(aa, bb, createdTime, createdIp) values (?aa, ?bb, ?current_time, ?remote_addr) "
  },
```

以下にPOSTし、BODYに、以下のJSONを入れたとします。
    http://localhost:8080/restday/v1/master1/


```JSON
{
  "aa" : 123,
  "bb" : "xyz"
}
```

このライブラリは、aa=123, bb="xyz", current_time=現在時刻, remote_addr=クライアントのIPアドレス、とし、上記SQLを実行します

---

## 複合主キーも利用可能です
## Composite primary key is also available

- No3


```SQL
create table twokey (
    twokey_comp  int,
    twokey_sqno  int,
    aa   integer default null,
    bb  varchar(50) default null,    
    tt   date default null,
    createdIp   varchar(50) default null,
    createdTime   timestamp null default null,
    updatedIp   varchar(50) default null,
    updatedTime   timestamp null default null,
   primary key( twokey_comp, twokey_sqno )
);
```

```JSON
  "GET" : {
    "validate" : [ 
          { "pkey1" : [  "int", "min" , 1, "max" , 1000 ] },
          { "pkey2" : [  "int", "min" , 1, "max" , 1000 ] }
            ],
    "sql" : "select * from twokey where  twokey_comp = ?pkey1 and twokey_sqno = ?pkey2 "
  },

```

    http://localhost:8080/restday/v1/twokey/111/222
    
    
- pkey1 = 111, pkey2 = 222 としてSQLを実行し、結果をJSONで返します。
- Execute SQL with pkey1 = 111, pkey2 = 222 and return the result as JSON.


---

## GETに条件を指定して、結果を絞ることも、多少可能です。
## It is somewhat possible to narrow down the results by specifying conditions for GET.

- No4

```JSON
  "GET" : {
    "validate" : [
            ],
    "where" : [ 
          { "from" : [  "Date" ] },
          { "to" :   [  "Date" ] }
            ],
    "sql" : "select * from user_t {w ?from <= first_visit  && first_visit <= ?to}"
  }
```

- {w から } に囲んだ部分は、パラメータに指定がある時に限りSQLに展開されます
- The part between {w and} is expanded to SQL only when specified in the parameter


    http://localhost:8080/restday/v1/users/?from=2019-06-01

- 上の例の場合、以下のSQLが実行されます。
-  In the above example, the following SQL is executed.

```SQL
select * from user_t where '2019-06-01' <= first_visit 
```

---

    http://localhost:8080/restday/v1/users/?to=2020-04-01

- 上の例の場合、以下のSQLが実行されます。
-  In the above example, the following SQL is executed.

```SQL
select * from user_t where  first_visit <= '2020-04-01'
```

---

    http://localhost:8080/restday/v1/users/?from=2019-06-01&to=2020-04-01

- 上の例の場合、以下のSQLが実行されます。
-  In the above example, the following SQL is executed.

```SQL
select * from user_t where  '2019-06-01' <= first_visit  && first_visit <= '2020-04-01'
```

---

- 今の所、and 条件でつなぐ事しかできません
- For now, you can only connect with and conditions

- 簡単なSQL構文でないと、上手く展開されません
- If it is not simple SQL syntax, it will not expand well


## Requirement

-   Java 
-   Apache Tomcat
-   Apache Ant
-   MySQL




## Dependent library

-   minimal-json   
-   log4j




## MyDevelop Environment

-  Windows 10
-  MySQL 5.7.19
-  OpenJdk 11 ( Amazon Corretto)
-  Apache Ant      1.10.7
-  Apache Tomcat   9.0.29
-  Apache NetBeans 11.2


## About minimal-json.jar   https://github.com/ralfstx/minimal-json/

-   私は、Java11を使っているが、もし別のjava (ex. java8)を使っているなら
   再コンパイルした方が良いでしょう。
   
-   I am using Java11 but if you are using another java (ex.java8)
   You may want to recompile.   
   
-   minimal-json の jarが見当たらなかったので、自分でコンパイルしています。
-   I did not find the minimal-json jar, so I compile it myself.


```Batchfile
    cd ant.min
    ant clean
    ant
```


## Install
  
  Look at HwoToRunOnYourPC.md
  
## Contribution

- 他のデータベースでも動くようにしたいです ( postgres )
- I want to work with other databases ( postgres )

- これに適した良い ORマッパー ライブラリがあれば、教えてください。それに変更したいです。
- If there is a good OR mapper library suitable for this, please let me know. I want to change it.

- MySQLのConnectionをクローズする時にExceptionが出るのを直したいです。誰か教えてください。
- I want to fix an Exception when closing MySQL Connection. Please tell someone.

## Licence

[MIT]



