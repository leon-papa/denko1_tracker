# [Phase 2] ビジネスロジックとテスト駆動開発

Phase 1 のデータベース土台構築が完了しました。
次に、このアプリの「頭脳」となる**弱点分析ロジック**を実装し、それが数学的に正しいことをテストコードで証明します。

## 1. 構成内容

### 弱点分析サービス (Service Layer)
各カテゴリ（計算・記憶・図面・法令）の取得点数から、正答率（%）を算出するロジックを実装します。

* **WeaknessAnalysisService**
  * `calculateAnalysis`: 分野別の正答率を計算
  * `findWeakestCategory`: 最も低い正答率の分野を特定

### 配点設定（定数化）
将来の変更を容易にするため、配点をソースコード内で定数として定義します。
* 計算: 20点 / 記憶: 30点 / 図面: 30点 / 法令: 20点

### 自動テスト (Test Layer)
JUnit 5を用いて、ロジックが期待通りに動くかを徹底的にテストします。

* **WeaknessAnalysisServiceTest**
  * 正常系テスト（一般的なスコア）
  * 境界値テスト（満点、0点）
  * エッジケース（同点時の優先順位など）

## 2. 実装ファイル詳細
1. `src/main/java/com/example/denko1_tracker/service/WeaknessAnalysisService.java`
2. `src/test/java/com/example/denko1_tracker/service/WeaknessAnalysisServiceTest.java`

## 3. 動作確認項目
* `./mvnw test` を実行し、全てのテストが `BUILD SUCCESS` となること。
