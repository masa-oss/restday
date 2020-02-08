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
   
  
## Contribution

  

## Licence

[MIT]



