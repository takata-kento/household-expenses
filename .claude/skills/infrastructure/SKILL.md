---
name: infrastructure
description: 家計簿アプリのインフラ（PostgreSQL / Docker）。データベーススキーマ（13テーブル構成）、DB起動コマンド（docker-compose）、ER図の参照先を含む。データベース構成の確認・DB環境の起動・スキーマ設計を扱う際に参照する。
---

# インフラ・データベース

家計簿アプリのデータベース（PostgreSQL）とコンテナ環境に関する情報。

## データベース起動

```bash
docker-compose up -d    # PostgreSQL起動
docker-compose down     # コンテナ停止
```

## データベーススキーマ

13テーブル構成のPostgreSQL設計:
- **ユーザー管理**: users, authorities, user_group, group_invitation
- **金融管理**: financial_account, balance_edit_history
- **予算管理**: monthly_budget, daily_budget_balance
- **支出管理**: daily_transaction, daily_living_expense, daily_personal_expense
- **固定費管理**: fixed_expense_category, fixed_expense_history
- **貯金管理**: monthly_saving
- **分類管理**: living_expense_category

詳細なテーブル定義・リレーションは `documents/er_diagram.md`（Mermaid形式）を参照。
