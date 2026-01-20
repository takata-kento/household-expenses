# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

家計簿アプリケーション - 紙の家計簿を電子化したWebアプリケーション

### アーキテクチャ構成
- **バックエンド**: Spring Boot 3.5.3 + Java 21 + PostgreSQL
- **データアクセス**: Spring Data JDBC（JPAではない）
- **フロントエンド**: Vue.js 3.5.26
- **データベース**: PostgreSQL 15

## 開発コマンド

### バックエンド（household_expenses/）
```bash
cd household_expenses
./mvnw spring-boot:run    # 開発サーバー起動
./mvnw test              # テスト実行
./mvnw test -Dtest=ClassName  # 単一テストクラス実行
./mvnw package           # アプリケーションビルド
```

### フロントエンド（front/household-expenses）
```bash
cd front/household-expenses
npm run dev             # 開発サーバー起動
npm run build           # プロダクションビルド
npm run start           # プロダクションサーバー起動
npm run lint            # ESLint実行
```

### データベース
```bash
docker-compose up -d postgres    # PostgreSQL起動
docker-compose down             # コンテナ停止
```

## アーキテクチャ設計原則

### Eric EvansのDDD（ドメイン駆動設計）
**レイヤードアーキテクチャ**:
- **Presentation層**: REST Controller（ユーザーインターフェース）
- **Application層**: Service クラス（アプリケーションロジック）
- **Domain層**: エンティティ、値オブジェクト、リポジトリインターフェース（ビジネスロジック）
- **Infrastructure層**: リポジトリ実装、データアクセス（技術的関心事）

**パッケージ構成**:
```
com.takata_kento.household_expenses/
├── presentation/        # Controller
├── application/         # Service
├── domain/             # Entity, Repository Interface
│   └── valueobject/    # Value Object
└── infrastructure/     # Repository Implementation
```

### Kent BeckのTDD（テスト駆動開発）
- **Red-Green-Refactorサイクル**を実践
- 実装前にテストを作成
- Testcontainersを使用した統合テスト環境

## 主要機能・データ設計

### 機能領域
1. **変動費の記録**: 日次収入・支出の記録と自動計算
2. **固定費の記録**: 月次固定費の種類・金額管理
3. **貯金額の記録**: 月次貯金実績の記録
4. **預金額の管理**: 複数金融口座の管理
5. **ユーザー管理**: 認証・グループ機能・招待システム

### データ共有ポリシー
- **共有データ**: 生活費記録、固定費情報、予算情報（グループメンバー全員が閲覧可能）
- **非共有データ**: 個人支出詳細、収入情報、貯金額、預金残高（記録者本人のみ閲覧可能）

### セキュリティ要件
- Spring Security による認証・認可
- パスワード要件: 8文字以上の英数字混合
- セッション管理: 1週間の自動ログアウト
- グループ間データ分離

## データベーススキーマ

13テーブル構成のPostgreSQL設計:
- **ユーザー管理**: users, authorities, user_group, group_invitation
- **金融管理**: financial_account, balance_edit_history
- **予算管理**: monthly_budget, daily_budget_balance  
- **支出管理**: daily_transaction, daily_living_expense, daily_personal_expense
- **固定費管理**: fixed_expense_category, fixed_expense_history
- **貯金管理**: monthly_saving
- **分類管理**: living_expense_category

## 設計文書

詳細な設計仕様は `documents/` ディレクトリを参照:
- `requirements_definition.md`: 機能要件・業務ルール
- `class_diagram.md`: クラス設計（Mermaid形式）
- `er_diagram.md`: データベース設計（Mermaid形式）

## 開発時の重要事項

### 実装順序
1. **Domain層**: エンティティ・値オブジェクト・リポジトリインターフェース
2. **Infrastructure層**: リポジトリ実装
3. **Application層**: Service クラス
4. **Presentation層**: Controller

### テスト戦略
- Kent Beckとt-wadaのTDDサイクルに従った開発
- 単体テスト: JUnit + AssertJ（JUnit標準のAssertionsではない）
- 統合テスト: Testcontainers + PostgreSQL
- ドメインロジックの重点的なテスト

#### TDD実装の具体的遵守事項
Red-Green-Refactorサイクルの実践

- **実装前にテストを作成**: 機能実装する前に、まずテストを書いて失敗させる（Red）
- **最小限の実装で通す**: テストが通る最小限の実装を行う（Green）  
- **リファクタリング**: コードの品質を向上させる（Refactor）

#### テストコーディング規約
- **検証ライブラリ**: AssertJを使用（`import static org.assertj.core.api.Assertions.*;`）
- **テスト構造**: Given/When/Thenコメントを必ず記述
  - `// Given`: テストの実行条件部分
  - `// When`: どのような操作を行うかを表す
  - `// Then`: 結果の検証部分
- **検証対象変数**: 検証する変数名は`actual`とする
- **例**:
```java
@Test
void testAdd() {
    // Given
    Money money1 = new Money(1000);
    Money money2 = new Money(500);
    Money expected = new Money(1500);

    // When
    Money actual = money1.add(money2);
    
    // Then
    assertThat(actual).isEqualTo(expected);
}
```

#### Given部分の明確化
- **期待値の明示**: テストのGiven部分で予想される数値やデータを明確に変数として定義
```java
// Given
long expectedId = 1L;
String expectedUsername = "testuser";
Long expectedUserGroupId = 100L;
```

#### テスト対象クラスの利用原則
テスト対象クラスはWhenおよびThenでしか使用してはいけない
つまりテスト対象クラスの使用は確認したい動作の実行か状態の検証のみで使用される

例：
repositoryクラスをテストしたい場合、Givenでデータの挿入をしたい場面がある。このような時にテスト対象のrepositoryクラスを使用してデータを挿入してはいけない。
この場合は`JdbcClient`クラスのようなフレームワークにデフォルトで備わっているクラスを使用したり、`@SQL`を使用してデータベースの初期化をする。

#### テストコード構成の統一ルール
- **各getterメソッドの個別テスト必須**: コンストラクタテストとは別に、各getterメソッドを個別にテストする
- **更新メソッドでは他フィールドの不変性確認必須**: 更新対象以外のフィールドが変更されていないことを必ず検証する
- **失敗テスト実装→成功実装の2段階確認プロセス**: 実装前にテストが確実に失敗することを確認してから正しい実装を行う

#### TDD Red-Green-Refactorの厳密実践
- **空メソッド実装による失敗確認の重要性**: メソッドの存在確認と実際の動作確認を分離して行う
- **コンパイルエラー→実行時エラー→成功の段階的確認**: 各段階で期待通りの失敗が発生することを確認する
```java
// 段階1: コンパイルエラー（メソッド未実装）
// 段階2: 実行時エラー（空メソッドによる期待値との不一致）
// 段階3: 成功（正しい実装による期待値との一致）
```

### コード品質
- 設計文書（クラス図・ER図）との整合性確保
- Eric EvansのDDD原則に基づいたモデリング
- Robert C.MartinのClean Codeの実践

### コーディング規約の具体的指導

#### メソッド命名規則
- **getプレフィックス禁止**: プロパティの取得メソッドにgetをつけない
```java
// ❌ 禁止
public String getUsername() { return username; }

// ✅ 正しい
public String username() { return username; }
```

#### null安全性
- **Optionalの活用**: nullable なフィールドはOptionalを使用してnull安全にする
```java
// user_group_id がnullableの場合
private Optional<UserGroupId> userGroupId;
```

### リファクタリングの判断基準
- **重複コード**: 既存フレームワーク機能との重複を避ける
  - 例: `findByUserId`は`CrudRepository.findById`と重複するため削除
- **TDDサイクルでのリファクタリング**: 機能追加や変更時もテストファーストで実装

### Spring Data JDBC値オブジェクト設定
- **設定クラスの配置**: フレームワーク設定は`config`パッケージに分離
- **AbstractJdbcConfiguration継承**: カスタムコンバーターの登録に必要
