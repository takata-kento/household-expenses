# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

家計簿アプリケーション - 紙の家計簿を電子化したWebアプリケーション

### アーキテクチャ構成
- **バックエンド**: Spring Boot + Java + PostgreSQL
- **データアクセス**: Spring Data JDBC（JPAではない）
- **フロントエンド**: Next.js（React + TypeScript）
- **データベース**: PostgreSQL

> 各バージョンは `household_expenses/pom.xml` および `household-expenses-front/package.json` を参照（単一情報源として管理）。
