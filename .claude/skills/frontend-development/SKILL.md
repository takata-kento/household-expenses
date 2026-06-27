---
name: frontend-development
description: 家計簿アプリのフロントエンド（Next.js / React / TypeScript）開発の規約とコマンド。household-expenses-front 配下のフロントエンドコード実装・ビルド・lint・開発サーバー起動時に参照する。
---

# フロントエンド開発ガイド（household-expenses-front）

Next.js（React + TypeScript + Tailwind CSS）によるフロントエンド開発時に参照する。

## 開発コマンド

```bash
cd household-expenses-front
npm run dev             # 開発サーバー起動（next dev --turbopack）
npm run build           # プロダクションビルド（next build）
npm run start           # プロダクションサーバー起動（next start）
npm run lint            # ESLint実行（next lint）
```
