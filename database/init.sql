-- 拡張機能の有効化
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ユーザーグループテーブル
CREATE TABLE user_group (
    id BIGSERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL,
    month_start_day INTEGER NOT NULL DEFAULT 1 CHECK (month_start_day >= 1 AND month_start_day <= 31),
    created_by_user_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- ユーザーテーブル
CREATE TABLE "users" (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    user_group_id BIGINT REFERENCES user_group(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    enabled boolean
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
    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
    invited_user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    invited_by_user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'rejected')),
    invited_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (user_group_id, invited_user_id)
);

-- 金融口座テーブル
CREATE TABLE financial_account (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    account_name VARCHAR(255) NOT NULL,
    balance INTEGER NOT NULL DEFAULT 0,
    is_main_account BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- 預金残高編集履歴テーブル
CREATE TABLE balance_edit_history (
    id BIGSERIAL PRIMARY KEY,
    financial_account_id BIGINT NOT NULL REFERENCES financial_account(id) ON DELETE CASCADE,
    old_balance INTEGER NOT NULL,
    new_balance INTEGER NOT NULL,
    edit_reason TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 月次予算テーブル
CREATE TABLE monthly_budget (
    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
    budget_amount INTEGER NOT NULL,
    set_by_user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (user_group_id, year, month)
);

-- 生活費分類テーブル
CREATE TABLE living_expense_category (
    id BIGSERIAL PRIMARY KEY,
    user_group_id BIGINT REFERENCES user_group(id) ON DELETE CASCADE,
    category_name VARCHAR(255) NOT NULL,
    description TEXT,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- 日次収支テーブル
CREATE TABLE daily_transaction (
    user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    transaction_date DATE NOT NULL,
    income INTEGER NOT NULL DEFAULT 0,
    total_expense INTEGER NOT NULL DEFAULT 0,
    personal_expense INTEGER NOT NULL DEFAULT 0,
    financial_account_id BIGINT NOT NULL REFERENCES financial_account(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (user_id, transaction_date)
);

-- 日次生活費テーブル
CREATE TABLE daily_living_expense (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    transaction_date DATE NOT NULL,
    living_expense_category_id BIGINT NOT NULL REFERENCES living_expense_category(id) ON DELETE CASCADE,
    amount INTEGER NOT NULL,
    memo TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (user_id, transaction_date) REFERENCES daily_transaction(user_id, transaction_date) ON DELETE CASCADE
);

-- 日次個人支出テーブル
CREATE TABLE daily_personal_expense (
    user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    transaction_date DATE NOT NULL,
    sequence_no INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (user_id, transaction_date, sequence_no),
    FOREIGN KEY (user_id, transaction_date) REFERENCES daily_transaction(user_id, transaction_date) ON DELETE CASCADE
);

-- 日次予算残高テーブル
CREATE TABLE daily_budget_balance (
    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
    transaction_date DATE NOT NULL,
    total_living_expense INTEGER NOT NULL DEFAULT 0,
    budget_balance INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (user_group_id, transaction_date)
);

-- 固定費分類テーブル
CREATE TABLE fixed_expense_category (
    id BIGSERIAL PRIMARY KEY,
    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
    category_name VARCHAR(255) NOT NULL,
    description TEXT,
    default_amount INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- 固定費履歴テーブル
CREATE TABLE fixed_expense_history (
    id BIGSERIAL PRIMARY KEY,
    fixed_expense_category_id BIGINT NOT NULL REFERENCES fixed_expense_category(id) ON DELETE CASCADE,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
    amount INTEGER NOT NULL,
    effective_date DATE,
    memo TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- 月次貯金テーブル
CREATE TABLE monthly_saving (
    user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
    saving_amount INTEGER NOT NULL,
    financial_account_id BIGINT NOT NULL REFERENCES financial_account(id) ON DELETE CASCADE,
    memo TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (user_id, year, month)
);

-- インデックスの作成（パフォーマンス向上のため）
CREATE INDEX idx_user_username ON "users"(username);
CREATE INDEX idx_user_group_id ON "users"(user_group_id);
CREATE INDEX idx_financial_account_user_id ON financial_account(user_id);
CREATE INDEX idx_financial_account_main ON financial_account(user_id, is_main_account) WHERE is_main_account = TRUE;
CREATE INDEX idx_daily_transaction_date ON daily_transaction(transaction_date);
CREATE INDEX idx_daily_living_expense_date ON daily_living_expense(transaction_date);
CREATE INDEX idx_daily_personal_expense_date ON daily_personal_expense(transaction_date);
CREATE INDEX idx_daily_budget_balance_date ON daily_budget_balance(transaction_date);
CREATE INDEX idx_monthly_budget_year_month ON monthly_budget(year, month);
CREATE INDEX idx_fixed_expense_history_year_month ON fixed_expense_history(year, month);
CREATE INDEX idx_monthly_saving_year_month ON monthly_saving(year, month);

-- 更新日時の自動更新のためのトリガー関数
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 各テーブルに更新日時トリガーを設定
CREATE TRIGGER update_user_updated_at BEFORE UPDATE ON "users" FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_user_group_updated_at BEFORE UPDATE ON user_group FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_group_invitation_updated_at BEFORE UPDATE ON group_invitation FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_financial_account_updated_at BEFORE UPDATE ON financial_account FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_monthly_budget_updated_at BEFORE UPDATE ON monthly_budget FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_living_expense_category_updated_at BEFORE UPDATE ON living_expense_category FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_daily_transaction_updated_at BEFORE UPDATE ON daily_transaction FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_daily_living_expense_updated_at BEFORE UPDATE ON daily_living_expense FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_daily_personal_expense_updated_at BEFORE UPDATE ON daily_personal_expense FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_daily_budget_balance_updated_at BEFORE UPDATE ON daily_budget_balance FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_fixed_expense_category_updated_at BEFORE UPDATE ON fixed_expense_category FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_fixed_expense_history_updated_at BEFORE UPDATE ON fixed_expense_history FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_monthly_saving_updated_at BEFORE UPDATE ON monthly_saving FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- デフォルト生活費分類データの挿入（グローバル設定として）
INSERT INTO living_expense_category (user_group_id, category_name, description, is_default) VALUES
(NULL, '食費', '食材・飲料などの食事関連費用', TRUE),
(NULL, '食費', '外食関連費用', TRUE),
(NULL, '交通費', '電車・バス・タクシー・ガソリン代など', TRUE),
(NULL, '日用品', '洗剤・ティッシュ・シャンプーなどの生活用品', TRUE),
(NULL, '医療費', '病院代・薬代・健康関連費用', TRUE),
(NULL, '娯楽費', '映画・本・レジャー・趣味関連費用', TRUE),
(NULL, '衣服費', '衣類・靴・アクセサリーなど', TRUE),
(NULL, '教育費', '書籍・講座・資格取得などの学習費用', TRUE),
(NULL, 'その他', 'その他の生活費', TRUE);

-- 初期化完了メッセージ
SELECT 'Database initialization completed successfully!' as message;