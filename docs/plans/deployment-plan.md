# [Deployment Plan] 本番環境へのデプロイ

Denko1-Tracker を Render などのクラウド環境で公開するための実行計画です。

## 1. 構成内容

### データベースの移行 (H2 -> PostgreSQL)
本番環境では、データが永続化される PostgreSQL を使用します。
* `pom.xml` に PostgreSQL ドライバを追加。
* `application-prod.properties` を作成し、環境変数経由で接続情報を取得するように設定。

### コンテナ化 (Docker)
環境を問わず実行可能な Docker イメージを作成します。
* `Dockerfile` の作成：OpenJDK 21 をベースにしたビルド・実行用ファイル。

### デプロイ設定 (Infrastructure as Code)
Render の構成をコードで管理します。
* `render.yaml` の作成：Web Service (Spring Boot) と Database (PostgreSQL) の依存関係を定義。

## 2. 実装ファイル詳細
1. `pom.xml`: 依存関係の追加
2. `Dockerfile`: イメージビルド定義
3. `render.yaml`: Render 構成定義
4. `src/main/resources/application-prod.properties`: 本番用環境設定

## 3. 必要なユーザー操作
1. **GitHub へのプッシュ**: 本プロジェクトを GitHub リポジトリに公開してください。
2. **Render アカウントの作成**: [Render](https://render.com/) でアカウントを作成し、GitHub と連携してください。
3. **デプロイの実行**: Render ダッシュボードから本リポジトリを選択すると、`render.yaml` に基づいて自動的にデプロイが開始されます。

## 4. 動作確認項目
* デプロイ完了後、Render から付与される `https://xxx.onrender.com` にアクセスし、正常に動作すること。
