---
name: infrastructure
description: 家計簿アプリのインフラ（PostgreSQL / Docker）。DB起動コマンド（docker-compose）、データベース設計（ER図）の参照先を含む。データベース構成の確認・DB環境の起動・スキーマ設計を扱う際に参照する。
---

# インフラ・データベース

家計簿アプリのデータベース（PostgreSQL）とコンテナ環境に関する情報。

## データベース起動

```bash
docker-compose up -d    # PostgreSQL起動
docker-compose down     # コンテナ停止
```

## データベース設計

テーブル構成・テーブル定義・リレーションは `documents/er_diagram.md`（Mermaid形式）を単一情報源として参照する。
