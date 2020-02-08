#  IDEでソースを開く方法について
#

-  私はOpenJDK11( Amazon Corretto) と NetBeans-11.2 で、これを書いています。少数派かもしれませんね（笑）

-  もしあなたがNetBeansを使っているならば、Gitからダウンロードしたファイルを、そのまま開けば、編集できるでしょう。

-  私は、念のため確認してみましたが、なぜか、JunitのテストのJavaだけ、エラーマークが出ました。理由はよくわかりません。

-  でも、Junitは、問題なく実行できました。

-  私は、ソースを書くことと、Junitを動かす事のみに、NetBeansを使っています。

-  NetBeansからTomcatを起動したり、連携機能は使っておりません。

- ビルドはantで行い、tomcatはコマンドプロンプトから起動するのが、私のスタイルです。


---

#  eclipseとかInteliJとか、他のIDEを使いたい方への参考

- 配布ファイルの直下にあるbuild.xmlは、NetBeansが生成した（NetBeansが使う）ビルドファイルです。
- eclipseとかInteliJ を使う人には、不要なファイルです。

- 同様に、nbprojectのディレクトリ以下のファイルもNetBeansが生成した（NetBeansが使う）ビルドファイルです。
- あなたにとっては、不要なファイルです。消しても良いでしょう。


- フォルダー構成は以下のようになっています。あなたのIDEに設定してみてください。

- src 。  これは、Restdayの本体です。

- src2 。 これは、JSONライブラリのソースです。

- src3 。 カスタマイズする際の、サンプルクラスです。

- srct 。 これは、JUnitを動かすためのモックアップです。

- test 。  JUNITのソースです。


# 設定が、必要なJarファイルは以下のとおりです

-  lib/servlet-api.jar
-  webapps/WEB-INF/lib/log4j-api-2.11.1.jar

# コンパイルテスト時

-  JUnit 4.12
-  Hamcrest 1.3
-  これらは NetBeansに含まれているので、githubには、上げていません。

# テスト実行時

-  webapps/WEB-INF/lib/log4j-api-2.11.1.jar
-  webapps/WEB-INF/lib/log4j-core-2.11.1.jar


# 注意事項

- IDEでJunitを実行する際は、srcの下にある log4j2.xmlが使われる筈です。

-  17行目、18行目あたりに、ログファイルの書き込み先の設定がありますので、あなたのPCの環境に合わせてください。



