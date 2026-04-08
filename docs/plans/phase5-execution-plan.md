# Phase 5 実行計画：デプロイと運用性拡張 [完了]

本フェーズでは、ローカル開発環境から本番環境（クラウド）への移行と、実際の試験勉強における「使い勝手」と「データ保護」を重視した拡張を行いました。

## 1. インフラ構成の構築
*   **プラットフォーム**: Render (Web Service + Managed PostgreSQL)
*   **データベース移行**: H2 Database から PostgreSQL への移行に伴うスキーマ調整。
*   **JDBC動的接続**: Render の環境変数を Spring Boot で解析し、JDBC URL を動的に生成する `EnvironmentPostProcessor` の実装。

## 2. データの可搬性向上（CSV Export）
*   **機能**: 筆記・技能それぞれの学習全履歴を CSV 形式で一括ダウンロード。
*   **技術的工夫**: 
    *   Excel等での文字化けを防ぐため、UTF-8 ではなく **BOM付与済み UTF-8** を採用。
    *   マルチユーザー対応のため、現在ログイン中のユーザーデータのみを抽出するフィルタリング。

## 3. モバイル最適化とトラブルシューティング
*   **Viewport設定**: すべての HTML テンプレートに `<meta name="viewport" ...>` を追加し、スマホでの適切なスケーリングを確保。
*   **モバイルUI構築**: 
    *   ボタンサイズの拡大（タップミスの防止）。
    *   表（テーブル）の横揺れ防止と、ダッシュボード要素の縦積みレイアウトへの変更。
*   **403 Forbidden の解消**: 
    *   Render のプロキシ環境に対応するため、`server.forward-headers-strategy=native` を適用。
    *   Cookie の `same-site=lax` および `secure=true` 設定によるセッション維持の安定化。
