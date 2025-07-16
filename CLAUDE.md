# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

家計簿アプリケーション - 紙の家計簿を電子化したWebアプリケーション

### アーキテクチャ構成
- **バックエンド**: Spring Boot 3.5.3 + Java 21 + PostgreSQL
- **データアクセス**: Spring Data JDBC（JPAではない）
- **フロントエンド**: Next.js 15.3.5 + React 19 + Tailwind CSS 4
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

### フロントエンド（household-expenses-front/）
```bash
cd household-expenses-front
npm run dev              # 開発サーバー起動（Turbopack使用）
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
- Kent BeckのTDDサイクルに従った開発
- 単体テスト: JUnit + AssertJ（JUnit標準のAssertionsではない）
- 統合テスト: Testcontainers + PostgreSQL
- ドメインロジックの重点的なテスト

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
    
    // When
    Money actual = money1.add(money2);
    
    // Then
    assertThat(actual.amount()).isEqualTo(1500);
}
```

### コード品質
- 設計文書（クラス図・ER図）との整合性確保
- Eric EvansのDDD原則に基づいたモデリング
- Robert C.MartinのClean Codeの実践
