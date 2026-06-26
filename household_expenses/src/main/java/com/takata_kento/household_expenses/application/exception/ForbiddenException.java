package com.takata_kento.household_expenses.application.exception;

/**
 * 認証済みユーザーに操作対象リソースへの権限がない場合に送出する例外。
 * 所有権違反・グループ外リソースへのアクセスなどが該当する。
 * HTTP では 403 Forbidden に対応する。
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
