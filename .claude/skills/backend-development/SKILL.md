---
name: backend-development
description: 家計簿アプリのバックエンド（Spring Boot + Java + Spring Data JDBC + PostgreSQL）開発の規約。DDDレイヤードアーキテクチャとパッケージ構成、TDD/テストコーディング規約（AssertJ・Testcontainers・Given/When/Then・テスト対象クラスの利用原則）、Javaコーディング規約（getプレフィックス禁止・Optional活用）、Spring Data JDBC値オブジェクト設定を含む。household_expenses/ 配下のバックエンドコード実装・テスト作成・リファクタリング時に参照する。
---

# バックエンド開発ガイド（household_expenses/）

Spring Boot + Java + Spring Data JDBC によるバックエンド開発時に従う規約。

## 開発コマンド

```bash
cd household_expenses
./mvnw spring-boot:run         # 開発サーバー起動
./mvnw test                    # テスト実行
./mvnw test -Dtest=ClassName   # 単一テストクラス実行
./mvnw package                 # アプリケーションビルド
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

## テスト戦略

- Kent Beckとt-wadaのTDDサイクルに従った開発
- 単体テスト: JUnit + AssertJ（JUnit標準のAssertionsではない）
- 統合テスト: Testcontainers + PostgreSQL
- ドメインロジックの重点的なテスト

### TDD Red-Green-Refactorの厳密実践

Red-Green-Refactorサイクルを実践する。

- **Red（テスト作成と失敗確認）**: 機能実装の前にテストを書いて失敗させる。このとき、テストクラス単体ではなく**テスト対象クラスのひな型（空メソッドを持つ空実装）もセットで作成する**。
  - テスト対象クラスが存在しないことによる **import のコンパイルエラーを Red 状態と誤認してはならない**。コンパイルエラーのままGreenフェーズへ移行しないこと。
  - ひな型を用意したうえでテストを実行し、空メソッドによる**アサーションの失敗（期待値との不一致）**として Red を確認する。
- **Green（最小限の実装）**: テストが通る最小限の実装を行う。
- **Refactor（リファクタリング）**: コードの品質を向上させる。

失敗確認はメソッドの存在確認と実際の動作確認を分離して段階的に行う。

```java
// ひな型作成後: テスト実行 → 空メソッドによるアサーション失敗（期待値との不一致）= Red
// 正しい実装後: テスト実行 → 期待値と一致 = Green
```

### テストコーディング規約

- **検証ライブラリ**: AssertJのBDDスタイルを使用（`import static org.assertj.core.api.BDDAssertions.*;`）。検証は`assertThat()`ではなく`then()`を用いる
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
    then(actual).isEqualTo(expected);
}
```

### Given部分の明確化

- **期待値の明示**: テストのGiven部分で予想される数値やデータを明確に変数として定義
```java
// Given
long expectedId = 1L;
String expectedUsername = "testuser";
Long expectedUserGroupId = 100L;
```

### テスト対象クラスの利用原則

テスト対象クラスはWhenおよびThenでしか使用してはいけない
つまりテスト対象クラスの使用は確認したい動作の実行か状態の検証のみで使用される

例：
repositoryクラスをテストしたい場合、Givenでデータの挿入をしたい場面がある。このような時にテスト対象のrepositoryクラスを使用してデータを挿入してはいけない。
この場合は`JdbcClient`クラスのようなフレームワークにデフォルトで備わっているクラスを使用したり、`@SQL`を使用してデータベースの初期化をする。

### テストコード構成の統一ルール

- **各getterメソッドの個別テスト必須**: コンストラクタテストとは別に、各getterメソッドを個別にテストする
- **更新メソッドでは他フィールドの不変性確認必須**: 更新対象以外のフィールドが変更されていないことを必ず検証する
- **失敗テスト実装→成功実装の2段階確認プロセス**: 実装前にテストが確実に失敗することを確認してから正しい実装を行う

## コーディング規約

### メソッド命名規則

- **getプレフィックス禁止**: プロパティの取得メソッドにgetをつけない
```java
// ❌ 禁止
public String getUsername() { return username; }

// ✅ 正しい
public String username() { return username; }
```

### null安全性

- **Optionalの活用**: nullable なフィールドはOptionalを使用してnull安全にする
```java
// user_group_id がnullableの場合
private Optional<UserGroupId> userGroupId;
```

## リファクタリングの判断基準

- **重複コード**: 既存フレームワーク機能との重複を避ける
  - 例: `findByUserId`は`CrudRepository.findById`と重複するため削除
- **TDDサイクルでのリファクタリング**: 機能追加や変更時もテストファーストで実装

## Spring Data JDBC値オブジェクト設定

- **設定クラスの配置**: フレームワーク設定は`config`パッケージに分離
- **AbstractJdbcConfiguration継承**: カスタムコンバーターの登録に必要
