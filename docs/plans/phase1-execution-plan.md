# [Phase 1] データベース設計と土台構築

Phase 0の「初期疎通（Hello World）」および「セキュリティの一時無効化」が完了しました。
次に、アプリケーションの中核となるデータを扱うための**エンティティ（Entity）**と、データベース操作を行うための**リポジトリ（Repository）**を構築します。

## 1. 構成内容
講師のMVP要件定義に基づき、以下の3つのテーブルを構成します。

### Entityの作成 (Domain Layer)
Spring Data JPAを用いて、H2 Database上に自動的にテーブルが作成されるようにEntityクラスを定義します。

* **User**: ユーザー情報（ログイン用）
* **WrittenExamRecord**: 筆記試験の記録（年度、カテゴリ別得点）
* **SkillExamRecord**: 技能試験の記録（問題番号、タイム、合否）

### Repositoryの作成 (Repository Layer)
データの保存・取得・検索を行うための標準的なインターフェースを作成します。

* **UserRepository**
* **WrittenExamRecordRepository**
* **SkillExamRecordRepository**

## 2. 実装ファイル詳細

### Entity
1. `src/main/java/com/example/denko1_tracker/entity/User.java`
2. `src/main/java/com/example/denko1_tracker/entity/WrittenExamRecord.java`
3. `src/main/java/com/example/denko1_tracker/entity/SkillExamRecord.java`

### Repository
1. `src/main/java/com/example/denko1_tracker/repository/UserRepository.java`
2. `src/main/java/com/example/denko1_tracker/repository/WrittenExamRecordRepository.java`
3. `src/main/java/com/example/denko1_tracker/repository/SkillExamRecordRepository.java`

## 3. 動作確認項目
* `mvn compile` によるビルドの成功。
* アプリケーション起動後、`http://localhost:8081/h2-console` にアクセスし、各テーブルが自動生成されていることを確認。
