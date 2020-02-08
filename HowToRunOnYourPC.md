#  前提
#  Premise

- Windows10でのインストールについて記載します。

- Macは使った事がないので書けません。ごめんなさい。


- JavaとTomcatとAnt と MySQL が既にインストールされている事を前提とします。

-  コマンドプロンプトでAntが動く事
-  %TOMCAT_HOME%\bin\startup  で Tomcatが起動して http://localhost:8080/ でTomcatの画面が出る事

#  手順
#  procedure

- Gitから落としたソースを、どこか適当なディレクトリに置きます。 以下 %REST%　と記載します。（環境変数への設定は不要です）

- 今はTomcatは起動していないものとします。

- %REST%\tomcat\conf\Catalina\localhost\restday.xml を以下にコピーする
  %TOMCAT_HOME%\conf\Catalina\localhost\


- そのファイルを編集します。２行目と３行目に書いてあるディレクトリを %REST%に変更します。

- MySQLにデータベースを作成します。 DLL-my.txtを見てください。

- Create databaseはしてください。

- テスト用の Tableも作成してください

- CREATE USER restrest を実行する／しないは、好みですが、実行しておけば、次のプロパティファイルの設定が楽です。

- %REST%\webapps/WEB-INF/restrest.properties の   MySQLの接続先を設定します。

- %REST%\webapps/WEB-INF/classes/log4j2.xml    17行目、18行目のファイル位置を、ご自分のPC用に変更してください

- もしあなたのJavaがOpenJDK-11 でないなら、 minimal-json.jar を再コンパイルした方が良さそうです。

-  %REST%\webapps/WEB-INF/lib\minimal-json.jar  このファイルはOpenJDK-11でコンパイルしたものです

-  cd %REST%\ant.min
-  ant clean
-  ant

- いよいよ Restdayのコンパイルです。  cd %REST%\ant.run
-  ant clean
-  ant
-  これで、%REST%\webapps\WEB-INF\classes  にclass ファイルができます。

- 準備が整いましたので、tomcatを起動します。

- %TOMCAT_HOME%\bin\startup

- GoogleChromeとかで、 http://localhost:8080/restday/

- テスト用の画面が出れば、成功です。

- お疲れ様でした。










