# [Phase 3] 画面・UIの統合 (フロントエンド)

Phase 2 でロジックが完成しました。
このフェーズでは、昨日作成した高品質なデザインと計算ロジックを統合し、ユーザーが実際に触れる形にします。

## 1. 構成内容

### フロントエンド移植 (Presenter)
昨日作成したCSSとHTMLテンプレートをプロジェクトに組み込みます。

* **静的リソース**: `style.css` (Glassmorphismデザイン)
* **テンプレート**: `index.html` (ダッシュボード), `record.html` (データ入力)
* **グラフ表示**: Chart.js を用いたレーダーチャート

### コントローラーの実装 (WebController)
各画面への遷移と、フォームからのデータ受け取りを担当します。

* **WebController**
  * `/`: ダッシュボード表示（弱点分析結果を含む）
  * `/record`: 記録入力フォームの表示
  * `/record/written`: 筆記試験の保存
  * `/record/skill`: 技能試験の保存

## 2. 実装ファイル詳細
1. `src/main/resources/static/css/style.css`
2. `src/main/resources/templates/index.html`
3. `src/main/resources/templates/record.html`
4. `src/main/java/com/example/denko1_tracker/controller/WebController.java`

## 3. 動作確認項目
* `http://localhost:8081/` にアクセスし、デザインが昨日同様に美しく表示されること。
* 実際にデータを入力し、トップ画面のグラフと履歴テーブルがリアルタイムに更新されること。
