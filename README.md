# Denko1-Tracker ⚡
### 第一種電気工事士 試験対策 学習トラッカー

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.5-6DB33F?logo=springboot\u0026logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk\u0026logoColor=white)](https://openjdk.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql\u0026logoColor=white)](https://www.postgresql.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

「第一種電気工事士」の合格を最短距離で勝ち取るための、分析特化型学習支援Webアプリケーションです。  
自身の弱点を視覚的に把握し、効率的な学習サイクルを構築します。

---

## 🚀 解決する課題

第一種電気工事士の試験範囲は広く、特に筆記試験では計算から法令まで多岐にわたる知識が求められます。
- 「自分がどの分野で点数を落としているのかが曖昧」
- 「技能試験の施工時間がなかなか短縮されない」
- 「過去の成長記録がバラバラで管理しにくい」

Denko1-Tracker は、これらの課題を「データ」と「視覚化」で解決します。

## ✨ 主要機能

### 1. 筆記試験（過去問）管理 ＆ 弱点分析
- **分野別記録**: 過去問の得点を「計算」「記憶」「図面」「法令」の4つの重要カテゴリに分けて記録。
- **レーダーチャート分析**: 蓄積されたデータから自動的に正答率を算出し、チャートで視覚化。
- **最優先復習アラート**: 最も正答率が低い分野を「現在の弱点」として自動特定し、重点的な学習を促します。

### 2. 技能試験 タイムトライアル記録
- **施工タイム管理**: 全10問の公表問題について、完成時間と合否（欠陥の有無）を記録。
- **成長の軌跡**: 制限時間（60分）との差を意識し、合格レベルへの到達度を通算記録で振り返れます。

### 3. データエクスポート機能
- **CSVダウンロード**: 筆記・技能の全記録を一括ダウンロード。ExcelやGoogleスプレッドシートでの独自分析やバックアップが可能です。

### 4. セキュアな個人データ管理 ＆ モバイル最適化
- **ユーザー認証**: Spring Security によるセキュアなログイン機能。
- **レスポンシブデザイン**: スマートフォンでの全画面操作に最適化。試験直前の見直しもスマホでスマートに行えます。

## 🛠️ 技術スタック

- **Backend**: Java 21 / Spring Boot 4.0.5 / Spring Security 7.0
- **Frontend**: Thymeleaf / Vanilla CSS / Chart.js
- **Database**: PostgreSQL / H2 (Development)
- **Deployment**: Docker / Render.com

---

## 🎨 デザインコンセプト
**"Safety First & Modern Tech"**
現場作業の「安全第一」を想起させるアンバー（琥珀色）をアクセントに、信頼感のある Glassmorphism（ガラス質感）のダークテーマを採用。モチベーションを維持する洗練されたUIを提供します。

## 🏁 クイックスタート

### ローカル開発環境での実行
1. リポジトリをクローン: `git clone <repository-url>`
2. H2 Database を使用した起動:
```bash
./mvnw spring-boot:run
```
3. ブラウザでアクセス: `http://localhost:8081`

### デプロイ (Render.com)
本プロジェクトは `render.yaml` による「Infrastructure as Code」に対応しています。
1. GitHub リポジトリを Render に接続。
2. `render.yaml` を読み込むことで、DB (PostgreSQL) と Webサービスが自動構成されます。

---

## 📜 ライセンス
[MIT License](LICENSE)

## 👤 開発者
- **Author**: Leon-Papa
- **Objective**: 効率的な資格取得と、エンジニアとしての技術力証明（ポートフォリオ）

---
> [!TIP]
> **AI駆動開発 (AIDD) ガイドライン**  
> 本プロジェクトで採用した「AIを活用した効率的な開発手法」を抽象化した設計図を [docs/aidd_framework/blueprint.md](./docs/aidd_framework/blueprint.md) に公開しています。次回のプロジェクト開始時に AI に読み込ませることで、同様の高品質な開発プロセスを再現可能です。

> [!TIP]
> 開発の詳細な軌跡（トラブルシューティングや修正履歴）は `docs/walkthrough.md` に記録されています。
