package com.takata_kento.household_expenses.application.exception;

/**
 * リソースの現在の状態とリクエストが競合する場合に送出する例外。
 * 重複登録（既に存在するリソースの再作成）などが該当する。
 * HTTP では 409 Conflict に対応する。
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
