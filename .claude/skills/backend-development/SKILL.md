---
name: backend-development
description: 家計簿アプリのバックエンド（Spring Boot 3.5.3 + Java 21 + Spring Data JDBC + PostgreSQL）開発の規約。DDDレイヤードアーキテクチャとパッケージ構成、実装順序、TDD/テストコーディング規約（AssertJ・Testcontainers・Given/When/Then・テスト対象クラスの利用原則）、Javaコーディング規約（getプレフィックス禁止・Optional活用）、Spring Data JDBC値オブジェクト設定を含む。household_expenses/ 配下のバックエンドコード実装・テスト作成・リファクタリング時に参照する。
---

# バックエンド開発ガイド（household_expenses/）

Spring Boot + Java 21 + Spring Data JDBC によるバックエンド開発時に従う規約。

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

### 実装順序

1. **Domain層**: エンティティ・値オブジェクト・リポジトリインターフェース
2. **Infrastructure層**: リポジトリ実装
3. **Application層**: Service クラス
4. **Presentation層**: Controller

## テスト戦略

- Kent Beckとt-wadaのTDDサイクルに従った開発
- 単体テスト: JUnit + AssertJ（JUnit標準のAssertionsではない）
- 統合テスト: Testcontainers + PostgreSQL
- ドメインロジックの重点的なテスト

### TDD実装の具体的遵守事項

Red-Green-Refactorサイクルの実践

- **実装前にテストを作成**: 機能実装する前に、まずテストを書いて失敗させる（Red）
- **最小限の実装で通す**: テストが通る最小限の実装を行う（Green）
- **リファクタリング**: コードの品質を向上させる（Refactor）

### テストコーディング規約

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

### TDD Red-Green-Refactorの厳密実践

- **空メソッド実装による失敗確認の重要性**: メソッドの存在確認と実際の動作確認を分離して行う
- **コンパイルエラー→実行時エラー→成功の段階的確認**: 各段階で期待通りの失敗が発生することを確認する
```java
// 段階1: コンパイルエラー（メソッド未実装）
// 段階2: 実行時エラー（空メソッドによる期待値との不一致）
// 段階3: 成功（正しい実装による期待値との一致）
```

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
