# [Phase 0] 環境のセットアップと初期疎通確認

本フェーズでは、Spring Bootプロジェクトが正しく起動し、ブラウザからアクセスできる「土台」を構築することを目的とします。

## 1. 構成内容
新しく作成された `denko1_tracker` プロジェクトにおいて、以下の設定および実装を行いました。

### Spring Securityの一時無効化
開発初期段階において、ログイン画面（AutoConfiguration）による進行の妨げを防ぐため、セキュリティ設定を一時的に無効化しました。
* 方法: `SecurityConfig.java` を作成し、`HttpSecurity` で全てのパスを `permitAll()` に設定。

### H2 Databaseの設定
開発用インメモリデータベース（H2）の接続設定を `application.properties` に追加し、H2 Consoleを有効化しました。

### テスト用エンドポイントの作成
アプリケーションがHTTPリクエストを正しく処理できるか確認するため、シンプルな文字列を返す `HelloController` を作成しました。

## 2. 実装ファイル詳細
1. `src/main/resources/application.properties`: データベース設定、ポート番号設定 (8081)
2. `src/main/java/com/example/denko1_tracker/config/SecurityConfig.java`: セキュリティ無効化設定
3. `src/main/java/com/example/denko1_tracker/controller/HelloController.java`: 初期疎通確認用コントローラー

## 3. 動作確認項目
* ポート `8081` でアプリケーションが正常に起動すること。
* `http://localhost:8081/` にアクセスした際、ログイン画面にリダイレクトされずにメッセージが表示されること。
