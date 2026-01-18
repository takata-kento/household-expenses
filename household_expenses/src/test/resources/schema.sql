-- テスト用スキーマ定義
-- トリガーとデフォルトデータは除外し、テーブル構造のみ定義

-- 拡張機能の有効化
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ユーザーグループテーブル
CREATE TABLE user_group (
    id BIGSERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL,
    month_start_day INTEGER NOT NULL DEFAULT 1 CHECK (month_start_day >= 1 AND month_start_day <= 31),
    created_by_user_id VARCHAR(36),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0
);

-- ユーザーテーブル
CREATE TABLE "users" (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    user_group_id BIGINT REFERENCES user_group(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    enabled boolean,
    version INTEGER DEFAULT 0
);

-- ユーザーグループの作成者外部キー制約を追加
ALTER TABLE user_group ADD FOREIGN KEY (created_by_user_id) REFERENCES "users"(id) ON DELETE SET NULL;

CREATE TABLE "authorities" (
    username VARCHAR(255) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);
CREATE UNIQUE INDEX ix_auth_username ON authorities (username,authority);

-- グループ招待テーブル
CREATE TABLE group_invitation (
    id VARCHAR(36) PRIMARY KEY,
    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
    invited_user_id VARCHAR(36) NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    invited_by_user_id VARCHAR(36) NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    invited_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    UNIQUE (user_group_id, invited_user_id)
);

-- 金融口座テーブル
CREATE TABLE financial_account (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    account_name VARCHAR(255) NOT NULL,
    balance INTEGER NOT NULL DEFAULT 0,
    is_main_account BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0
);

-- 預金残高編集履歴テーブル
CREATE TABLE balance_edit_history (
    id VARCHAR(36) PRIMARY KEY,
    financial_account_id VARCHAR(36) NOT NULL REFERENCES financial_account(id) ON DELETE CASCADE,
    old_balance INTEGER NOT NULL,
    new_balance INTEGER NOT NULL,
    edit_reason TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- 月次予算テーブル
CREATE TABLE monthly_budget (
    id BIGSERIAL PRIMARY KEY,
    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
    budget_amount INTEGER NOT NULL,
    set_by_user_id VARCHAR(36) NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0,
    UNIQUE (user_group_id, year, month)
);

-- 生活費分類テーブル
CREATE TABLE living_expense_category (
    id BIGSERIAL PRIMARY KEY,
    user_group_id BIGINT REFERENCES user_group(id) ON DELETE CASCADE,
    category_name VARCHAR(255) NOT NULL,
    description TEXT,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0
);

-- 日次グループ収支テーブル
CREATE TABLE daily_group_transaction (
    id VARCHAR(36) PRIMARY KEY,
    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
    transaction_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0,
    UNIQUE (user_group_id, transaction_date)
);

-- 日次生活費テーブル
CREATE TABLE daily_living_expense (
    id VARCHAR(36) PRIMARY KEY,
    daily_group_transaction_id VARCHAR(36) NOT NULL REFERENCES daily_group_transaction(id) ON DELETE CASCADE,
    user_id VARCHAR(36) NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    living_expense_category_id BIGINT NOT NULL REFERENCES living_expense_category(id) ON DELETE CASCADE,
    amount INTEGER NOT NULL,
    memo TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0
);

-- 日次個人収支テーブル
CREATE TABLE daily_personal_transaction (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    transaction_date DATE NOT NULL,
    income INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0,
    UNIQUE (user_id, transaction_date)
);

-- 日次個人支出テーブル
CREATE TABLE daily_personal_expense (
    id VARCHAR(36) PRIMARY KEY,
    daily_personal_transaction_id VARCHAR(36) NOT NULL REFERENCES daily_personal_transaction(id) ON DELETE CASCADE,
    daily_personal_transaction_key INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    memo TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0
);

-- 日次予算残高テーブル
CREATE TABLE daily_budget_balance (
    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
    transaction_date DATE NOT NULL,
    total_living_expense INTEGER NOT NULL DEFAULT 0,
    budget_balance INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0,
    PRIMARY KEY (user_group_id, transaction_date)
);

-- 固定費分類テーブル
CREATE TABLE fixed_expense_category (
    id VARCHAR(36) PRIMARY KEY,
    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
    category_name VARCHAR(255) NOT NULL,
    description TEXT,
    default_amount INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0
);

-- 固定費履歴テーブル
CREATE TABLE fixed_expense_history (
    id VARCHAR(36) PRIMARY KEY,
    fixed_expense_category_id VARCHAR(36) NOT NULL REFERENCES fixed_expense_category(id) ON DELETE CASCADE,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
    amount INTEGER NOT NULL,
    effective_date DATE,
    memo TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0
);

-- 月次貯金テーブル
CREATE TABLE monthly_saving (
    user_id VARCHAR(36) NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
    saving_amount INTEGER NOT NULL,
    financial_account_id VARCHAR(36) NOT NULL REFERENCES financial_account(id) ON DELETE CASCADE,
    memo TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 0,
    PRIMARY KEY (user_id, year, month)
);

-- インデックスの作成（パフォーマンス向上のため）
CREATE INDEX idx_user_username ON "users"(username);
CREATE INDEX idx_user_group_id ON "users"(user_group_id);
CREATE INDEX idx_financial_account_user_id ON financial_account(user_id);
CREATE INDEX idx_financial_account_main ON financial_account(user_id, is_main_account) WHERE is_main_account = TRUE;
CREATE INDEX idx_daily_group_transaction_date ON daily_group_transaction(transaction_date);
CREATE INDEX idx_daily_group_transaction_user_group ON daily_group_transaction(user_group_id, transaction_date);
CREATE INDEX idx_daily_living_expense_group_transaction ON daily_living_expense(daily_group_transaction_id);
CREATE INDEX idx_daily_living_expense_user ON daily_living_expense(user_id);
CREATE INDEX idx_daily_personal_transaction_date ON daily_personal_transaction(transaction_date);
CREATE INDEX idx_daily_personal_transaction_user ON daily_personal_transaction(user_id, transaction_date);
CREATE INDEX idx_daily_personal_expense_transaction ON daily_personal_expense(daily_personal_transaction_id);
CREATE INDEX idx_daily_budget_balance_date ON daily_budget_balance(transaction_date);
CREATE INDEX idx_monthly_budget_year_month ON monthly_budget(year, month);
CREATE INDEX idx_fixed_expense_history_year_month ON fixed_expense_history(year, month);
CREATE INDEX idx_monthly_saving_year_month ON monthly_saving(year, month);
