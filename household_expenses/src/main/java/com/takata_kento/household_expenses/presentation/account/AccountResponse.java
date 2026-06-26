package com.takata_kento.household_expenses.presentation.account;

import com.takata_kento.household_expenses.domain.account.FinancialAccount;
import com.takata_kento.household_expenses.domain.valueobject.AccountName;

/**
 * 金融口座のレスポンス表現。
 *
 * @param id 口座番号（6〜8桁の数字文字列）
 * @param bankName 銀行名
 * @param accountName 口座名（任意ラベル、未設定の場合は null）
 * @param balance 残高
 * @param mainAccount メイン口座フラグ
 */
public record AccountResponse(String id, String bankName, String accountName, int balance, boolean mainAccount) {
    public static AccountResponse from(FinancialAccount account) {
        return new AccountResponse(
            account.id().value(),
            account.bankName().value(),
            account.accountName().map(AccountName::value).orElse(null),
            account.balance().amount(),
            account.isMainAccount()
        );
    }
}
