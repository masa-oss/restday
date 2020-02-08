#  前提
#  Premise

- Windows10でのインストールについて記載します。
- This section describes installation on Windows 10.

- Macは使った事がないので書けません。ごめんなさい。
- I can't write on a Mac because I've never used it. sorry.


- JavaとTomcatとAnt と MySQL が既にインストールされている事を前提とします。
- It is assumed that Java, Tomcat, Ant and MySQL are already installed.

-  コマンドプロンプトでAntが動く事
-  Apache-Ant works at command prompt.

-  %TOMCAT_HOME%\bin\startup  で Tomcatが起動して http://localhost:8080/ でTomcatの画面が出る事
-  Tomcat starts at% TOMCAT_HOME%\bin\startup and Tomcat screen appears at http://localhost:8080/

#  手順
#  procedure

- Gitから落としたソースを、どこか適当なディレクトリに置きます。 以下 %REST%　と記載します。（環境変数への設定は不要です）
- Place the source dropped from Git in some suitable directory. It is described as% REST% below. (No need to set environment variables)

- 今はTomcatは起動していないものとします。
- Assume now that Tomcat is not running.

- %REST%\tomcat\conf\Catalina\localhost\restday.xml を以下にコピーする
  %TOMCAT_HOME%\conf\Catalina\localhost\
- Copy% REST% \ tomcat \ conf \ Catalina \ localhost \ restday.xml to the following
  % TOMCAT_HOME% \ conf \ Catalina \ localhost \


- そのファイルを編集します。２行目と３行目に書いてあるディレクトリを %REST%に変更します。
- Edit that file. Change the directories written on the second and third lines to% REST%.

- MySQLにデータベースを作成します。 DLL-my.txtを見てください。
- Create a database in MySQL. Look at DLL-my.txt.

- Create databaseはしてください。
- Please create database.

- テスト用の Tableも作成してください
- Also create a test table.

- CREATE USER restrest を実行する／しないは、あなたの好みですが、実行しておけば、次のプロパティファイルの設定が楽です。
- Whether or not to execute CREATE USER restrest is your preference, but if you do, the following property file settings are easier.

- %REST%\webapps/WEB-INF/restrest.properties の   MySQLの接続先を設定します。
- Set MySQL connection destination of% REST% \ webapps / WEB-INF / restrest.properties.

- %REST%\webapps/WEB-INF/classes/log4j2.xml    17行目、18行目のファイル位置を、ご自分のPC用に変更してください
- % REST% \ webapps / WEB-INF / classes / log4j2.xml Please change the file location on line 17 and 18 for your PC

- もしあなたのJavaがOpenJDK-11 でないなら、 minimal-json.jar を再コンパイルした方が良さそうです。
- If your Java is not OpenJDK-11, it seems better to recompile minimal-json.jar.

-  %REST%\webapps/WEB-INF/lib\minimal-json.jar  このファイルはOpenJDK-11でコンパイルしたものです
-  % REST%\webapps/WEB-INF/lib\minimal-json.jar.  This file is compiled with OpenJDK-11.

-  cd %REST%\ant.min
-  ant clean
-  ant

- いよいよ Restdayのコンパイルです。
- Finally, Restday compilation.

-  cd %REST%\ant.run
-  ant clean
-  ant

-  これで、%REST%\webapps\WEB-INF\classes  にclass ファイルができます。
-  You now have a class file in  %REST%\webapps\WEB-INF\classes.

- 準備が整いましたので、tomcatを起動します。
- Now you are ready, start tomcat.

- %TOMCAT_HOME%\bin\startup

- GoogleChromeとかで、 http://localhost:8080/restday/ にアクセスしてください
- Go to http://localhost:8080/restday/    with Google Chrome or other browser.

- テスト用の画面が出れば、成功です。
- If you see the test screen, you are successful.

- お疲れ様でした。
-  Thank you for your hard work.

