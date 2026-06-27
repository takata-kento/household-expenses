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

## 開発の基本方針

領域（バックエンド/フロントエンド/インフラ）を問わず、以下を共通の基本方針とする。

- **TDD（テスト駆動開発）**: Kent BeckのRed-Green-Refactorサイクルを実践し、実装前にテストを書いて失敗させてから実装する
- **DDD（ドメイン駆動設計）**: Eric EvansのDDD原則に基づきモデリングし、設計文書（クラス図・ER図）との整合性を保つ
- **Clean Code**: Robert C. MartinのClean Codeを実践する

### 領域別の開発規約

各領域固有の規約・手順は対応するスキルを参照する。

- **バックエンド開発**（Spring Boot / Java / Spring Data JDBC）: `backend-development` スキルを参照
  - DDDレイヤードアーキテクチャ・パッケージ構成・実装順序
  - TDD/テストコーディング規約（AssertJ・Testcontainers・Given/When/Then）
  - Javaコーディング規約（getプレフィックス禁止・Optional活用）
  - Spring Data JDBC値オブジェクト設定
